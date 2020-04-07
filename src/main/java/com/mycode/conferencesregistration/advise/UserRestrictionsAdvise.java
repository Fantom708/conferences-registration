package com.mycode.conferencesregistration.advise;

import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Yurii Kovtun
 */
@ControllerAdvice
public class UserRestrictionsAdvise {

    @ExceptionHandler(ConferenceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String conferenceNotFoundHandler(ConferenceNotFoundException ex) {
        return ex.getMessage();
    }

}
