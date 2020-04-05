package com.mycode.conferencesregistration.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yurii Kovtun
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "reporter", "typeReport", "name", "description",})
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullInfo.class)
    private Long id;

    @NotEmpty(message = "The report name can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    private String name;

    @NotEmpty(message = "The report description can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    private String description;

    @NotNull(message = "The report type can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    @Enumerated(EnumType.STRING)
    private ReportType typeReport;

    @NotEmpty(message = "The reporter can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    private String reporter;
}
