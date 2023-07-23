package com.batch.valuation.controller;

import com.batch.valuation.model.ServerResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class JobController {

    @Autowired
    @Qualifier("fxMarketJob")
    Job fxJob;

    @Autowired
    JobLauncher jobLauncher;


//    @GetMapping("/job")
//    public ServerResponse launchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//
//        Map<String, JobParameter> maps = new HashMap<>();
//        maps.put("time", new JobParameter(System.currentTimeMillis()));
//        JobParameters parameters = new JobParameters(maps);
//        JobExecution jobExecution = jobLauncher.run(job, parameters);
//
//        return new ServerResponse("success");
//    }

    @GetMapping("/fxJob")
    public ServerResponse fxJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(fxJob, parameters);

        return new ServerResponse("success");
    }
}
