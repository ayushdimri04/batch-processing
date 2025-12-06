package com.batch_processing.batch.listener;

import com.batch_processing.batch.entity.Coffee;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void afterJob(JobExecution jobExecution){
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            logger.info("***Batch Job is finished***");

            String query = "SELECT id , brand , origin , characterstics FROM coffee";

            jdbcTemplate.query(query, (rs , row) -> new Coffee(
                            rs.getLong("id"),
                            rs.getString("brand"),
                            rs.getString("origin"),
                            rs.getString("characterstics")
            ))
                    .forEach(coffee -> logger.info("Found {} in mydb",coffee));
        }
    }
}
