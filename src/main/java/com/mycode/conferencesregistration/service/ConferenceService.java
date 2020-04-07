package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;

import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface ConferenceService {

    List<Conference> findAll();

    long addConference(ConferenceDto conference);

    void editConference(Long id, ConferenceDto newConference);
}
