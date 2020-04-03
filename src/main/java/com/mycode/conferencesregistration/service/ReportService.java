package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@Service
public class ReportService {

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

}
