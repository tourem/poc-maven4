package com.larbotech.maven4.monitoring.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MetricsService Tests")
class MetricsServiceTest {

    private MetricsService metricsService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metricsService = new MetricsService(meterRegistry);
    }

    @Test
    @DisplayName("Should increment counter")
    void shouldIncrementCounter() {
        // When
        metricsService.incrementCounter("test.counter", "tag1", "value1");

        // Then
        assertThat(meterRegistry.counter("test.counter", "tag1", "value1").count()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Should record timer")
    void shouldRecordTimer() {
        // When
        metricsService.recordTimer("test.timer", 100, TimeUnit.MILLISECONDS, "tag1", "value1");

        // Then
        assertThat(meterRegistry.timer("test.timer", "tag1", "value1").count()).isEqualTo(1);
    }
}
