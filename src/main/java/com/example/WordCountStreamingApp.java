package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import java.util.Properties;

public class WordCountStreamingApp {
    public static void main(String[] args) {
        Properties configProps = new Properties();
        configProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        configProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        configProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream("mytopic").mapValues(value -> String.valueOf(value.length())).to("my-output-topic");

        KafkaStreams streams = new KafkaStreams(builder.build(), configProps);
        streams.start();
    }
}
