package com.pawalert.backend.global.batch;

import com.pawalert.backend.global.excel.ExcelDataParser;
import com.pawalert.backend.global.excel.ExcelFileDownloader;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ExcelFileDownloader excelFileDownloader;
    private final ExcelDataParser excelDataParser;
    private final JobLauncher jobLauncher;
    private final JobExecutionListener jobExecutionListener;

    @Autowired
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                       ExcelFileDownloader excelFileDownloader, ExcelDataParser excelDataParser,
                       JobLauncher jobLauncher, JobExecutionListener jobExecutionListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.excelFileDownloader = excelFileDownloader;
        this.excelDataParser = excelDataParser;
        this.jobLauncher = jobLauncher;
        this.jobExecutionListener = jobExecutionListener; // Listener 초기화
    }

    @Bean
    public Job importHospitalJob() {
        return new JobBuilder("importHospitalJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener) // Listener를 사용
                .start(step1())
                .build();
    }
    @Bean
    public Job importAnimalJob() {
        return new JobBuilder("importAnimalJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener) // Listener를 사용
                .start(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    String url = "https://www.animal.go.kr/front/awtis/shop/hospitalExcelList.do";
                    String fileName = "hospital_data.xlsx";
                    String filePath = null;
                    try {
                        filePath = excelFileDownloader.downloadExcelFile(url, fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (filePath != null) {
                        try {
                            excelDataParser.parseAndSaveData(filePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("File download failed.");
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    String url = "https://www.animal.go.kr/front/awtis/institution/poiExcel.do";
                    String fileName = "poiExcel_data.xlsx";
                    String filePath = null;
                    try {
                        filePath = excelFileDownloader.downloadExcelFile(url, fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (filePath != null) {
                        try {
                            excelDataParser.parseAnimalSaveData(filePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("File download failed.");
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    //    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    @Scheduled(cron = "0 0 1 * * ?")
    public void performJob() {
        try {
            jobLauncher.run(importHospitalJob(), new JobParametersBuilder().toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0 0 2 * * ?")
    public void performJob2() {
        try {
            jobLauncher.run(importAnimalJob(), new JobParametersBuilder().toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
