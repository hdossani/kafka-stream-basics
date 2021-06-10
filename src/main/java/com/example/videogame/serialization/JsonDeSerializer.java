package com.example.videogame.serialization;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonDeSerializer<T> implements Deserializer<T> {

    public  JsonDeSerializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public JsonDeSerializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    private Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private Class<T> destinationClass;
    private Type reflectionTypeToken;

    @Override
    public T deserialize(String s, byte[] bytes) {
        if(bytes == null) {
            return null;
        }
        Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
    }
}
