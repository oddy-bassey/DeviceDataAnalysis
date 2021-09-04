package com.revoltcode.DeviceDataAnalysis.model.calls;

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
public class NestedData {

    @Field(type = FieldType.Text)
    private String designation;

    @Field(type = FieldType.Text)
    private String value;
}
