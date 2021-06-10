package com.example.color;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

@Slf4j
public class UserColourTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream = builder.stream("user-colours", Consumed.with(Serdes.String(), Serdes.String()));
        stream
                .selectKey((k,v) -> v.split(",")[0])
                .mapValues(
                        v -> v.split(",")[1].trim())
                .filter(
                        (key, value) -> {

                            return value != null && (value.equals("green") || value.equals("red") || value.equals("blue"));
                        })
                .to("user-fav-colors");


            KTable<String, String> kTable = builder.table("user-fav-colors", Consumed.with(Serdes.String(), Serdes.String()));

            KTable<String, Long> userColourCount = kTable.groupBy(
                  (k, v) -> new KeyValue<>(v, v))
                    .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("test-color")
                            .withKeySerde(Serdes.String())
                           .withValueSerde(Serdes.Long()));

            //userColourCount.toStream().to("test-color");

        userColourCount.toStream().foreach(
                    (k,v) -> log.info("### K " + k + " ### V " + v)
           );




       // KStream<String, Long> colorstream = builder.stream("color-count", Consumed.with(Serdes.String(), Serdes.Long()));
       // colorstream.foreach(
       //             (k,v) -> log.info("### Key: " + k + " ### Value: " + v)
       //          );
        return builder.build();
    }
}
