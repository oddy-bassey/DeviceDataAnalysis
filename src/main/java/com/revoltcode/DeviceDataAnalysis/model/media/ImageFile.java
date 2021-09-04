package com.revoltcode.DeviceDataAnalysis.model.media;

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
public class ImageFile {

      @Field(type = FieldType.Keyword)
      private Integer id;

      @Field(type = FieldType.Text)
      private String name;

      @Field(type = FieldType.Text)
      private String stored_name;

      @Field(type = FieldType.Text)
      private String thumb_location;

      @Field(type = FieldType.Text)
      private String path;

      @Field(type = FieldType.Text)
      private String memory;

      @Field(type = FieldType.Integer)
      private Integer size;

      @Field(type = FieldType.Text)
      private String resolution;

      @Field(type = FieldType.Text)
      private String pixelResolution;

      @Field(type = FieldType.Keyword)
      private String cameraMake;

      @Field(type = FieldType.Keyword)
      private String cameraModel;

      @Field(type = FieldType.Date)
      private String exifDateTime;

      @Field(type = FieldType.Date)
      private String date_time;

      @Field(type = FieldType.Date)
      private String date_time_modified;
}
