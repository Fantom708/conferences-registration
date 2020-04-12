package com.mycode.conferencesregistration.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.ReportType;
import com.mycode.conferencesregistration.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Yurii Kovtun
 */
@Getter
@AllArgsConstructor
@ToString
public class ReportDtoGetResponse {

    @JsonView(Views.UserInfoWithId.class)
    private Long id;

    @JsonView(Views.UserInfo.class)
    private String name;

    @JsonView(Views.UserInfo.class)
    private String description;

    @JsonView(Views.UserInfo.class)
    private ReportType typeReport;

    @JsonView(Views.UserInfo.class)
    private String reporter;
}
