package com.mycode.conferencesregistration.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.service.ReportService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@RestController
@RequestMapping("conferences")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{conference_id}/talks")
    @JsonView(Views.UserInfo.class)
    public Set<Report> getReportsByConferenceId(
            @PathVariable("conference_id") Long id
    ) {
        return reportService.getReports(id);
    }

    @PostMapping("/{conference_id}/talks")
    @JsonView(Views.FullInfo.class)
    public Report addReport(
            @Valid @RequestBody Report report,
            @PathVariable("conference_id") Long id) {

        return reportService.addReportToConference(id, report);
    }

}
