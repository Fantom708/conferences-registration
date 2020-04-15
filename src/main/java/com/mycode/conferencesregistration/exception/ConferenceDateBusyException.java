package com.mycode.conferencesregistration.exception;

import java.time.LocalDate;

/**
 * @author Yurii Kovtun
 */
public class ConferenceDateBusyException extends RuntimeException {

    public ConferenceDateBusyException(LocalDate date) {
        super("Conference date [" + date + "] has already busied");
    }

}
