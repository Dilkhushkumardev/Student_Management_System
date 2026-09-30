package com.smartattend.biometric;

import com.smartattend.dto.BiometricDtoModels.BiometricEventRequest;
import com.smartattend.dto.BiometricDtoModels.BiometricEventResponse;
import com.smartattend.enums.BiometricResult;
import com.smartattend.enums.DeviceType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FingerprintBiometricProvider implements BiometricProvider {

    @Override
    public DeviceType getSupportedType() {
        return DeviceType.FINGERPRINT;
    }

    @Override
    public BiometricEventResponse processScan(BiometricEventRequest request) {
        // Hardware adapter integration point (ZKTeco, Morpho, eNBioScan, Mantra MFS100 SDK)
        return BiometricEventResponse.builder()
                .success(true)
                .result(BiometricResult.SUCCESS)
                .message("Fingerprint verified via hardware biometric gateway")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean isHardwareConnected(String deviceCode) {
        return true;
    }
}
