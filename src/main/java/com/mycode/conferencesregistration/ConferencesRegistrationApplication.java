package com.mycode.conferencesregistration;

import com.mycode.conferencesregistration.config.ConferenceSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConferenceSettings.class)
public class ConferencesRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferencesRegistrationApplication.class, args);
    }
}

