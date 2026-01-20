package com.jpmc.midascore.component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-core-group"
    )
    public void listen(String message) {
        try {
            Transaction transaction =
                    objectMapper.readValue(message, Transaction.class);

            // For Task 3 logic, this is where DB logic runs
            System.out.println("Received transaction: " + transaction);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
