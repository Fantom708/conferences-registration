package com.mycode.conferencesregistration.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullInfo.class)
    private Long id;

    @NotEmpty(message = "The conference name can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    private String name;

    @NotEmpty(message = "The conference name can not be missed or empty")
    @JsonView(Views.UserInfo.class)
    private String topic;

    @NotNull(message = "The conference date can not be missed or empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView(Views.UserInfo.class)
    private LocalDate dateStart;

    @Min(value = 100, message = "The number of participants can not be missed, empty and not less 100")
    @JsonView(Views.UserInfo.class)
    private int amountParticipants;

    @ManyToMany
    @JoinTable(
            name = "conferences_reports",
            joinColumns = {@JoinColumn(name = "conference_id")},
            inverseJoinColumns = {@JoinColumn(name = "report_id")}
    )
    @JsonView(Views.FullInfo.class)
    private Set<Report> reports = new HashSet<>();
}
