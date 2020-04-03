package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ExistsConferenceNameException extends RuntimeException {

    public ExistsConferenceNameException(String name) {
        super("The name conference [" + name + "] has already used");
    }
}
