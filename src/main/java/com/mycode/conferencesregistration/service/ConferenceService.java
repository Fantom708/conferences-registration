package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import com.mycode.conferencesregistration.domain.dto.ConferenceUpdateRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface ConferenceService {

    List<ConferenceDto> findAll();

    long addConference(@Valid ConferenceCreationRequest conference);

    void editConference(Long id, ConferenceUpdateRequest newConference);
}
