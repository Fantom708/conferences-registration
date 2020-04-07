package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.dto.ReportDto;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${maxUseReports}")
    private int maxUseReports;

    @Value("${dayToConferenceRegistration}")
    private long dayToConferenceRegistration;

    private final ReportRepo reportRepo;
    private final ConferenceRepo conferenceRepo;


    @Override
    public Set<Report> getReports(Long idConference) {

        Conference conference = conferenceRepo.findById(idConference)
                .orElseThrow(() -> new ConferenceNotFoundException(idConference));

        return conference.getReports();
    }


    @Transactional
    @Override
    public long addReportToConference(Long id, ReportDto report) {

        Conference conference = conferenceRepo.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        // Перевірка унікальності назви звіту
        if (!reportRepo.findByNameIgnoreCase(report.getName()).isEmpty()) {
            throw new ExistsReportNameException(report.getName());
        }

        // Перевірка максимально допустимої кількості звітів по звітуючому
        if (reportRepo.findByReporterIgnoreCase(report.getReporter()).size() >= maxUseReports) {
            throw new ReportsLimitException(report.getReporter());
        }

        // Перевірка дати подачі заявки на участь у конференції
        if (ChronoUnit.DAYS.between(LocalDate.now(), conference.getDateStart()) <= dayToConferenceRegistration) {
            throw new ReportRegistrationException(dayToConferenceRegistration);
        }

        Report dbReport = new Report(report.getName(), report.getDescription(), report.getTypeReport(), report.getReporter());
        conference.getReports().add(dbReport);

        return reportRepo.save(dbReport).getId();
    }

}
