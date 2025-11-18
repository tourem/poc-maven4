package com.larbotech.maven4.monitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service de gestion des métriques
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public void incrementCounter(String name, String... tags) {
        Counter.builder(name)
                .tags(tags)
                .register(meterRegistry)
                .increment();
    }

    public void recordTimer(String name, long duration, TimeUnit unit, String... tags) {
        Timer.builder(name)
                .tags(tags)
                .register(meterRegistry)
                .record(duration, unit);
    }

    public void recordGauge(String name, Number value, String... tags) {
        meterRegistry.gauge(name, value);
    }
}
