package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Yurii Kovtun
 */
@Service
public class ConferenceService {

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

        Conference conf  = conferenceRepo.findByNameIgnoreCase(conference.getName())
                .orElseThrow(() -> new ExistsConferenceNameException(conference.getName()));
        return conferenceRepo.save(conference);
    }

    public Conference editConference(Long id, Conference newConference) {
        Conference conference = getConference(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        BeanUtils.copyProperties(newConference, conference, "id", "reports");

        return conferenceRepo.save(conference);
    }

    public Report addReportToConference(Conference conference, Report report) {
        conference.getReports().add(report);
        reportRepo.save(report);

        return conferenceRepo.save(conference).getReports()
                .stream().
                        filter(data -> Objects.equals(data, report))
                .findFirst().get();
    }

}
