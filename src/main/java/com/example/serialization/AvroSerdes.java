package com.example.serialization;

import com.osc.kstreams.basicavro.EntitySentiment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.HashMap;
import java.util.Map;

public class AvroSerdes {

    public static SpecificAvroSerde<EntitySentiment> twitterSpecificAvroSerde() {
        SpecificAvroSerde<EntitySentiment> entitySentimentSpecificAvroSerde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        entitySentimentSpecificAvroSerde.configure(serdeConfig, false);
        return entitySentimentSpecificAvroSerde;
    }
}
