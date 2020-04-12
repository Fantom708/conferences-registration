package com.mycode.conferencesregistration.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Views;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ConferenceDtoGetResponse {

    @JsonView(Views.UserInfoWithId.class)
    private Long id;

    @JsonView(Views.UserInfo.class)
    private String name;

    @JsonView(Views.UserInfo.class)
    private String topic;

    @JsonView(Views.UserInfo.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @JsonView(Views.UserInfo.class)
    private int amountParticipants;

    @JsonView(Views.FullInfo.class)
    private Set<ReportDtoGetResponse> reports;
}
