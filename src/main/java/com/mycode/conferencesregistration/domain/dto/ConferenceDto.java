package com.mycode.conferencesregistration.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.LocalDate;

/**
 * @author Yurii Kovtun
 */
@Value
public class ConferenceDto {

    Long id;
    String name;
    String topic;
    LocalDate dateStart;
    int amountParticipants;
}
