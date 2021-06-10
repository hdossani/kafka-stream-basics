package com.example.serialization;

import com.osc.kstreams.basicavro.EntitySentiment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TweetToSentimentConverter {

    public static Double randomDouble() {
        return ThreadLocalRandom.current().nextDouble(0, 1);
    }

    public static List<EntitySentiment> getEntitySentiment(Tweet tweet) {
        List<EntitySentiment> results = new ArrayList<>();

        Iterable<String> words = Arrays.asList(tweet.getText().split(" "));
        for (String entity : words) {
            EntitySentiment entitySentiment =
                    EntitySentiment.newBuilder()
                            .setCreatedAt(tweet.getCreatedAt())
                            .setId(tweet.getId())
                            .setEntity(entity)
                            .setText(tweet.getText())
                            .setSalience(randomDouble())
                            .setSentimentScore(randomDouble())
                            .setSentimentMagnitude(randomDouble())
                            .build();

            results.add(entitySentiment);
        }
        return results;
    }

}
