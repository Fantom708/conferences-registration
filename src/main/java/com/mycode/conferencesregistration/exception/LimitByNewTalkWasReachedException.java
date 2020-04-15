package com.mycode.conferencesregistration.exception;

/**
 * @author Yurii Kovtun
 */
public class LimitByNewTalkWasReachedException extends RuntimeException {

    public LimitByNewTalkWasReachedException(String reporter, int limit) {
        super(String.format("Max limit [%d] by number of talk for user [%s] was reached", limit, reporter));
    }
}
