package com.mycode.conferencesregistration.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.domain.dto.ReportAddResult;
import com.mycode.conferencesregistration.domain.dto.ReportDto;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@RestController
@RequestMapping("conferences")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/{conference_id}/talks")
    @JsonView(Views.UserInfo.class)
    public Set<Report> getReportsByConferenceId(
            @PathVariable("conference_id") Long id
    ) {
        return reportService.getReports(id);
    }

    @PostMapping("/{conference_id}/talks")
    public ReportAddResult addReport(
            @Valid @RequestBody ReportDto report,
            @PathVariable("conference_id") Long id) {

        long newReportId = reportService.addReportToConference(id, report);
        return new ReportAddResult(newReportId);
    }

    @ExceptionHandler(ExistsReportNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsReportNameHandler(ExistsReportNameException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ReportsLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String reportsLimitHandler(ReportsLimitException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ReportRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String conferenceRegistrationHandler(ReportRegistrationException ex) {
        return ex.getMessage();
    }
}
