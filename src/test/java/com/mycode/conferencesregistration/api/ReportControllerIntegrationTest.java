package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.ReportType;
import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.service.ReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ReportController.class)
public class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    private Long idConference = 1L;
    private Long idReport = 5L;
    private ReportDtoAddRequest reportRequest = new ReportDtoAddRequest(
            "name", "description", ReportType.REPORT, "reporter");


    private ResultActions addPostRequest(Long id, ReportDtoAddRequest report) throws Exception {
        return mockMvc.perform(post(String.format("/conferences/%s/talks", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\n\"name\": \"%s\",\n" +
                                "\"description\": \"%s\",\n" +
                                "\"typeReport\": \"%s\",\n" +
                                "\"reporter\": \"%s\"\n}",
                        report.getName(), report.getDescription(),
                        report.getTypeReport(), report.getReporter()))
        );
    }

    private ResultActions addPostRequest(Long id, String... lines) throws Exception {
        return mockMvc.perform(post(String.format("/conferences/%s/talks", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Stream.of(lines)
                        .collect(joining(",\n", "{\n", "\n}"))
                )
        );
    }

    @Test
    public void addReportSuccess() throws Exception {
        when(reportService.addReportToConference(refEq(idConference), refEq(reportRequest)))
                .thenReturn(idReport);

        addPostRequest(idConference, reportRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idReport));

        verify(reportService).addReportToConference(refEq(idConference), refEq(reportRequest));
    }

    @Test
    public void addReportIfConferenceNotFound() throws Exception {
        when(reportService.addReportToConference(refEq(idConference), refEq(reportRequest)))
                .thenThrow(new ConferenceNotFoundException(idConference));

        addPostRequest(idConference, reportRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void addReportIfExistsReportName() throws Exception {
        when(reportService.addReportToConference(refEq(idConference), refEq(reportRequest)))
                .thenThrow(new ExistsReportNameException(reportRequest.getReporter()));

        addPostRequest(idConference, reportRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void addReportIfReportsLimit() throws Exception {
        when(reportService.addReportToConference(refEq(idConference), refEq(reportRequest)))
                .thenThrow(new ReportsLimitException(reportRequest.getReporter(), 3));

        addPostRequest(idConference, reportRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addReportIfReportRegistrationException() throws Exception {
        when(reportService.addReportToConference(refEq(idConference), refEq(reportRequest)))
                .thenThrow(new ReportRegistrationException(30));

        addPostRequest(idConference, reportRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfIncorrectRequest() throws Exception {
        addPostRequest(1L, "\"name\": \"\"",
                "\"description\": \"description\"",
                "\"typeReport\": \"REPORT\"",
                "\"reporter\": \"reporter\"")
                .andExpect(status().isBadRequest());

        addPostRequest(1L, "\"name\": \"name\"",
                "\"typeReport\": \"REPORT\"",
                "\"reporter\": \"reporter\"")
                .andExpect(status().isBadRequest());

        addPostRequest(1L, "\"name\": \"name\"",
                "\"description\": \"description\"",
                "\"typeReport\": \"---\"",
                "\"reporter\": \"reporter\"")
                .andExpect(status().isBadRequest());

        addPostRequest(1L, "\"name\": \"\"",
                "\"description\": \"description\"",
                "\"typeReport\": \"REPORT\"",
                "\"reporter\": \"\"")
                .andExpect(status().isBadRequest());

        addPostRequest(1L, "\"name\": \"\"",
                "\"description\": \"description\"",
                "\"typeReport\": \"REPORT\"",
                "\"reporter\": \"reporter\"")
                .andExpect(status().isBadRequest());

    }
}