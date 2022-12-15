package com.secomind.poc.aggregator.aggregator;

import java.util.*;
import org.astarteplatform.devicesdk.AstarteDevice;
import org.astarteplatform.devicesdk.protocol.AstarteDeviceAggregateDatastreamInterface;
import org.astarteplatform.devicesdk.protocol.AstarteDeviceDatastreamInterface;
import org.astarteplatform.devicesdk.protocol.AstarteInterfaceMappingNotFoundException;
import org.astarteplatform.devicesdk.protocol.AstarteInvalidValueException;
import org.astarteplatform.devicesdk.transport.AstarteTransportException;
import org.joda.time.DateTime;

public class Aggregator {
  private static Aggregator instance = null;

  private Map<String, Set<String>> groups;
  private Map<String, Report> reports;
  private AstarteDevice device;

  private static final String singleScanInterfaceName = "com.secomind.SingleScan";
  private static final String groupScanInterfaceName = "com.secomind.GroupScan";
  private static final String singleReportInterfaceName = "com.secomind.SingleReport";

  private Aggregator() {
    this.groups = Collections.synchronizedMap(new HashMap<>());
    this.reports = Collections.synchronizedMap(new HashMap<>());
  }

  public static Aggregator getInstance() {
    if (instance == null) {
      instance = new Aggregator();
    }
    return instance;
  }

  public void setAstarteDevice(AstarteDevice device) {
    this.device = device;
  }

  public void collectGroup(String deviceId, String value, String groupId) {
    if (!groups.containsKey(groupId)) {
      groups.put(groupId, new HashSet<>());
    }
    if (!value.equals("Error")) {
      groups.get(groupId).add(value);
    }

    addToReports(deviceId, value);
  }

  public void collectSingle(String deviceId, String value) {
    AstarteDeviceDatastreamInterface singleScanInterface =
        (AstarteDeviceDatastreamInterface) device.getInterface(singleScanInterfaceName);
    try {
      singleScanInterface.streamData("/" + deviceId + "/value", value, DateTime.now());
      System.out.println(">>>" + singleScanInterfaceName + "/" + deviceId + "/value : " + value);
    } catch (AstarteTransportException
        | AstarteInterfaceMappingNotFoundException
        | AstarteInvalidValueException e) {
      e.printStackTrace();
    }
    addToReports(deviceId, value);
  }

  private void addToReports(String deviceId, String value) {
    if (!reports.containsKey(deviceId)) {
      reports.put(deviceId, new Report());
    }

    final Report report = reports.get(deviceId);
    if (value.equals("Error")) {
      report.incrementError();
    } else {
      report.incrementSuccess();
    }
  }

  public void sendGroups() {
    for (String groupId : groups.keySet()) {
      String[] values = groups.get(groupId).toArray(new String[0]);
      AstarteDeviceDatastreamInterface groupScanInterface =
          (AstarteDeviceDatastreamInterface) device.getInterface(groupScanInterfaceName);
      try {
        groupScanInterface.streamData("/" + groupId + "/data", values, DateTime.now());
        System.out.println(
            ">>>" + groupScanInterfaceName + "/" + groupId + "/data : " + values.toString());
        groups.remove(groupId);
      } catch (AstarteTransportException
          | AstarteInterfaceMappingNotFoundException
          | AstarteInvalidValueException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendReports() {
    for (String deviceId : reports.keySet()) {
      Report report = reports.get(deviceId);
      Map<String, Object> values = new HashMap<>();
      values.put("SuccessScans", report.getSuccess());
      values.put("ErrorScans", report.getErrors());

      AstarteDeviceAggregateDatastreamInterface singleReportInterface =
          (AstarteDeviceAggregateDatastreamInterface)
              device.getInterface(singleReportInterfaceName);
      try {
        singleReportInterface.streamData("/" + deviceId, values, DateTime.now());
        System.out.println(">>>" + singleReportInterfaceName + "/" + deviceId + "/ : " + values);
        reports.remove(deviceId);
      } catch (AstarteTransportException
          | AstarteInterfaceMappingNotFoundException
          | AstarteInvalidValueException e) {
        e.printStackTrace();
      }
    }
  }
}
