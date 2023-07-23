package com.batch.valuation.listener;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import com.batch.valuation.model.CacheUtil;
import com.batch.valuation.model.StockVolume;

/**
 * The Class JobCompletionNotificationListener
 *
 * @author ashraf
 */
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private static final String HEADER = "stock,volume";

    private static final String LINE_DILM = ",";

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.trace("Loading the results into file");
            Path path = Paths.get("volume.csv");
            try (BufferedWriter fileWriter = Files.newBufferedWriter(path)) {
                fileWriter.write(HEADER);
                fileWriter.newLine();
                Cache<String, StockVolume> cache = CacheUtil.getEhCache();
                List<StockVolume> list = StreamSupport.stream(cache.spliterator(), true).map(Cache.Entry::getValue).collect(Collectors.toList());
                for (StockVolume pd : list) {
                    fileWriter.write(pd.getStock() +
                            LINE_DILM + pd.getVolume());
                    fileWriter.newLine();
                }
            } catch (Exception e) {
                log.error("Fetal error: error occurred while writing {} file", path.getFileName());
            }
        }
    }
}
