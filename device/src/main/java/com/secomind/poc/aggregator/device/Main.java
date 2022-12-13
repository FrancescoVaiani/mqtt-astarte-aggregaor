package com.secomind.poc.aggregator.device;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String brokerURI = "tcp://localhost:1883";
        String clientId = "client_1_1";
        String groupId = "group_1";
        String topic = "data/group/"+ groupId +"/" + clientId;
        Random rand = new Random();

        try {
            MqttClient client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
            client.connect();
            while(true){
                String content;
                int value = rand.nextInt(100);
                if (value == 0){
                    content = "Error";
                } else {
                    content = Integer.toString(value);
                }
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(0);
                client.publish(topic, message);
                System.out.println("Message published");
                System.out.println("topic: " + topic);
                System.out.println("message content: " + content);
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (MqttException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Interrupted.");
        }
    }
}
