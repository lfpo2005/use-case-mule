package com.lfpo.msuser.services.kafka;

import com.lfpo.msuser.models.UserModel;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;


@Service
public class UserProducer {

    private final Producer<String, String> producer;

    @Value("${kafka.topic.name}")
    private String topicName;


    public UserProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    public void sendUserDetails(UserModel userModel) {
        String userId = String.valueOf(userModel.getUserId());
        String fullName = userModel.getFullName();
        String cpf = userModel.getCpf();
        String userType = String.valueOf(userModel.getUserType());

        String userRecord = String.format("{\"userId\":\"%s\", \"fullName\":\"%s\", \"cpf\":\"%s\", \"userType\":\"%s\"}", userId, fullName, cpf, userType);
        producer.send(new ProducerRecord<>(topicName, userId, userRecord));
    }
}