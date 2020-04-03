package com.mycode.conferencesregistration.controller;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Set<Report> getReportsByConferenceId(
//            @PathVariable("conference_id") Conference conference,
            @PathVariable("conference_id") Long id
    ) {
        return reportService.getReports(id);
    }

/*    @ExceptionHandler(ConferenceNotFoundException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad recommendations!!!")
    void onSaveError() {
    }*/
}
