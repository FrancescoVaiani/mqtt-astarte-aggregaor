package com.secomind.poc.aggregator.aggregator;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import org.astarteplatform.devicesdk.AstarteDevice;
import org.astarteplatform.devicesdk.generic.AstarteGenericDevice;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
     * is false, which means that the application is responsible for reconnecting in case of
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
    // Load configs
    Path configPath = Paths.get("./config/aggregator/config.yml");
    Constructor constructor = new Constructor(AggregatorConfig.class);
    Yaml yaml = new Yaml(constructor);
    AggregatorConfig configs = yaml.load(new FileReader(configPath.toFile()));

    // Astarte Initialization
    String realm = configs.getAstarteRealm();
    String pairingUrl = configs.getAstartePairingUrl();
    String deviceId = configs.getAstarteDeviceId();
    String credentialsSecret = configs.getAstarteCredentialsSecret();

    AstarteDevice device = initAstarte(realm, pairingUrl, deviceId, credentialsSecret);
    Aggregator aggregator = Aggregator.getInstance();
    aggregator.setAstarteDevice(device);

    // MQTT Initialization
    String brokerURI = configs.getMqttBrokerUri();
    String clientId = configs.getMqttClientId();
    String[] topics = configs.getMqttTopicList().toArray(new String[0]);

    MqttClient client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
    client.setCallback(new AggregatorMqttEventCallback(aggregator));
    client.connect();
    for (String topic : topics) {
      client.subscribe(topic, 0);
    }

    Timer reportTimer = new Timer();
    ScheduledReportTask reportTask = new ScheduledReportTask();
    reportTimer.schedule(reportTask, 0, configs.getReportSendRepetitionInSeconds() * 1000);

    Timer groupTimer = new Timer();
    ScheduledGroupTask groupTask = new ScheduledGroupTask();
    groupTimer.schedule(groupTask, 0, configs.getGroupSendRepetitionPeriod() * 1000);
  }
}
