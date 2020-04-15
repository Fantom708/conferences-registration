package com.mycode.conferencesregistration.exception;

/**
 * @author Yurii Kovtun
 */
public class ConferenceNotFoundException extends RuntimeException {

    public ConferenceNotFoundException(Long id) {
        super("Could not find conference [" + id + "]");
    }
}