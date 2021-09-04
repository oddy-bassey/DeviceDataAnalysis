package com.revoltcode.DeviceDataAnalysis.service;

import com.revoltcode.DeviceDataAnalysis.model.calls.*;
import com.revoltcode.DeviceDataAnalysis.model.device.*;
import com.revoltcode.DeviceDataAnalysis.model.media.*;
import com.revoltcode.DeviceDataAnalysis.model.message.Sms;

import java.io.IOException;
import java.util.List;

public interface DeviceService {

    Device findById(final String id);

    void initializePresetData();

    void generateDeviceDataSet();

    List<Contact> generateDeviceContact();

    List<IncomingCall> generateDeviceIncomingCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts);

    List<OutgoingCall> generateDeviceOutgoingCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts);

    List<MissedCall> generateDeviceMissedCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts);

    List<Sms> generateDeviceMessages(String phoneNumber, List<Contact> contacts);

    List<ImageFile> generateDeviceImages(String manufacturer, String model);

    List<DocumentFile> generateDeviceFiles();

    List<TrackingReport> generateTrackingData(int dataSize, String imsi, String imei);

}
