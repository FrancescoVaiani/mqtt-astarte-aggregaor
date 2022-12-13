package com.secomind.poc.aggregator.aggregator;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.astarteplatform.devicesdk.AstarteDevice;
import org.astarteplatform.devicesdk.generic.AstarteGenericDevice;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
  public static AstarteDevice initAstarte(
      String realm, String pairingUrl, String deviceId, String credentialsSecret) throws Exception {

    JdbcConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:mem:testDb");
    AggregatorInterfaceProvider interfaceProvider = new AggregatorInterfaceProvider();
    AstarteDevice device =
        new AstarteGenericDevice(
            deviceId, realm, credentialsSecret, interfaceProvider, pairingUrl, connectionSource);

    /*
     * Connect listeners
     *
     * See ExampleMessageListener to listen for device connection, disconnection and failure.
     */
    device.setAstarteMessageListener(new AggregatorMessageListener());

    /*
     * Set this if you want to let AstarteDevice take care of the reconnection. The default
     * is false, which means that the application is responsible of reconnecting in case of
     * failures
     */
    device.setAlwaysReconnect(true);

    /*
     * Start the connection
     */
    device.connect();

    /*
     * Wait for device connection
     *
     * This can be handled asynchronously in the Message Listener.
     */
    while (!device.isConnected()) {
      Thread.sleep(100);
    }

    return device;
  }

  public static void main(String[] args) throws Exception {
    // Astarte Initialization
    String realm = "realm";
    String pairingUrl = "pairing_url";
    String deviceId = "device_id";
    String credentialsSecret = "credentials_secret";

    AstarteDevice device = initAstarte(realm, pairingUrl, deviceId, credentialsSecret);

    // MQTT Initialization
    String brokerURI = "tcp://localhost:1883";
    String clientId = "aggregator";
    String[] topics = {"data/group/group_1/+", "data/single/device_1"};

    MqttClient client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
    client.setCallback(new AggregatorMqttEventCallback());
    client.connect();
    for (String topic : topics) {
      client.subscribe(topic, 0);
    }
  }
}
