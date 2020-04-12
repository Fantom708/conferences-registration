package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.dto.ConferenceDtoAddRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDtoGetResponse;

import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface ConferenceService {

    List<ConferenceDtoGetResponse> findAll();

    long addConference(ConferenceDtoAddRequest conference);

    void editConference(Long id, ConferenceDtoAddRequest newConference);
}
