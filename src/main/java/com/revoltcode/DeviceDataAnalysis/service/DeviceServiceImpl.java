package com.revoltcode.DeviceDataAnalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revoltcode.DeviceDataAnalysis.datacategory.*;
import com.revoltcode.DeviceDataAnalysis.model.GeoLocation;
import com.revoltcode.DeviceDataAnalysis.model.calls.*;
import com.revoltcode.DeviceDataAnalysis.model.device.*;
import com.revoltcode.DeviceDataAnalysis.model.media.*;
import com.revoltcode.DeviceDataAnalysis.model.message.Sms;
import com.revoltcode.DeviceDataAnalysis.repository.DeviceRepository;
import com.revoltcode.DeviceDataAnalysis.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;

    private List<Phone> phones = new ArrayList<>();
    private List<Device> devices = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();

    private List<IncomingCall> incomingCalls = new ArrayList<>();
    private List<OutgoingCall> outgoingCalls = new ArrayList<>();
    private List<MissedCall> missedCalls = new ArrayList<>();

    private List<Sms> messages = new ArrayList<>();
    private List<ImageFile> images = new ArrayList<>();
    private List<DocumentFile> files = new ArrayList<>();

    private static List<String> sampleSms = new ArrayList<>();

    private static List<String> imageInternalStorage = new ArrayList<>();
    private static List<String> fileInternalStorage = new ArrayList<>();

    private static Map<Integer, Device> createdUserDevice = new HashMap<>();
    private static Map<String, String> IMEI = new HashMap<>();
    private static Map<String, String> ICCID = new HashMap<>();
    private static Map<String, String> IMSI = new HashMap<>();
    private static Map<String, String> deviceContacts = new HashMap<>();
    private static Map<String, String> deviceOwnerNumbers = new HashMap<>();

    private static Logger log = Logger.getLogger(DeviceServiceImpl.class.getName());

    private ObjectMapper objectMapper = new ObjectMapper();
    private  Random random = new Random();
    private final int minLat = 5, minLong =5, maxLat = 10, maxLong = 10;

    private int deviceCount = 1;

    private FileWriter deviceDataFileWriter, trackingDataFileWriter;
    private BufferedWriter deviceBufferWriter, trackingBufferWriter;

    @Value("${input.folder}")
    private String inputLocation;

    //@PostConstruct
    @Override
    public void initializePresetData(){
        phones.clear();
        sampleSms.clear();
        imageInternalStorage.clear();
        fileInternalStorage.clear();

        phones.add(Phone.builder().brand("TECNO").model("TECNO TECNO CF7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO PHANTOM A7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO PHANTOM A5").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO LITE7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO P3").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG GALAXY S11").build());
        phones.add(Phone.builder().brand("VIVO").model("VIVO X21").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG GALAXY S8").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG NOTE 10 PLUS").build());
        phones.add(Phone.builder().brand("IPHONE").model("IPHONE 8 PLUS").build());
        phones.add(Phone.builder().brand("IPHONE").model("IPHONE XS").build());
        phones.add(Phone.builder().brand("NOKIA").model("NOKIA LUMIA 255").build());
        phones.add(Phone.builder().brand("LG").model("LG G6").build());
        phones.add(Phone.builder().brand("GOOGLE PIXEL").model("GOOGLE PIXEL 4XL").build());
        phones.add(Phone.builder().brand("MOTOROLA").model("MOTOROLA G5S PLUS").build());
        phones.add(Phone.builder().brand("NOKIA").model("NOKIA 8").build());
        phones.add(Phone.builder().brand("BLACKBERRY").model("KEYONE").build());
        phones.add(Phone.builder().brand("LENOVO").model("LENOVO K8").build());
        phones.add(Phone.builder().brand("SONY").model("XPERIA XZS").build());
        phones.add(Phone.builder().brand("HTC").model("HTC ONE X10").build());
        phones.add(Phone.builder().brand("HUAWEI").model("HUAWEI P10").build());

        sampleSms.add("Hey, thanks for shopping with Jumia! We’ve got tons of exciting deals in our upcoming Collection. ");
        sampleSms.add("Stay tuned or visit www.cstore.com to learn more about commercial trading");
        sampleSms.add("Hello, thanks for subscribing to USWines.");
        sampleSms.add("We’re eager to serve you better, pls reply to this msg now with R for Red Wine, W for White, P for Rose Wine.");
        sampleSms.add("Hi, your fav messaging partner now lets you instantly engage with social media leads! Tap http://bit.ly/2nOFcRS to get started.");
        sampleSms.add("Hey Brandon, we’ve stocked our shelves with assorted mint cookie treats for you.");
        sampleSms.add("Hey bro have you acquired the arsenal?");
        sampleSms.add("Prepare for the operation, we bomb the capital tomorrow");
        sampleSms.add("I will kill you if you told anyone");
        sampleSms.add("Hi, just wanted to say hi");
        sampleSms.add("My brother and sister are coming home now");
        sampleSms.add("I'll be travelling to america tommoow");
        sampleSms.add("when will you come to america");
        sampleSms.add("what time is it now?");
        sampleSms.add("its about 2:00am");
        sampleSms.add("make sure the nuclear ewapon is in place");
        sampleSms.add("All units were killed");
        sampleSms.add("Elope with the kids before he finds out");
        sampleSms.add("i'll be sending money tomorrow");
        sampleSms.add("Hi, i've missed you, where are you?");

        imageInternalStorage.add("Internal shared storage/Pictures/Screenshot/screenshot");
        imageInternalStorage.add("Internal shared storage/DCIM/Camera/");
        imageInternalStorage.add("Internal shared storage/Xender/Camera/");
        fileInternalStorage.add("Internal shared storage/mtklog/mobilelog/");
        fileInternalStorage.add("Internal shared storage/");
        fileInternalStorage.add("Internal shared storage/mtklog/gpsdbglog/");
        fileInternalStorage.add("Internal shared storage/Android/data/");

        log.info("Data Initialised!");
    }

    @Override
    public void generateDeviceDataSet() {

        try {
            deviceDataFileWriter = new FileWriter(inputLocation+"GenerateDeviceData.json", true);
            deviceBufferWriter = new BufferedWriter(deviceDataFileWriter);

            trackingDataFileWriter  = new FileWriter(inputLocation+"GeneratedTrackingData.json", true);
            trackingBufferWriter = new BufferedWriter(trackingDataFileWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int month = random.nextInt(8)+1;
            int day = random.nextInt(8)+1;
            int minuite = random.nextInt(8)+1;

            Device device = null;
            Phone phone = phones.get(random.nextInt(21));
            String imei = DataUtil.generateIMEI();

            while (IMEI.get(imei) != null) {
                imei = DataUtil.generateIMEI();
            }
            IMEI.put(imei, imei);

            String iccid = DataUtil.generateNumberRef(19);
            while (ICCID.get(iccid) != null) {
                iccid = DataUtil.generateNumberRef(19);
            }
            ICCID.put(iccid, iccid);

            String imsi = DataUtil.generateNumberRef(19);
            while (IMSI.get(imsi) != null) {
                imsi = DataUtil.generateNumberRef(19);
            }
            IMSI.put(imsi, imsi);

            String phoneNumber = "";
            while (deviceOwnerNumbers.get(phoneNumber = DataUtil.generatePhoneNumber()) != null) {

            }
            deviceOwnerNumbers.put(phoneNumber, phoneNumber);

            device = Device.builder()
                    .id(deviceCount)
                    .report_type("cell")
                    .selected_manufacture(phone.getBrand())
                    .selected_model(phone.getModel())
                    .detected_manufacture(phone.getBrand())
                    .detected_model(phone.getBrand())
                    .revision("8." + random.nextInt() + "." + random.nextInt() + " " + DataUtil.generateNumberRef(6) + " BCDEFH-" + DataUtil.generateNumberRef(6) + "V237")
                    .imei(imei)
                    .iccid(iccid)
                    .imsi(imsi)
                    .advertisingid(DataUtil.generateNumberRef(6) + "ef-e" + DataUtil.generateNumberRef(3) + "-" + DataUtil.generateNumberRef(4) + "-" + DataUtil.generateNumberRef(3) + "c-" + DataUtil.generateNumberRef(4) + "cd9e2acf")
                    .start_date_time("2021-0"+month+"-1"+day+"T12:3"+minuite+":24+01:00")
                    .phone_date_time("2021-0"+month+"-1"+day+"T12:2"+minuite+":24+01:00")
                    .end_date_time("2021-0"+month+"-1"+day+"T12:4"+minuite+":24+01:00")
                    .phone_date_time(DataUtil.getDate())
                    .connection_type("USB Cable")
                    .xml_format("1." + random.nextInt(9) + "." + random.nextInt(9) + "." + random.nextInt(9))
                    .software("7." + random.nextInt(45) + "." + random.nextInt(9) + "." + random.nextInt(90) + " UFED")
                    .serial(DataUtil.generateNumberRef(7))
                    .usingclient(random.nextInt(4) + 1)
                    .contact(generateDeviceContact())
                    .db_id(deviceCount)
                    .db_data_size(Integer.valueOf(DataUtil.generateNumberRef(random.nextInt(12 + 3))))
                    .documents(generateDeviceFiles())
                    .build();

            device.setIncoming_call(generateDeviceIncomingCalls(phoneNumber, imsi, imei, device.getContact()));
            device.setOutgoing_call(generateDeviceOutgoingCalls(phoneNumber, imsi, imei, device.getContact()));
            device.setMissed_call(generateDeviceMissedCalls(phoneNumber, imsi, imei, device.getContact()));
            device.setDb_container_name(device.getDetected_manufacture()+" DB");

            device.setSms_message(generateDeviceMessages(phoneNumber, device.getContact()));
            device.setImage(generateDeviceImages(device.getSelected_manufacture(), device.getSelected_model()));
            devices.add(device);

            deviceCount += 1;

            //write data to folder
            String deviceJson = objectMapper.writeValueAsString(device);
            String trackingJson = objectMapper.writeValueAsString(generateTrackingData(deviceCount, imsi, imei));

            deviceBufferWriter.write(deviceJson);
            trackingBufferWriter.write(trackingJson);

            deviceBufferWriter.newLine();
            trackingBufferWriter.newLine();

            deviceBufferWriter.close();
            trackingBufferWriter.close();

            log.info("************** created device: "+deviceCount);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        //createdUserDevice.put(deviceCount, device);
        //deviceRepository.save(device);
    }

    @Override
    public List<Contact> generateDeviceContact(){
        contacts.clear();
        int x = 1;

        int bound = random.nextInt(100);

        while(contacts.size() < bound+1){

            String phoneNumber = "";
            while(deviceOwnerNumbers.get(phoneNumber = DataUtil.generatePhoneNumber()) != null){

            }

            deviceContacts.put(phoneNumber, phoneNumber);

            String designation = null;
            switch (random.nextInt(3)){
                case 0:
                    designation = "General";
                    break;

                case 1:
                    designation = "Home";
                    break;

                case 2:
                    designation = "Mobile";
                    break;

                case 3:
                    designation = "Office";
                    break;

                default:
            }

            if(devices.size()>0 && random.nextInt(100) % 2 == 0){
                Device device = devices.get(random.nextInt(devices.size()));

                if(device.getContact().size()>1) {
                    contacts.add(device.getContact().get(device.getContact().size() - 1));
                }else{
                    Contact data = Contact.builder()
                            .id(x)
                            .name("firstname" + x + " surname" + x)
                            .memory("Phone")
                            .build();

                    data.setPhone_number(NestedData.builder()
                            .designation(designation)
                            .value(phoneNumber)
                            .build());

                    List<NestedData> nestedDataList = new ArrayList<>();
                    nestedDataList.add(NestedData.builder()
                            .designation("Email")
                            .value("firstname" + x + "surname" + x + "@mail.com")
                            .build());

                    nestedDataList.add(NestedData.builder()
                            .designation("Company")
                            .value((random.nextInt(10)%2 == 0)? "Student" : "Civil Servant")
                            .build());

                    data.setExtra_field(nestedDataList);
                    contacts.add(data);
                }
            }else {
                Contact data = Contact.builder()
                        .id(x)
                        .name("firstname" + x + " surname" + x)
                        .memory("Phone")
                        .build();

                data.setPhone_number(NestedData.builder()
                        .designation(designation)
                        .value(phoneNumber)
                        .build());

                List<NestedData> nestedDataList = new ArrayList<>();
                nestedDataList.add(NestedData.builder()
                        .designation("Email")
                        .value("firstname" + x + "surname" + x + "@mail.com")
                        .build());

                nestedDataList.add(NestedData.builder()
                        .designation("Company")
                        .value((random.nextInt(10)%2 == 0)? "Health" : "Finance")
                        .build());

                data.setExtra_field(nestedDataList);
                contacts.add(data);
            }
            x++;
        }
        return contacts;
    }

    @Override
    public List<IncomingCall> generateDeviceIncomingCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts){
        incomingCalls.clear();

        int x = 1;
        int bound = random.nextInt(100)+1;

        while(incomingCalls.size() < bound+1) {
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            Contact contact = contacts.get(random.nextInt(contacts.size()));

            IncomingCall call = IncomingCall.builder()
                    .id(x)
                    .type(CallType.Incoming.name())
                    .number(contact.getPhone_number().getValue())
                    .name(contact.getName())
                    .timestamp(DataUtil.getDate())
                    .duration("00:"+minuites+":"+seconds)
                    .build();

            incomingCalls.add(call);

            x++;
        }

        return incomingCalls;
    }

    @Override
    public List<OutgoingCall> generateDeviceOutgoingCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts){
        outgoingCalls.clear();

        int x = 1;
        int bound = random.nextInt(100)+1;

        while(outgoingCalls.size() < bound+1) {
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            Contact contact = contacts.get(random.nextInt(contacts.size()));

            OutgoingCall outgoingCall = OutgoingCall.builder()
                    .id(x)
                    .type(CallType.Outgoing.name())
                    .number(contact.getPhone_number().getValue())
                    .name(contact.getName())
                    .timestamp(DataUtil.getDate())
                    .duration("00:"+minuites+":"+seconds)
                    .build();

            outgoingCalls.add(outgoingCall);

            x++;
        }

        return outgoingCalls;
    }

    @Override
    public List<MissedCall> generateDeviceMissedCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts){
        missedCalls.clear();

        int x = 1;
        int bound = random.nextInt(100)+1;

        while(missedCalls.size() < bound+1) {
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            Contact contact = contacts.get(random.nextInt(contacts.size()));

            MissedCall missedCall = MissedCall.builder()
                    .id(x)
                    .type(CallType.Missed.name())
                    .number(contact.getPhone_number().getValue())
                    .name(contact.getName())
                    .timestamp(DataUtil.getDate())
                    .duration("00:"+minuites+":"+seconds)
                    .build();

            missedCalls.add(missedCall);

            x++;
        }

        return missedCalls;
    }

    @Override
    public List<Sms> generateDeviceMessages(String phoneNumber, List<Contact> contacts){
        messages.clear();
        int x = 1;
        int bound = random.nextInt(200)+1;

        while(messages.size() < bound+1){
            MsgStatus msgStatus = null;
            MsgType msgType = null;

            int month = random.nextInt(8)+1;
            int day = random.nextInt(8)+1;
            int minuite = random.nextInt(8)+1;

            switch (random.nextInt(2)){
                case 0:
                    msgStatus = MsgStatus.Read;
                    break;

                case 1:
                    msgStatus = MsgStatus.Unread;
                    break;
                default:
            }
            switch (random.nextInt(2)){
                case 0:
                    msgType = MsgType.Outgoing;
                    break;

                case 1:
                    msgType = MsgType.Incoming;
                    break;
                default:
            }

            Contact contact = contacts.get(random.nextInt(contacts.size()));

            messages.add(Sms.builder()
                    .id(x)
                    .number(contact.getPhone_number().getValue())
                    .name(contact.getName())
                    .timestamp("2019-0"+month+"-0"+day+"T19:3"+minuite+":06+01:00")
                    .status(msgStatus.name())
                    .folder("Inbox")
                    .storage("Phone")
                    .type(msgType.name())
                    .text(sampleSms.get(random.nextInt(20)))
                    .smsc(phoneNumber)
                    .build());

            x++;
        }
        return messages;
    }

    @Override
    public List<ImageFile> generateDeviceImages(String manufacturer, String model){
        images.clear();
        int x =1;
        int bound = random.nextInt(100)+1;

        while(images.size() < bound){
            int month = random.nextInt(12)+1;
            int day = random.nextInt(29);

            int month1 = random.nextInt(8)+1;
            int day1 = random.nextInt(8)+1;
            int minuite = random.nextInt(8)+1;

            int height = random.nextInt(100);
            images.add(ImageFile.builder()
                    .id(x)
                    .name("screenshot_2021"+month+""+day+"_"+x+"png")
                    .stored_name("screenshot_2021"+month+""+day+"_"+x+"png")
                    .thumb_location("Images/screenshot_2021"+month+""+day+"_"+x+"png")
                    .path(imageInternalStorage.get(random.nextInt(3)))
                    .memory("Phone")
                    .size(Integer.valueOf(DataUtil.generateNumberRef(4)))
                    .resolution(height + 10 + " X " + height)
                    .pixelResolution(height + 10 + " X " + height)
                    .cameraMake(manufacturer)
                    .cameraModel(model)
                    .date_time("2019-0"+month1+"-0"+day1+"T15:0"+minuite+":10")
                    .date_time_modified("2019-0"+month1+"-0"+day1+"T15:0"+minuite+":10")
                    .exifDateTime(DataUtil.getDate())
                    .build());
            x++;
        }

        return  images;
    }

    @Override
    public List<DocumentFile> generateDeviceFiles(){
        files.clear();
        int x = 1;
        int bound = random.nextInt(100)+1;

        while (files.size() < bound) {

            int month = random.nextInt(8)+1;
            int day = random.nextInt(8)+1;
            int minuite = random.nextInt(8)+1;
            files.add(DocumentFile.builder()
                    .id(x)
                    .name("document"+1)
                    .stored_name("000~file"+x+".txt")
                    .path(fileInternalStorage.get(random.nextInt(4)))
                    .memory(((random.nextInt(2) == 0)? "Phone" : "SD card"))
                    .size(Integer.valueOf(DataUtil.generateNumberRef(4)))
                    .date_time("2020-0"+month+"-0"+day+"T13:0"+minuite+":01")
                    .date_time_modified("2020-0"+month+"-0"+day+"T13:0"+minuite+":01")
                    .build());

            x++;
        }
        return files;
    }

    @Override
    public List<TrackingReport> generateTrackingData(int dataSize, String imsi, String imei) {

        List<TrackingReport> trackingReportData = new ArrayList<>();
        try {
            final var IMEI = DataUtil.generateIMEI();
            PhoneStatus phoneStatus = null;
            Integer haResponseCode = null;

            for(int i=0; i< random.nextInt(30)+1; i++) {

                int month = random.nextInt(8)+1;
                int day = random.nextInt(8)+1;
                int minuite = random.nextInt(8)+1;

                GeoLocation geoLoc1 = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);
                GeoLocation geoLoc2 = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);

                switch (random.nextInt(2)) {
                    case 0:
                        phoneStatus = PhoneStatus.ONLINE;
                        haResponseCode = 200;
                        break;
                    case 1:
                        phoneStatus = PhoneStatus.OFFLINE;
                        haResponseCode = 404;
                        break;
                    default:
                }

                trackingReportData.add(TrackingReport.builder()
                        .no(1)
                        .alias("Test_sim")
                        .username("USER" + dataSize)
                        .msisdn(DataUtil.generateNumberRef(11))
                        .imsi(imsi)
                        .imei(imei)
                        .type("MSISDN")
                        .queryDate("1"+day+"/0"+month+"/2021 0"+minuite+":22")
                        .haLatitude(geoLoc1.getLatitude())
                        .haLongitude(geoLoc1.getLongitude())
                        .latitude(geoLoc2.getLatitude())
                        .longitude(geoLoc2.getLongitude())
                        .cellReference(DataUtil.generateNumberRef(9))
                        .phoneStatus(phoneStatus)
                        .locationQuality("UNKNOWN")
                        .locationAddress("NIGERIA")
                        .haResponseCode(haResponseCode)
                        .responseCode("MTN/ATI:27+0+0+0")
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackingReportData;
    }

    @Override
    public Device findById(final String id){
        return deviceRepository.findById(id).orElse(null);
    }
}
