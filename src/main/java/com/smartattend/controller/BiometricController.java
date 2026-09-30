package com.smartattend.controller;

import com.smartattend.biometric.BiometricEventPublisher;
import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.BiometricDtoModels.*;
import com.smartattend.service.BiometricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/biometric")
@Tag(name = "Biometric Gateway", description = "Endpoints for physical/simulated biometric devices, live attendance counter, and real-time streaming")
public class BiometricController {

    private final BiometricService biometricService;
    private final BiometricEventPublisher eventPublisher;

    public BiometricController(BiometricService biometricService, BiometricEventPublisher eventPublisher) {
        this.biometricService = biometricService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/events")
    @Operation(summary = "Idempotent event ingestion endpoint for biometric devices")
    public ResponseEntity<ApiResponse<BiometricEventResponse>> receiveBiometricEvent(@Valid @RequestBody BiometricEventRequest request) {
        BiometricEventResponse response = biometricService.processBiometricEvent(request);
        return ResponseEntity.ok(ApiResponse.ok(response.getMessage(), response));
    }

    @PostMapping("/simulate")
    @Operation(summary = "Demo simulation endpoint for faculty & test workflows (Fingerprint/Face/RFID)")
    public ResponseEntity<ApiResponse<BiometricLiveCounterDto>> simulateScan(@Valid @RequestBody BiometricSimulationRequest request) {
        BiometricLiveCounterDto counter = biometricService.simulateScan(request);
        return ResponseEntity.ok(ApiResponse.ok("Biometric simulation executed", counter));
    }

    @GetMapping("/live-counter/{sessionId}")
    @Operation(summary = "Get live biometric attendance counter and student feed for a session")
    public ResponseEntity<ApiResponse<BiometricLiveCounterDto>> getLiveCounter(@PathVariable Long sessionId) {
        return ResponseEntity.ok(ApiResponse.ok(biometricService.getLiveCounter(sessionId)));
    }

    @GetMapping(value = "/stream/{sessionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Server-Sent Events (SSE) real-time stream for live biometric counter screen")
    public SseEmitter streamSessionEvents(@PathVariable Long sessionId) {
        return eventPublisher.subscribe(sessionId);
    }

    @GetMapping("/devices")
    @Operation(summary = "List all registered biometric devices and their online status")
    public ResponseEntity<ApiResponse<List<BiometricDeviceDto>>> getAllDevices() {
        return ResponseEntity.ok(ApiResponse.ok(biometricService.getAllDevices()));
    }

    @PostMapping("/devices")
    @Operation(summary = "Register a new biometric terminal device")
    public ResponseEntity<ApiResponse<BiometricDeviceDto>> createDevice(@Valid @RequestBody BiometricDeviceCreateRequest request) {
        BiometricDeviceDto created = biometricService.createDevice(request);
        return ResponseEntity.ok(ApiResponse.created("Biometric device registered", created));
    }

    @PostMapping("/devices/{id}/heartbeat")
    @Operation(summary = "Record heartbeat signal from physical device")
    public ResponseEntity<ApiResponse<String>> recordHeartbeat(@PathVariable Long id, @RequestParam(required = false) String deviceCode) {
        biometricService.recordHeartbeat(deviceCode != null ? deviceCode : "BIO-CSE-001");
        return ResponseEntity.ok(ApiResponse.ok("Heartbeat recorded", "Device status: ONLINE"));
    }
}
