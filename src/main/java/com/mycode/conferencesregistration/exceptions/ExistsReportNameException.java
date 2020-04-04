package com.mycode.conferencesregistration.exceptions;

/**
 * @author Yurii Kovtun
 */
public class ExistsReportNameException  extends RuntimeException {

    public ExistsReportNameException(String name) {
        super("The name of report [" + name + "] has already used");
    }
}