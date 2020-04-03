package com.mycode.conferencesregistration.controller;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Report;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.service.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Yurii Kovtun
 */
@RestController
@RequestMapping("conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping
    public List<Conference> getAllConferences() {
        return conferenceService.findAll();
    }


    // TODO kill
    @GetMapping("/{id}/talks2")
    public Set<Report> getReportsByConferenceId(
            @PathVariable("id") Long id
    ) {
        Conference conference;
        conference = conferenceService.getConference(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        return conference.getReports();
    }

    @PostMapping(headers = "content-type=application/json")
    public Conference addConference(@Valid @RequestBody Conference conference) {
        return conferenceService.addConference(conference);
    }

    @PutMapping("/{conference_id}")
    public Conference editConference(@RequestBody Conference conference,
                                     @PathVariable("conference_id") Long id
    ) {
        return conferenceService.editConference(id, conference);
    }

    @PostMapping("/{conference_id}/talks")
    public Report addReport(
            @RequestBody Report report,
            @PathVariable("conference_id") Conference conference) {

        return conferenceService.addReportToConference(conference, report);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }
}
