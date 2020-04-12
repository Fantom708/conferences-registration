package com.mycode.conferencesregistration.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Yurii Kovtun
 */
@Getter
@AllArgsConstructor
public class ConferenceDtoAddRequest {

    @NotEmpty(message = "The conference name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The conference name can not be missed or empty")
    private String topic;

    @NotNull(message = "The conference date can not be missed or empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @Min(value = 100, message = "The number of participants can not be missed, empty and not less 100")
    private int amountParticipants;
}
