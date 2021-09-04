package com.revoltcode.DeviceDataAnalysis.model.message;

import com.revoltcode.DeviceDataAnalysis.datacategory.MsgStatus;
import com.revoltcode.DeviceDataAnalysis.datacategory.MsgType;
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
public class Sms {

   @Field(type = FieldType.Keyword)
   private Integer id;

   @Field(type = FieldType.Keyword)
   private String number;

   @Field(type = FieldType.Text)
   private String name;

   @Field(type = FieldType.Keyword)
   private String status;

   private String timestamp;

   @Field(type = FieldType.Text)
   private String folder;

   @Field(type = FieldType.Text)
   private String storage;

   @Field(type = FieldType.Keyword)
   private String type;

   @Field(type = FieldType.Text)
   private String text;

   @Field(type = FieldType.Text)
   private String smsc;
}
