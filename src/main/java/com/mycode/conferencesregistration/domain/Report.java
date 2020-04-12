package com.mycode.conferencesregistration.domain;

import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ReportDtoGetResponse;
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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ReportType typeReport;

    @NonNull
    private String reporter;

    public ReportDtoAddRequest toDtoAddRequest() {
        return new ReportDtoAddRequest(name, description, typeReport, reporter);
    }

    public ReportDtoGetResponse toDtoGetResponse() {
        return new ReportDtoGetResponse(id, name, description, typeReport, reporter);
    }
}