package com.example.videogame.serialization;

import com.example.videogame.HighScores;
import com.example.videogame.model.Player;
import com.example.videogame.model.Product;
import com.example.videogame.model.ScoreEvent;

import com.example.videogame.model.joined.Enriched;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;


public class JsonSerdes {

    public static Serde<ScoreEvent> scoreEventSerde() {
        JsonSerializer<ScoreEvent> serializer = new JsonSerializer<>();
        JsonDeSerializer<ScoreEvent> deSerializer = new JsonDeSerializer<>(ScoreEvent.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }

    public static Serde<Player> playerSerde() {
        JsonSerializer<Player> serializer = new JsonSerializer<>();
        JsonDeSerializer<Player> deSerializer = new JsonDeSerializer<>(Player.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }

    public static Serde<Product> productSerde() {
        JsonSerializer<Product> serializer = new JsonSerializer<>();
        JsonDeSerializer<Product> deSerializer = new JsonDeSerializer<>(Product.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }

    public static Serde<Enriched> enrichedSerde() {
        JsonSerializer<Enriched> serializer = new JsonSerializer<>();
        JsonDeSerializer<Enriched> deSerializer = new JsonDeSerializer<>(Enriched.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }

    public static Serde<HighScores> highScoresSerde() {
        JsonSerializer<HighScores> serializer = new JsonSerializer<>();
        JsonDeSerializer<HighScores> deSerializer = new JsonDeSerializer<>(HighScores.class);
        return Serdes.serdeFrom(serializer, deSerializer);
    }

}
