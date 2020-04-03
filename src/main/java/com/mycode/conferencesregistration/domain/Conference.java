package com.mycode.conferencesregistration.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
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
    private Long id;

    @NotEmpty(message = "The conference name can not be missed or empty")
    private String name;

    @NotEmpty(message = "The conference name can not be missed or empty")
    private String topic;

    // TODO Date format
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @NotEmpty(message = "The conference date can not be missed or empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dateStart;

    @Min(value = 100, message = "The number of participants can not be missed, empty and not less 100")
//    @NotNull(message = "The number of participants can not be missed or empty")
    private int amountParticipants;

    @ManyToMany
    @JoinTable(
            name = "conferences_reports",
            joinColumns = {@JoinColumn(name = "report_id")},
            inverseJoinColumns = {@JoinColumn(name = "conference_id")}
    )
    private Set<Report> reports = new HashSet<>();
}
