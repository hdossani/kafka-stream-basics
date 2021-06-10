package com.example.videogame;

import com.example.videogame.model.Player;
import com.example.videogame.model.Product;
import com.example.videogame.model.ScoreEvent;
import com.example.videogame.model.joined.Enriched;
import com.example.videogame.model.joined.ScoreWithPlayer;
import com.example.videogame.serialization.JsonSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

public class VideoGameTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();
        //Score Event Stream
        KStream<String, ScoreEvent> scoreEvents = builder.stream("score-events", Consumed.with(Serdes.ByteArray(), JsonSerdes.scoreEventSerde()))
                                                    .selectKey((k,v) -> v.getPlayerId().toString());

        //Players table
        KTable<String, Player> players = builder.table("players", Consumed.with(Serdes.String(), JsonSerdes.playerSerde()));

        //Product global table
        GlobalKTable<String, Product> products = builder.globalTable("products", Consumed.with(Serdes.String(), JsonSerdes.productSerde()));

        //Join params for scoreEvents -> players join
        Joined<String, ScoreEvent, Player> playerJoinParams = Joined.with(Serdes.String(), JsonSerdes.scoreEventSerde(), JsonSerdes.playerSerde());

        //Join ScoreEvents - Players
        ValueJoiner<ScoreEvent, Player, ScoreWithPlayer> scorePlayerJoiner = (score, player) -> new ScoreWithPlayer(score, player);
        KStream<String, ScoreWithPlayer> scoreWithPlayerKStream = scoreEvents.join(players, scorePlayerJoiner, playerJoinParams);
        /**
         * map score-with-player records to products
         *
         * <p>Regarding the KeyValueMapper param types: - String is the key type for the score events
         * stream - ScoreWithPlayer is the value type for the score events stream - String is the lookup
         * key type
         */
        KeyValueMapper<String, ScoreWithPlayer, String> keyValueMapper = (leftKey, scoreWithPlayer) -> {
            return String.valueOf(scoreWithPlayer.getEventScore().getProductId());
        };

        // join the withPlayers stream to the products global ktable
        ValueJoiner<ScoreWithPlayer, Product, Enriched> productJoiner = (scoreWithPlayer, product) -> new Enriched(scoreWithPlayer, product);
        KStream<String, Enriched> withProducts = scoreWithPlayerKStream.join(products, keyValueMapper, productJoiner);
        withProducts.print(Printed.<String, Enriched>toSysOut().withLabel("with-products"));

        /** Group the enriched product stream */
        KGroupedStream<String, Enriched> grouped = withProducts.groupBy(
                (key,value) -> value.getProductId().toString(),
                                Grouped.with(Serdes.String(), JsonSerdes.enrichedSerde())
        );

        /** The initial value of our aggregation will be a new HighScores instances */
        Initializer<HighScores> highScoresInitializer = HighScores::new;

        /** The logic for aggregating high scores is implemented in the HighScores.add method */
        Aggregator<String, Enriched, HighScores> highScoresAdder =
                (key, value, aggregate) -> aggregate.add(value);

        /** Perform the aggregation, and materialize the underlying state store for querying */
        KTable<String, HighScores> highScores =
                grouped.aggregate(
                        highScoresInitializer,
                        highScoresAdder,
                        Materialized.<String, HighScores, KeyValueStore<Bytes, byte[]>>
                                // give the state store an explicit name to make it available for interactive
                                // queries
                                        as("leader-boards")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.highScoresSerde()));

        highScores.toStream().to("high-scores");

        return builder.build();
    }
}
