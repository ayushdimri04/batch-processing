package com.batch_processing.batch.config;

import com.batch_processing.batch.BatchProcessingApplication;
import com.batch_processing.batch.entity.Coffee;
import com.batch_processing.batch.listener.JobCompletionNotificationListener;
import com.batch_processing.batch.processor.CoffeeItemProcessor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Value("${file.input}")
    private String fileInput;

    @Bean
    public FlatFileItemReader<Coffee> reader() {
        return new FlatFileItemReaderBuilder<Coffee>().name("coffeeItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("id" , "brand", "origin", "characterstics")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Coffee>() {
                    {
                        setTargetType(Coffee.class);
                    }
                })
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Coffee> writer(DataSource datasource){
        return new JdbcBatchItemWriterBuilder<Coffee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characterstics) VALUES (:brand , :origin , :characterstics)")
                .dataSource(datasource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1){

        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                      JdbcBatchItemWriter<Coffee> writer){
        return new StepBuilder("step1", jobRepository)
                .<Coffee, Coffee>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public CoffeeItemProcessor processor(){
        return new CoffeeItemProcessor();
    }
}
