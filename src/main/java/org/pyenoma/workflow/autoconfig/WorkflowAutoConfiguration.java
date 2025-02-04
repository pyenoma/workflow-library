package org.pyenoma.workflow.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@AutoConfiguration
@ComponentScan(basePackages = "org.pyenoma.workflow")
public class WorkflowAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(Executor.class)
    public Executor taskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
