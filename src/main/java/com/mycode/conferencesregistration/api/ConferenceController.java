package com.mycode.conferencesregistration.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycode.conferencesregistration.domain.Views;
import com.mycode.conferencesregistration.domain.dto.ConferenceDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDtoGetResponse;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.service.ConferenceService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

    private Counter counter = Metrics.counter("conferences.added");

    private final ConferenceService conferenceService;

    @Value
    private class ConferenceState {
        private final long id;
    }

    @GetMapping
    @JsonView(Views.UserInfoWithId.class)
    public List<ConferenceDtoGetResponse> getAllConferences() {
        return conferenceService.findAll();
    }

    @PostMapping
    public ConferenceState addConference(@Valid @RequestBody ConferenceDtoAddRequest conference) {
        long id = conferenceService.addConference(conference);
        counter.increment();
        return new ConferenceState(id);
    }

    @PutMapping("/{conference_id}")
    public void editConference(@Valid @RequestBody ConferenceDtoAddRequest newConference,
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
