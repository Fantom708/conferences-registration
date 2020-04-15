package com.mycode.conferencesregistration.domain;

import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
            name = "conferences_talks",
            joinColumns = {@JoinColumn(name = "conference_id")},
            inverseJoinColumns = {@JoinColumn(name = "talk_id")}
    )
    private Set<Talk> talks = new HashSet<>();

    // TODO kill ?
//    public Set<TalkDto> toDtoTalks() {
//        return talks.stream().
//                map(item -> item.toDto()).
//                collect(Collectors.toSet());
//    }

    public ConferenceDto toDto() {
        return new ConferenceDto(id, name, topic, dateStart, amountParticipants);
    }
}
