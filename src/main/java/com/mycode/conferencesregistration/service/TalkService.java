package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.domain.dto.TalkDto;

import java.util.Set;

/**
 * @author Yurii Kovtun
 */
public interface TalkService {

    Set<TalkDto> getTalks(Long idConference);

    long addTalkToConference(Long id, TalkCreationRequest report);
}
