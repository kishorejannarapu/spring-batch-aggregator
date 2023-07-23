package com.batch.valuation.jobdef;

import com.batch.valuation.listener.JobCompletionNotificationListener;
import com.batch.valuation.model.FxMarketEvent;
import com.batch.valuation.model.CacheUtil;
import com.batch.valuation.model.Trade;
import com.batch.valuation.processor.FxMarketEventProcessor;
import com.batch.valuation.reader.FxMarketEventReader;
import com.batch.valuation.writer.StockVolumeWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class JobDef {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public CacheUtil fxMarketPricesStore() {
        return new CacheUtil();
    }

    // FxMarketEventReader (Reader)
    @Bean
    public FxMarketEventReader fxMarketEventReader() {
        return new FxMarketEventReader();
    }

    // FxMarketEventProcessor (Processor)
    @Bean
    public FxMarketEventProcessor fxMarketEventProcessor() {
        return new FxMarketEventProcessor();
    }

    // StockVolumeAggregator (Writer)
    @Bean
    public StockVolumeWriter stockVolumeAggregator() {
        return new StockVolumeWriter();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    // Configure job step
    @Bean("fxMarketJob")
    public Job fxMarketPricesETLJob() {
        return jobBuilderFactory.get("fxMarketJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(etlStep())
                .end()
                .build();
    }

    @Bean
    public Step etlStep() {
        return stepBuilderFactory.get("etlStep")
                .<FxMarketEvent, Trade>chunk(10000)
                .reader(fxMarketEventReader())
                .processor(fxMarketEventProcessor())
                .writer(stockVolumeAggregator())
                .taskExecutor(taskExecutor()).build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(64);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(64);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("thread-");
        return executor;
    }

}