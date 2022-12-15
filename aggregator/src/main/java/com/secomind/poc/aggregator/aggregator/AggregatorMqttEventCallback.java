package com.secomind.poc.aggregator.aggregator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AggregatorMqttEventCallback implements MqttCallback {
  private Aggregator aggregator;

  public AggregatorMqttEventCallback(Aggregator aggregator) {
    this.aggregator = aggregator;
  }

  @Override
  public void connectionLost(Throwable cause) {
    System.out.println("connectionLost: " + cause.getMessage());
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    String value = new String(message.getPayload());
    System.out.println("topic: " + topic);
    System.out.println("Qos: " + message.getQos());
    System.out.println("message content: " + value);
    final String[] topicParams = topic.split("/");
    String deviceId, groupId;
    if (topicParams.length == 4) {
      groupId = topicParams[2];
      deviceId = topicParams[3];
      this.aggregator.collectGroup(deviceId, value, groupId);
      System.out.println("Group ID: " + groupId + "\nDevice ID: " + deviceId);
    } else if (topicParams.length == 3) {
      deviceId = topicParams[2];
      this.aggregator.collectSingle(deviceId, value);
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
