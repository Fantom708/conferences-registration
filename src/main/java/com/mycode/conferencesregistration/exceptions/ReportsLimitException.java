package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ReportsLimitException extends RuntimeException {

    public ReportsLimitException(String reporter) {
        super("Max limit by number of report for user [" + reporter + "] was reached");
    }
}
