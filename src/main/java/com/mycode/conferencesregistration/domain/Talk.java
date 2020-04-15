package com.mycode.conferencesregistration.domain;

import com.mycode.conferencesregistration.domain.dto.TalkDto;
import lombok.*;

import javax.persistence.*;

/**
 * @author Yurii Kovtun
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TypeTalk typeTalk;

    @NonNull
    private String reporter;

    public TalkDto toDto() {
        return new TalkDto(id, name, description, typeTalk, reporter);
    }
}