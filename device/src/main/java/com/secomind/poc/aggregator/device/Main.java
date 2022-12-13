package com.secomind.poc.aggregator.device;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
  public static void main(String[] args) {
    Options options = new Options();

    // -u MQTT URI
    Option brokerUriOption = new Option("u", "uri", true, "The MQTT URI");
    brokerUriOption.setRequired(true);
    options.addOption(brokerUriOption);

    // -d Device Id
    Option deviceIdOption = new Option("d", "deviceid", true, "The Device ID");
    deviceIdOption.setRequired(true);
    options.addOption(deviceIdOption);

    // -g Group Id
    Option groupIdOption = new Option("g", "groupid", true, "The Group ID");
    options.addOption(groupIdOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("Astarte SDK Example", options);

      System.exit(1);
    }

    String brokerURI = cmd.getOptionValue("uri");
    String deviceId = cmd.getOptionValue("deviceid");
    String groupId = cmd.getOptionValue("groupid");

    String topic = "data/";
    if (groupId == null) {
      topic += "single/";
    } else {
      topic += "group/" + groupId + "/";
    }
    topic += deviceId;

    Random rand = new Random();

    try {
      MqttClient client = new MqttClient(brokerURI, deviceId, new MemoryPersistence());
      client.connect();
      while (true) {
        String content;
        int value = rand.nextInt(100);
        if (value == 0) {
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
