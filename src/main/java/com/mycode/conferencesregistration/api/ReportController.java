package com.mycode.conferencesregistration.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.domain.dto.ReportDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ReportDtoGetResponse;
import com.mycode.conferencesregistration.exceptions.ExistsReportNameException;
import com.mycode.conferencesregistration.exceptions.ReportRegistrationException;
import com.mycode.conferencesregistration.exceptions.ReportsLimitException;
import com.mycode.conferencesregistration.service.ReportService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

    private Counter counter = Metrics.counter("reports.added");

    private final ReportService reportService;

    @Value
    private class ReportState {
        private final long id;
    }


    @GetMapping("/{conference_id}/talks")
    @JsonView(Views.UserInfoWithId.class)
    public Set<ReportDtoGetResponse> getReportsByConferenceId(
            @PathVariable("conference_id") Long id
    ) {
        return reportService.getReports(id);
    }

    @PostMapping("/{conference_id}/talks")
    public ReportState addReport(
            @Valid @RequestBody ReportDtoAddRequest report,
            @PathVariable("conference_id") Long id) {

        long newReportId = reportService.addReportToConference(id, report);
        counter.increment();
        return new ReportState(newReportId);
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
