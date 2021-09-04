package com.revoltcode.DeviceDataAnalysis.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phone {
    private String brand;
    private String model;
}
