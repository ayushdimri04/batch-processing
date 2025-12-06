package com.batch_processing.batch.config;

import com.batch_processing.batch.BatchProcessingApplication;
import com.batch_processing.batch.entity.Coffee;
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

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    private final BatchProcessingApplication batchProcessingApplication;

    @Value("${file.input}")
    private String fileInput;

    public BatchConfiguration(BatchProcessingApplication batchProcessingApplication) {
        this.batchProcessingApplication = batchProcessingApplication;
    }

    @Bean
    public FlatFileItemReader reader() {
        return new FlatFileItemReaderBuilder().name("coffeeItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("brand", "origin", "characterstics")
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {
                    {
                        setTargetType(Coffee.class);
                    }
                })
                .build();
    }

    @Bean
    public JdbcBatchItemWriter writer(DataSource datasource){
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characterstics) VALUES (:brand , :origin , :characterstics)")
                .dataSource(datasource)
                .build();
    }
}
