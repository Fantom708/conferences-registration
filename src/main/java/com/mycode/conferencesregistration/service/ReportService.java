package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ConferenceRegistrationException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@Service
public class ReportService {

    @Value("${maxUseReports}")
    private int maxUseReports;

    @Value("${dayToConferenceRegistration}")
    private long dayToConferenceRegistration;

    private final ReportRepo reportRepo;
    private final ConferenceRepo conferenceRepo;

    public ReportService(ReportRepo reportRepo, ConferenceRepo conferenceRepo) {
        this.reportRepo = reportRepo;
        this.conferenceRepo = conferenceRepo;
    }


    public Set<Report> getReports(Long idConference) {

        Conference conference = conferenceRepo.findById(idConference)
                .orElseThrow(() -> new ConferenceNotFoundException(idConference));

        return conference.getReports();
    }


    @Transactional
    public Report addReportToConference(Long id, Report report) {

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
            throw new ConferenceRegistrationException(dayToConferenceRegistration);
        }

        conference.getReports().add(report);
        reportRepo.save(report);

        return conferenceRepo.save(conference).getReports()
                .stream().
                        filter(data -> Objects.equals(data, report))
                .findFirst().get();
    }

}
