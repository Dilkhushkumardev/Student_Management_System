package com.smartattend.biometric;

import com.smartattend.dto.BiometricDtoModels.BiometricLiveCounterDto;
import com.smartattend.dto.BiometricDtoModels.LiveStudentFeedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class BiometricEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BiometricEventPublisher.class);

    // Session ID -> List of SSE Emitters
    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> sessionEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long sessionId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 mins timeout
        sessionEmitters.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(sessionId, emitter));
        emitter.onTimeout(() -> removeEmitter(sessionId, emitter));
        emitter.onError((e) -> removeEmitter(sessionId, emitter));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected to SmartAttend Realtime Live Feed"));
        } catch (IOException e) {
            removeEmitter(sessionId, emitter);
        }

        return emitter;
    }

    private void removeEmitter(Long sessionId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> list = sessionEmitters.get(sessionId);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) {
                sessionEmitters.remove(sessionId);
            }
        }
    }

    public void publishLiveCounterUpdate(Long sessionId, BiometricLiveCounterDto counter) {
        CopyOnWriteArrayList<SseEmitter> list = sessionEmitters.get(sessionId);
        if (list != null) {
            for (SseEmitter emitter : list) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("COUNTER_UPDATE")
                            .data(counter));
                } catch (Exception e) {
                    removeEmitter(sessionId, emitter);
                }
            }
        }
    }

    public void publishStudentScan(Long sessionId, LiveStudentFeedDto feedItem) {
        CopyOnWriteArrayList<SseEmitter> list = sessionEmitters.get(sessionId);
        if (list != null) {
            for (SseEmitter emitter : list) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("STUDENT_SCAN")
                            .data(feedItem));
                } catch (Exception e) {
                    removeEmitter(sessionId, emitter);
                }
            }
        }
    }
}
