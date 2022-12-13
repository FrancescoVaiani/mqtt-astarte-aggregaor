package com.secomind.poc.aggregator.aggregator;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
    public static void main(String[] args) {
        String brokerURI = "tcp://localhost:1883";
        String clientId = "aggregator";

        String[] topics = new String[]{"data/group/group_1/+", "data/single/device_1"};

        try {
            MqttClient client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("topic: " + topic);
                    System.out.println("Qos: " + message.getQos());
                    System.out.println("message content: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
            client.connect();
            for (String topic:topics) {
                client.subscribe(topic, 0);
            }
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
