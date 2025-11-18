package com.larbotech.maven4.batch.config;

import com.larbotech.maven4.batch.processor.UserItemProcessor;
import com.larbotech.maven4.batch.reader.UserItemReader;
import com.larbotech.maven4.batch.writer.UserItemWriter;
import com.larbotech.maven4.common.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration Spring Batch
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job importUserJob(Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
            .start(step1)
            .build();
    }

    @Bean
    public Step step1(UserItemReader reader,
                      UserItemProcessor processor,
                      UserItemWriter writer) {
        return new StepBuilder("step1", jobRepository)
            .<User, User>chunk(10, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}
