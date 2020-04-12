package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.ReportType;
import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.config.ConferenceSettings;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import com.mycode.conferencesregistration.repo.ReportRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("classpath:application-test.properties")
public class ReportServiceImplTest {

    private ReportService reportService;
    private ConferenceSettings conferenceSettings;

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private ConferenceRepo conferenceRepo;

    @Before
    public void init() {
        conferenceSettings = new ConferenceSettings();
        reportService = new ReportServiceImpl(reportRepo, conferenceRepo, conferenceSettings);
    }

    @Test
    public void getReportsReturnEmptyList() {
        Conference conference =
                new Conference("name", "topic", LocalDate.now(), 200);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(conference));

        assertThat(reportService.getReports(1L), is(conference.toDtoGetResponse().getReports()));
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void getReportsIfUnknownConferenceId() {
        when(conferenceRepo.findById(notNull())).thenReturn(Optional.empty());

        reportService.getReports(1L);
    }

    @Test
    public void addReportSuccess() {
        ReportDtoAddRequest report = new ReportDtoAddRequest("name", "description", ReportType.REPORT, "report");
        Long id = 1L;
        Conference dbConference =
                new Conference("name", "topic", LocalDate.of(2099, 1, 1), 200);
        Report dbReport = new Report("name", "description", ReportType.REPORT, "report");
        dbReport.setId(id);
        dbConference.getReports().add(dbReport);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(reportRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
        when(reportRepo.findByReporterIgnoreCase(notNull())).thenReturn(emptyList());
        when(reportRepo.save(notNull())).thenReturn(dbReport);

        assertThat(reportService.addReportToConference(id, report),
                is(
                        dbConference.getReports()
                                .stream()
                                .filter(data -> Objects.equals(data, dbReport))
                                .findFirst().get()
                                .getId()
                )
        );
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void addReportIfUnknownConferenceId() {
        ReportDtoAddRequest report = new ReportDtoAddRequest("name", "description", ReportType.REPORT, "report");

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.empty());

        reportService.addReportToConference(1L, report);
    }

    @Test(expected = ExistsReportNameException.class)
    public void addReportIfReportExists() {
        ReportDtoAddRequest report = new ReportDtoAddRequest("name", "description", ReportType.REPORT, "report");
        Conference dbConference =
                new Conference("name", "topic", LocalDate.of(2099, 1, 1), 200);
        Report dbReport = new Report("name", "description", ReportType.REPORT, "report");

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(reportRepo.findByNameIgnoreCase(notNull())).thenReturn(Arrays.asList(dbReport));

        reportService.addReportToConference(1L, report);
    }

    @Test(expected = ReportsLimitException.class)
    public void addReportIfMaxReportLimitOverflow() {
        ReportDtoAddRequest report = new ReportDtoAddRequest("name", "description", ReportType.REPORT, "report");
        Conference dbConference =
                new Conference("name", "topic", LocalDate.of(2099, 1, 1), 200);
        Report dbReport = new Report("name", "description", ReportType.REPORT, "report");

        List<Report> listReport = new ArrayList<>();
        for (int i = 0; i <= conferenceSettings.getMaxUserReports(); i++) {
            listReport.add(dbReport);
        }

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(reportRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
        when(reportRepo.findByReporterIgnoreCase(notNull())).thenReturn(listReport);

        reportService.addReportToConference(1L, report);
    }

    @Test(expected = ReportRegistrationException.class)
    public void addReportIfDaysLessThanPermit() {
        ReportDtoAddRequest report = new ReportDtoAddRequest("name", "description", ReportType.REPORT, "report");
        Conference dbConference =
                new Conference("name", "topic", LocalDate.of(2000, 1, 1), 200);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(reportRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
        when(reportRepo.findByReporterIgnoreCase(notNull())).thenReturn(emptyList());

        reportService.addReportToConference(1L, report);
    }
}
