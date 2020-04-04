package com.mycode.conferencesregistration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
    private Long id;

    @NotEmpty(message = "The report name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The report description can not be missed or empty")
    private String description;

    // TODO
//    @NotEmpty(message = "The report type can not be missed or empty")
    @Enumerated(EnumType.STRING)
    private ReportType typeReport;

    private String reporter;
}
