package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import com.mycode.conferencesregistration.domain.dto.ConferenceUpdateRequest;
import com.mycode.conferencesregistration.exception.ConferenceDateBusyException;
import com.mycode.conferencesregistration.exception.ConferenceNameExistsException;
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
public class ConferenceRestController {

    private Counter counter = Metrics.counter("conferences.added");

    private final ConferenceService conferenceService;

    @Value
    private class ConferenceState {
        long id;
    }

    @GetMapping
    public List<ConferenceDto> getAllConferences() {
        return conferenceService.findAll();
    }

    @PostMapping
    public ConferenceState addConference(@RequestBody ConferenceCreationRequest conference) {
        long id = conferenceService.addConference(conference);
        counter.increment();

        return new ConferenceState(id);
    }

    @PutMapping("/{conference_id}")
    public void editConference(@Valid @RequestBody ConferenceUpdateRequest newConference,
                               @PathVariable("conference_id") Long id
    ) {
        conferenceService.editConference(id, newConference);
    }

    @ExceptionHandler(ConferenceNameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsConferenceNameHandler(ConferenceNameExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ConferenceDateBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String dateConferenceIsBusyHandler(ConferenceDateBusyException ex) {
        return ex.getMessage();
    }
}
