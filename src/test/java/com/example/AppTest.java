package com.example;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Kafka Streaming Tests")
public class AppTest 
{

    @BeforeAll
    static void init(){
        System.out.println("### init ### ");
    }

    @Test
    public void performTest(){
        System.out.println("### performTest ###");
        assertEquals(2, 2);
    }
}
