package com.example.twitter;

import com.example.serialization.AvroSerdes;
import com.example.serialization.Tweet;
import com.example.serialization.TweetSerdes;
import com.example.serialization.TweetToSentimentConverter;
import com.osc.kstreams.basicavro.EntitySentiment;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.List;

@Slf4j
public class CryptoTopology {
    private static final  String TOPIC_INPUT = "twitter_input";

    public static Topology build() {
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<Void, Tweet> twitterStream = builder.stream(TOPIC_INPUT, Consumed.with(Serdes.Void(),  new TweetSerdes()));
        twitterStream.foreach(
                (key,value) -> {
                    if(value != null)
                        log.info("Tweet Text " + value.getText());
                }
        );
        // filter out retweets
        KStream<Void, Tweet> filtered =
                twitterStream.filter(
                        (key, tweet) -> {
                            return tweet != null && tweet.isRetweet();
                        });
        filtered.foreach(
                (key,value) -> {
                    if (value != null)
                        log.info("Filtered Tweet Text  " + value.getText() + " " + value.isRetweet());
                }
        );
        KStream<Void, EntitySentiment> enriched =
                filtered.flatMapValues(
                        (tweet) -> {
                            // perform entity-level sentiment analysis
                            List<EntitySentiment> results = TweetToSentimentConverter.getEntitySentiment(tweet);
                            return results;
                        });

        enriched.to("crypto-sentiment", Produced.with(Serdes.Void(), AvroSerdes.twitterSpecificAvroSerde()));
        return builder.build();
    }



}
