package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ReportDtoGetResponse;

import java.util.Set;

/**
 * @author Yurii Kovtun
 */
public interface ReportService {

    Set<ReportDtoGetResponse> getReports(Long idConference);

    long addReportToConference(Long id, ReportDtoAddRequest report);
}
