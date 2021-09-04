package com.revoltcode.DeviceDataAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoLocation {
    private double longitude;
    private double latitude;
}
