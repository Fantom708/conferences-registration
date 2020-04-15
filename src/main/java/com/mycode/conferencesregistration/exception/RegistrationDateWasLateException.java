package com.mycode.conferencesregistration.exception;

/**
 * @author Yurii Kovtun
 */
public class RegistrationDateWasLateException extends RuntimeException {

    public RegistrationDateWasLateException(long days) {
        super(String.format("Registration in less than %d days before the conference is forbidden", days));
    }
}