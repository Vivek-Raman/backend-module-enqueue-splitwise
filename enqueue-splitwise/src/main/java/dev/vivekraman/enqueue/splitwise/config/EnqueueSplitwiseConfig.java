package dev.vivekraman.enqueue.splitwise.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@ConfigurationPropertiesScan(basePackages="dev.vivekraman.enqueue.splitwise.config")
@EnableR2dbcRepositories(basePackages = "dev.vivekraman.enqueue.splitwise.repository")
public class EnqueueSplitwiseConfig {

  @Bean
  public GroupedOpenApi enqueueSplitwiseApiGroup() {
    return GroupedOpenApi.builder()
      .group(Constants.MODULE_NAME)
      .packagesToScan("dev.vivekraman.enqueue.splitwise.controller")
      .build();
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer javaTimeModuleCustomizer() {
    return builder -> builder
        .modules(new JavaTimeModule())
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
