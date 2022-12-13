package com.secomind.poc.aggregator.aggregator;

import java.util.ArrayList;
import java.util.List;

public class AggregatorConfig {
  private String astarteRealm;
  private String astartePairingUrl;
  private String astarteDeviceId;
  private String astarteCredentialsSecret;
  private String mqttBrokerUri;
  private String mqttClientId;
  private List<String> mqttTopicList = new ArrayList<>();

  public String getAstarteRealm() {
    return astarteRealm;
  }

  public void setAstarteRealm(String astarteRealm) {
    this.astarteRealm = astarteRealm;
  }

  public String getAstartePairingUrl() {
    return astartePairingUrl;
  }

  public void setAstartePairingUrl(String astartePairingUrl) {
    this.astartePairingUrl = astartePairingUrl;
  }

  public String getAstarteDeviceId() {
    return astarteDeviceId;
  }

  public void setAstarteDeviceId(String astarteDeviceId) {
    this.astarteDeviceId = astarteDeviceId;
  }

  public String getAstarteCredentialsSecret() {
    return astarteCredentialsSecret;
  }

  public void setAstarteCredentialsSecret(String astarteCredentialsSecret) {
    this.astarteCredentialsSecret = astarteCredentialsSecret;
  }

  public String getMqttBrokerUri() {
    return mqttBrokerUri;
  }

  public void setMqttBrokerUri(String mqttBrokerUri) {
    this.mqttBrokerUri = mqttBrokerUri;
  }

  public String getMqttClientId() {
    return mqttClientId;
  }

  public void setMqttClientId(String mqttClientId) {
    this.mqttClientId = mqttClientId;
  }

  public List<String> getMqttTopicList() {
    return mqttTopicList;
  }

  public void setMqttTopicList(List<String> mqttTopicList) {
    this.mqttTopicList = mqttTopicList;
  }

  @Override
  public String toString() {
    return "AggregatorConfig = [astarteRealm="
        + astarteRealm
        + ", astartePairingUrl="
        + astartePairingUrl
        + ", astarteDeviceId="
        + astarteDeviceId
        + ", astarteCredentialsSecret="
        + astarteCredentialsSecret
        + ", mqttBrokerUri="
        + mqttBrokerUri
        + ", mqttClientId="
        + mqttClientId
        + ", mqttTopicList="
        + mqttTopicList
        + "]";
  }
}
