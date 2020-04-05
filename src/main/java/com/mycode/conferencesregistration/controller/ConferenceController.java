package com.mycode.conferencesregistration.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.service.ConferenceService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @JsonView(Views.UserInfo.class)
    public List<Conference> getAllConferences() {
        return conferenceService.findAll();
    }

    @PostMapping(headers = "content-type=application/json")
    @JsonView(Views.FullInfo.class)
    public Conference addConference(@Valid @RequestBody Conference conference) {
        return conferenceService.addConference(conference);
    }

    @PutMapping("/{conference_id}")
    @JsonView(Views.FullInfo.class)
    public Conference editConference(@Valid @RequestBody Conference conference,
                                     @PathVariable("conference_id") Long id
    ) {
        return conferenceService.editConference(id, conference);
    }

}
