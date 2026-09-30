package com.smartattend.biometric;

import com.smartattend.dto.BiometricDtoModels.BiometricEventRequest;
import com.smartattend.dto.BiometricDtoModels.BiometricEventResponse;
import com.smartattend.enums.DeviceType;

public interface BiometricProvider {
    DeviceType getSupportedType();
    BiometricEventResponse processScan(BiometricEventRequest request);
    boolean isHardwareConnected(String deviceCode);
}
