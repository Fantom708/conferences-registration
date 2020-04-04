package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.*;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Yurii Kovtun
 */
@Service
public class ConferenceService {

    @Value("${maxUseReports}")
    private int maxUseReports;

    @Value("${dayToConferenceRegistration}")
    private long dayToConferenceRegistration;

    private final ReportRepo reportRepo;
    private final ConferenceRepo conferenceRepo;

    public ConferenceService(ConferenceRepo conferenceRepo, ReportRepo reportRepo) {
        this.conferenceRepo = conferenceRepo;
        this.reportRepo = reportRepo;
    }

    public List<Conference> findAll() {
/*        List<Conference> list = new ArrayList<>(Arrays.asList(
                new Conference("Конференция1", "Тема конференции1", LocalDateTime.now(), 100,
                        new ArrayList<>(Arrays.asList(
                                new Report("Доклад1", "COVID-19", ReportType.REPORT, "Ivanov I.I.")))),
                new Conference("Конференция2", "Тема конференции2", LocalDateTime.now(), 100, null)));

        return list;*/

        return conferenceRepo.findAll();
    }

    public List<Report> findReportsByConference(Conference conference) {
        return null; //conferenceRepo.findByConferences(conference);
    }

    public Optional<Conference> getConference(Long id) {
        return conferenceRepo.findById(id);
    }

    public Conference addConference(Conference conference) {

        // Перевірка унікальності назви конференції
        if (!conferenceRepo.findByNameIgnoreCase(conference.getName()).isEmpty()) {
            throw new ExistsConferenceNameException(conference.getName());
        }

        //Перевірка вільної дати проведення конференції
        if (!conferenceRepo.findByDateStart(conference.getDateStart()).isEmpty()) {
            throw new DateConferenceIsBusyException(conference.getDateStart());
        }

        return conferenceRepo.save(conference);
    }

    public Conference editConference(Long id, Conference newConference) {
        Conference conference = getConference(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        BeanUtils.copyProperties(newConference, conference, "id", "reports");

        return conferenceRepo.save(conference);
    }

    // TODO use transaction
    // TODO move to Report
    public Report addReportToConference(Conference conference, Report report) {

        // Перевірка унікальності назви звіту
        if (!reportRepo.findByNameIgnoreCase(report.getName()).isEmpty()) {
            throw new ExistsReportNameException(report.getName());
        }

        // Перевірка максимально допустимої кількості звітів по звітуючому
        if (reportRepo.findByReporterIgnoreCase(report.getReporter()).size() >= maxUseReports) {
            throw new ReportsLimitException(report.getReporter());
        }

        // Перевірка дати подачі заявки на участь у конференції
        if (ChronoUnit.DAYS.between(LocalDate.now(), conference.getDateStart())<= dayToConferenceRegistration) {
            throw new ConferenceRegistrationException();
        }

        conference.getReports().add(report);
        reportRepo.save(report);

        return conferenceRepo.save(conference).getReports()
                .stream().
                        filter(data -> Objects.equals(data, report))
                .findFirst().get();
    }

}
