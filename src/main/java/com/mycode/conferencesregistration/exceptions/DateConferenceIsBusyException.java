package com.mycode.conferencesregistration.exceptions;

import java.time.LocalDate;

/**
 * @author Yurii Kovtun
 */
public class DateConferenceIsBusyException extends RuntimeException {

    public DateConferenceIsBusyException(LocalDate date) {
        super("Conference date [" + date + "] has already busied");
    }

}
