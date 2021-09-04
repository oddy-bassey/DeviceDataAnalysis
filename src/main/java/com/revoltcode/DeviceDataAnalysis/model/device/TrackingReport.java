package com.revoltcode.DeviceDataAnalysis.model.device;

import com.revoltcode.DeviceDataAnalysis.datacategory.PhoneStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingReport {

    @Field(type = FieldType.Keyword)
    private Integer no;

    @Field(type = FieldType.Text)
    private String alias;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Keyword)
    private String msisdn;

    @Field(type = FieldType.Keyword)
    private String imsi;

    @Field(type = FieldType.Keyword)
    private String imei;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text)
    private String queryDate;

    @Field(type = FieldType.Long)
    private double haLatitude;

    @Field(type = FieldType.Long)
    private double haLongitude;

    @Field(type = FieldType.Long)
    private double latitude;

    @Field(type = FieldType.Long)
    private double longitude;

    @Field(type = FieldType.Keyword)
    private String cellReference;

    @Field(type = FieldType.Keyword)
    private PhoneStatus phoneStatus;

    @Field(type = FieldType.Text)
    private String locationQuality;

    @Field(type = FieldType.Text)
    private String locationAddress;

    @Field(type = FieldType.Integer)
    private Integer haResponseCode;

    @Field(type = FieldType.Text)
    private String responseCode;
}
