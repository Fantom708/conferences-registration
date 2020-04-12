package com.mycode.conferencesregistration.domain;

import com.mycode.conferencesregistration.domain.dto.ConferenceDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDtoGetResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yurii Kovtun
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String topic;

    @NonNull
    private LocalDate dateStart;

    @NonNull
    private int amountParticipants;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "conferences_reports",
            joinColumns = {@JoinColumn(name = "conference_id")},
            inverseJoinColumns = {@JoinColumn(name = "report_id")}
    )
    private Set<Report> reports = new HashSet<>();

    public ConferenceDtoAddRequest toDtoAddRequest() {
        return new ConferenceDtoAddRequest(name, topic, dateStart, amountParticipants);
    }

    public ConferenceDtoGetResponse toDtoGetResponse() {
        return new ConferenceDtoGetResponse(id, name, topic, dateStart, amountParticipants,
                reports.stream()
                        .map(rep -> rep.toDtoGetResponse())
                        .collect(Collectors.toSet()));
    }
}
