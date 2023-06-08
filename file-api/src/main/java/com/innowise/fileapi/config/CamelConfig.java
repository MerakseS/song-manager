package com.innowise.fileapi.config;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.aws2.sqs.Sqs2Component;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.autoconfigure.core.AwsClientBuilderConfigurer;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class CamelConfig {

    @Bean
    public SqsClient sqsClient(AwsClientBuilderConfigurer awsClientBuilderConfigurer) {
        return awsClientBuilderConfigurer.configure(SqsClient.builder()).build();
    }

    @Bean
    public CamelContext camelContext(ApplicationContext applicationContext, SqsClient sqsClient) {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        camelContext.getRegistry().bind("client", sqsClient);
        camelContext.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");

        Sqs2Component component = new Sqs2Component();
        component.setCamelContext(camelContext);
        component.start();

        return camelContext;
    }

    @Bean
    public ProducerTemplate producerTemplate(CamelContext camelContext) {
        return camelContext.createProducerTemplate();
    }
}
