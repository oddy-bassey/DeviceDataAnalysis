package com.revoltcode.DeviceDataAnalysis.repository;

import com.revoltcode.DeviceDataAnalysis.model.device.Device;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DeviceRepository extends ElasticsearchRepository<Device, String> {

}
