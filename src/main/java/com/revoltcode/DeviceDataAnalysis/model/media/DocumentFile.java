package com.revoltcode.DeviceDataAnalysis.model.media;

import com.revoltcode.DeviceDataAnalysis.helper.Indices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentFile {

    @Field(type = FieldType.Keyword)
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String stored_name;

    @Field(type = FieldType.Text)
    private String path;

    @Field(type = FieldType.Text)
    private String memory;

    @Field(type = FieldType.Integer)
    private Integer size;

    @Field(type = FieldType.Date)
    private String date_time;

    @Field(type = FieldType.Date)
    private String date_time_modified;
}
