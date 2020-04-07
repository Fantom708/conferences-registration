package com.mycode.conferencesregistration.domain.dto;

import com.mycode.conferencesregistration.domain.ReportType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yurii Kovtun
 */
@Data
public class ReportDto {
    @NotEmpty(message = "The report name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The report description can not be missed or empty")
    private String description;

    @NotNull(message = "The report type can not be missed or empty")
    @Enumerated(EnumType.STRING)
    private ReportType typeReport;

    @NotEmpty(message = "The reporter can not be missed or empty")
    private String reporter;
}
