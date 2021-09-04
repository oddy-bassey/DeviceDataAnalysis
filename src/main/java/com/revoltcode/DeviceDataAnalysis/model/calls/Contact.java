package com.revoltcode.DeviceDataAnalysis.model.calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {

    @Field(type = FieldType.Keyword)
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String memory;

    private NestedData phone_number;

    private List<NestedData> extra_field;
}

