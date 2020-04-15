package com.mycode.conferencesregistration.exception;

/**
 * @author Yurii Kovtun
 */
public class TalkNameExistsException extends RuntimeException {

    public TalkNameExistsException(String name) {
        super("The name of talk [" + name + "] has already used");
    }
}