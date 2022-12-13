package com.secomind.poc.aggregator.aggregator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AggregatorMqttEventCallback implements MqttCallback {
  @Override
  public void connectionLost(Throwable cause) {
    System.out.println("connectionLost: " + cause.getMessage());
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    System.out.println("topic: " + topic);
    System.out.println("Qos: " + message.getQos());
    System.out.println("message content: " + new String(message.getPayload()));
    final String[] topicParams = topic.split("/");
    String deviceId, groupId;
    if (topicParams.length == 4) {
      groupId = topicParams[2];
      deviceId = topicParams[3];
      System.out.println("Group ID: " + groupId + "\nDevice ID: " + deviceId);
    } else if (topicParams.length == 3) {
      deviceId = topicParams[2];
      System.out.println("Device ID: " + deviceId);
    } else {
      System.out.println("No data in topic");
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    System.out.println("deliveryComplete---------" + token.isComplete());
  }
}
