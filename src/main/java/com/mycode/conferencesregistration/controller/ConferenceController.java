package com.mycode.conferencesregistration.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.domain.dto.ConferenceAddResult;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Yurii Kovtun
 */
@RestController
@RequestMapping("conferences")
@RequiredArgsConstructor
public class ConferenceController {

    private final ConferenceService conferenceService;

    @GetMapping
    @JsonView(Views.FullInfo.class)
    public List<Conference> getAllConferences() {
        return conferenceService.findAll();
    }

    @PostMapping
    public ConferenceAddResult addConference(@Valid @RequestBody ConferenceDto conference) {
        long id = conferenceService.addConference(conference);
        return new ConferenceAddResult(id);
    }

    @PutMapping("/{conference_id}")
    public void editConference(@Valid @RequestBody ConferenceDto newConference,
                               @PathVariable("conference_id") Long id
    ) {
        conferenceService.editConference(id, newConference);
    }

    @ExceptionHandler(ExistsConferenceNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsConferenceNameHandler(ExistsConferenceNameException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DateConferenceIsBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String dateConferenceIsBusyHandler(DateConferenceIsBusyException ex) {
        return ex.getMessage();
    }
}
