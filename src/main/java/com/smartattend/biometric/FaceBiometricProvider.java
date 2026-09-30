package com.smartattend.biometric;

import com.smartattend.dto.BiometricDtoModels.BiometricEventRequest;
import com.smartattend.dto.BiometricDtoModels.BiometricEventResponse;
import com.smartattend.enums.BiometricResult;
import com.smartattend.enums.DeviceType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FaceBiometricProvider implements BiometricProvider {

    @Override
    public DeviceType getSupportedType() {
        return DeviceType.FACE;
    }

    @Override
    public BiometricEventResponse processScan(BiometricEventRequest request) {
        return BiometricEventResponse.builder()
                .success(true)
                .result(BiometricResult.SUCCESS)
                .message("Facial recognition verification successful")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean isHardwareConnected(String deviceCode) {
        return true;
    }
}
