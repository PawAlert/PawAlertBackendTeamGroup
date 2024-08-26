package com.pawalert.backend.global.excel;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    /**
     * 배치 작업이 시작될 때 호출되는 메서드입니다.
     * @param jobExecution 현재 실행 중인 배치 작업의 상태를 담고 있는 객체
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job Started: " + jobExecution.getJobInstance().getJobName());
    }

    /**
     * 배치 작업이 끝날 때 호출되는 메서드입니다.
     * @param jobExecution 완료된 배치 작업의 상태를 담고 있는 객체
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job Ended: " + jobExecution.getJobInstance().getJobName());
    }
}
