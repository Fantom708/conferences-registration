package com.mycode.conferencesregistration.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

/**
 * @author Yurii Kovtun
 */
@Entity
//@Data
//@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullInfo.class)
    private Long id;

    //    @NotEmpty(message = "The report name can not be missed or empty")
    @NonNull
    @JsonView(Views.UserInfo.class)
    private String name;

    //    @NotEmpty(message = "The report description can not be missed or empty")
    @NonNull
    @JsonView(Views.UserInfo.class)
    private String description;

    //    @NotNull(message = "The report type can not be missed or empty")
    @NonNull
    @JsonView(Views.UserInfo.class)
    @Enumerated(EnumType.STRING)
    private ReportType typeReport;

    //    @NotEmpty(message = "The reporter can not be missed or empty")
    @NonNull
    @JsonView(Views.UserInfo.class)
    private String reporter;
}
