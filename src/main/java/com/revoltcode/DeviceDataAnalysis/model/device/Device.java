package com.revoltcode.DeviceDataAnalysis.model.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.revoltcode.DeviceDataAnalysis.helper.Indices;
import com.revoltcode.DeviceDataAnalysis.model.calls.*;
import com.revoltcode.DeviceDataAnalysis.model.media.DocumentFile;
import com.revoltcode.DeviceDataAnalysis.model.media.ImageFile;
import com.revoltcode.DeviceDataAnalysis.model.message.Sms;
import com.revoltcode.DeviceDataAnalysis.util.DataUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = Indices.DEVICE_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class Device {

        @Field(type = FieldType.Keyword)
        private Integer id;

        @Field(type = FieldType.Text)
        private String report_type;

        @Field(type = FieldType.Text)
        private String selected_manufacture;

        @Field(type = FieldType.Keyword)
        private String selected_model;

        @Field(type = FieldType.Keyword)
        private String detected_manufacture;

        @Field(type = FieldType.Keyword)
        private String detected_model;

        @Field(type = FieldType.Text)
        private String revision;

        @Field(type = FieldType.Keyword)
        private String imei;

        @Field(type = FieldType.Keyword)
        private String iccid;

        @Field(type = FieldType.Keyword)
        private String imsi;

        @Field(type = FieldType.Text)
        private String advertisingid;

        @Field(type = FieldType.Keyword)
        private String connection_type;

        @Field(type = FieldType.Text)
        private String xml_format;

        @Field(type = FieldType.Text)
        private String software;

        @Field(type = FieldType.Text)
        private String serial;

        @Field(type = FieldType.Integer)
        private Integer usingclient;

        @Field(type = FieldType.Nested)
        private List<Contact> contact;

        @Field(type = FieldType.Nested)
        private List<Sms> sms_message;

        @Field(type = FieldType.Nested)
        private List<IncomingCall> incoming_call;

        @Field(type = FieldType.Nested)
        private List<OutgoingCall> outgoing_call;

        @Field(type = FieldType.Nested)
        private List<MissedCall> missed_call;

        @Field(type = FieldType.Nested)
        private List<ImageFile> image;

        @Field(type = FieldType.Nested)
        private List<DocumentFile> documents;

        @Field(type = FieldType.Date)
        private String phone_date_time;

        private String start_date_time;
        private String end_date_time;

        private String db_container_name;
        private Integer db_data_size;
        private Integer db_id;
}
