package com.example.twitter;

import com.osc.kstreams.basicavro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import static org.apache.kafka.common.serialization.Serdes.String;

@Slf4j
public class TransactionTopology {

    private static final  String TOPIC_INPUT = "transactions";
    private static final  String TOPIC_OUTPUT = "transactions_output";

    public static void main(String[] args) {
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, Payment> avroPaymentStream = builder.stream(TOPIC_INPUT, Consumed.with(String(), paymentSpecificAvroSerde() ));

        avroPaymentStream.foreach(
                (key,value) -> log.info("Transaction " + value)
        );

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev2");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());


        final KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }


    static SpecificAvroSerde<Payment> paymentSpecificAvroSerde() {
        SpecificAvroSerde<Payment> movieAvroSerde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        movieAvroSerde.configure(serdeConfig, false);
        return movieAvroSerde;
    }

}
