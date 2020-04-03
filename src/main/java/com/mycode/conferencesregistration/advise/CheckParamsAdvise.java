package com.mycode.conferencesregistration.advise;

import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Yurii Kovtun
 */
@ControllerAdvice
public class CheckParamsAdvise {

    @ResponseBody
    @ExceptionHandler(ConferenceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String conferenceNotFoundHandler(ConferenceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExistsConferenceNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String ExistsConferenceNameHandler(ExistsConferenceNameException ex) {
        return ex.getMessage();
    }
}
