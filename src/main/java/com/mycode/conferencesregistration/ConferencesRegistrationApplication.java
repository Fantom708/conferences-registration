package com.mycode.conferencesregistration;

import com.mycode.conferencesregistration.config.ConferenceSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(ConferenceSettings.class)
public class ConferencesRegistrationApplication {

    @Bean
    public MeterRegistryCustomizer<?> commonTagsCustomizer(@Value("${spring.application.name}") final String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    public static void main(String[] args) {
        SpringApplication.run(ConferencesRegistrationApplication.class, args);
    }

}
