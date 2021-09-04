package com.revoltcode.DeviceDataAnalysis.model.calls;

import com.revoltcode.DeviceDataAnalysis.datacategory.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutgoingCall {

    @Field(type = FieldType.Keyword)
    private Integer id;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String number;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Date)
    private String timestamp;

    @Field(type = FieldType.Text)
    private String duration;
}
