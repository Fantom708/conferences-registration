package com.mycode.conferencesregistration.domain.dto;

import com.mycode.conferencesregistration.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yurii Kovtun
 */
@Getter
@AllArgsConstructor
public class ReportDtoAddRequest {

    @NotEmpty(message = "The report name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The report description can not be missed or empty")
    private String description;

    @NotNull(message = "The report type can not be missed or empty")
    private ReportType typeReport;

    @NotEmpty(message = "The reporter can not be missed or empty")
    private String reporter;
}
