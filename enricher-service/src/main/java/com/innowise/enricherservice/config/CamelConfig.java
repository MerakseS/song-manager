package com.innowise.enricherservice.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.aws2.sqs.Sqs2Component;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.innowise.enricherservice.route.SqsRouteBuilder;

import io.awspring.cloud.autoconfigure.core.AwsClientBuilderConfigurer;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class CamelConfig {

    @Bean
    public SqsClient sqsClient(AwsClientBuilderConfigurer awsClientBuilderConfigurer) {
        return awsClientBuilderConfigurer
            .configure(SqsClient.builder())
            .build();
    }

    @Bean
    public CamelContext camelContext(ApplicationContext applicationContext,
        SqsClient sqsClient, SqsRouteBuilder sqsRouteBuilder) throws Exception {

        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        camelContext.getRegistry().bind("client", sqsClient);
        camelContext.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");

        Sqs2Component component = new Sqs2Component();
        component.setCamelContext(camelContext);
        component.start();

        camelContext.addRoutes(sqsRouteBuilder);

        return camelContext;
    }
}
