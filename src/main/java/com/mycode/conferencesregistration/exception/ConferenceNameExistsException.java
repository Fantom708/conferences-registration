package com.mycode.conferencesregistration.exception;

/**
 * @author Yurii Kovtun
 */
public class ConferenceNameExistsException extends RuntimeException {

    public ConferenceNameExistsException(String name) {
        super("The name of conference [" + name + "] has already used");
    }
}
