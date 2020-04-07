package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.dto.ReportDto;

import java.util.Set;

/**
 * @author Yurii Kovtun
 */
public interface ReportService {

    Set<Report> getReports(Long idConference);

    long addReportToConference(Long id, ReportDto report);
}
