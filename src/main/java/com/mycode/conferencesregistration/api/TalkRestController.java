package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.domain.dto.TalkDto;
import com.mycode.conferencesregistration.exception.LimitByNewTalkWasReachedException;
import com.mycode.conferencesregistration.exception.RegistrationDateWasLateException;
import com.mycode.conferencesregistration.exception.TalkNameExistsException;
import com.mycode.conferencesregistration.service.TalkService;
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
public class TalkRestController {

    private Counter counter = Metrics.counter("reports.added");

    private final TalkService talkService;

    @Value
    private class TalkState {
        long id;
    }


    @GetMapping("/{conference_id}/talks")
    public Set<TalkDto> getReportsByConferenceId(
            @PathVariable("conference_id") Long id
    ) {
        return talkService.getTalks(id);
    }

    @PostMapping("/{conference_id}/talks")
    public TalkState addTalk(
            @Valid @RequestBody TalkCreationRequest talk,
            @PathVariable("conference_id") Long id
    ) {
        long newReportId = talkService.addTalkToConference(id, talk);
        counter.increment();
        return new TalkState(newReportId);
    }

    @ExceptionHandler(TalkNameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsReportNameHandler(TalkNameExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(LimitByNewTalkWasReachedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String reportsLimitHandler(LimitByNewTalkWasReachedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RegistrationDateWasLateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String conferenceRegistrationHandler(RegistrationDateWasLateException ex) {
        return ex.getMessage();
    }
}
