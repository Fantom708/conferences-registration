package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ConferenceRegistrationException extends RuntimeException {

    public ConferenceRegistrationException(long days) {
        super(String.format("Registration in less than %d days before the conference is forbidden", days));
    }
}