package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ReportDtoGetResponse;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.config.ConferenceSettings;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepo reportRepo;
    private final ConferenceRepo conferenceRepo;
    private final ConferenceSettings conferenceSettings;


    @Override
    public Set<ReportDtoGetResponse> getReports(Long idConference) {

        Conference conference = conferenceRepo.findById(idConference)
                .orElseThrow(() -> new ConferenceNotFoundException(idConference));

        return conference.toDtoGetResponse().getReports();
    }

    @Transactional
    @Override
    public long addReportToConference(Long id, ReportDtoAddRequest report) {

        Conference conference = conferenceRepo.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        // Перевірка унікальності назви звіту
        if (!reportRepo.findByNameIgnoreCase(report.getName()).isEmpty()) {
            throw new ExistsReportNameException(report.getName());
        }

        // Перевірка максимально допустимої кількості звітів по звітуючому
        if (reportRepo.findByReporterIgnoreCase(report.getReporter()).size() >= conferenceSettings.getMaxUserReports()) {
            throw new ReportsLimitException(report.getReporter(), conferenceSettings.getMaxUserReports());
        }

        // Перевірка дати подачі заявки на участь у конференції
        if (ChronoUnit.DAYS.between(LocalDate.now(), conference.getDateStart()) <= conferenceSettings.getDayToConferenceRegistration()) {
            throw new ReportRegistrationException(conferenceSettings.getDayToConferenceRegistration());
        }

        Report dbReport = new Report(report.getName(), report.getDescription(), report.getTypeReport(), report.getReporter());
        conference.getReports().add(dbReport);

        return reportRepo.save(dbReport).getId();
    }

}
