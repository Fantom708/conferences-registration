package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ReportsLimitException extends RuntimeException {

    public ReportsLimitException(String reporter, int limit) {
        super(String.format("Max limit [%d] by number of report for user [%s] was reached", limit, reporter));
    }
}
