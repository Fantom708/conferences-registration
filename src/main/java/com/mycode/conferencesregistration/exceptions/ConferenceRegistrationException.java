package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ConferenceRegistrationException extends RuntimeException {

    public ConferenceRegistrationException() {
        super("Registration in less than 30 days before the conference is forbidden");
    }
}