package com.mycode.conferencesregistration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * @author Yurii Kovtun
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "conference")
@Validated
public class ConferenceSettings {

    /**
     * Maximum number of user reports allowed for one conference
     */
    @Min(value = 1)
    private int maxUserReports = 3;

    /**
     * Min days before registration of the report at the conference
     */
    @Min(1)
    private int dayToConferenceRegistration = 30;
}
