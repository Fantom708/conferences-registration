package com.mycode.conferencesregistration.advise;

import com.mycode.conferencesregistration.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Yurii Kovtun
 */
@ControllerAdvice
public class UserRestrictionsAdvise {

    @ResponseBody
    @ExceptionHandler(ConferenceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String conferenceNotFoundHandler(ConferenceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExistsConferenceNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsConferenceNameHandler(ExistsConferenceNameException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExistsReportNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String existsReportNameHandler(ExistsReportNameException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ReportsLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String reportsLimitHandler(ReportsLimitException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DateConferenceIsBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String dateConferenceIsBusyHandler(DateConferenceIsBusyException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ConferenceRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String conferenceRegistrationHandler(ConferenceRegistrationException ex) {
        return ex.getMessage();
    }
}
