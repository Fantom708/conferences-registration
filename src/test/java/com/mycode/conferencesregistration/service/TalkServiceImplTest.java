package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.config.ConferenceSettings;
import com.mycode.conferencesregistration.dao.ConferenceDao;
import com.mycode.conferencesregistration.dao.TalkDao;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Talk;
import com.mycode.conferencesregistration.domain.TypeTalk;
import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exception.LimitByNewTalkWasReachedException;
import com.mycode.conferencesregistration.exception.RegistrationDateWasLateException;
import com.mycode.conferencesregistration.exception.TalkNameExistsException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
public class TalkServiceImplTest {

    private TalkService service;
    private ConferenceSettings settings;

    @Mock
    private TalkDao talkDao;

    @Mock
    private ConferenceDao conferenceDao;

    private Long id = 1L;
    private Conference dbConference = new Conference("name", "topic", LocalDate.now(), 200);
    private Talk dbTalk = new Talk(
            "name", "description", TypeTalk.REPORT, "report");
    private TalkCreationRequest talk = new TalkCreationRequest(
            "name", "description", TypeTalk.REPORT, "report");


    @Before
    public void init() {
        settings = new ConferenceSettings();
        service = new TalkServiceImpl(talkDao, conferenceDao, settings);
    }

    @Test
    public void getTalksSuccess() {
        Set<Talk> talks = new HashSet<Talk>() {{
            add(dbTalk);
        }};
        dbConference.setTalks(talks);

        when(conferenceDao.findById(id)).thenReturn(Optional.of(dbConference));

        Assertions.assertThat(service.getTalks(id)).isEqualTo(talks
                .stream()
                .map(item -> item.toDto())
                .collect(Collectors.toSet()));
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void getTalksIfConferenceIdUnknownThenThrowException() {
        when(conferenceDao.findById(notNull())).thenReturn(Optional.empty());

        service.getTalks(id);
    }

    @Test
    public void addTalkSuccess() {
        dbConference.setDateStart(LocalDate.of(9999, 1, 1));
        dbTalk.setId(id);

        when(conferenceDao.findById(id)).thenReturn(Optional.of(dbConference));
        when(talkDao.findByNameIgnoreCase("name")).thenReturn(emptyList());
        when(talkDao.findByReporterIgnoreCase("report")).thenReturn(emptyList());
        when(talkDao.save(refEq(dbTalk, "id"))).thenReturn(dbTalk);

        Assertions.assertThat(service.addTalkToConference(id, talk)).isEqualTo(id);
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void addTalkIfConferenceNotFoundThenThrowException() {
        when(conferenceDao.findById(id)).thenReturn(Optional.empty());

        service.addTalkToConference(id, talk);
    }

    @Test(expected = TalkNameExistsException.class)
    public void addTalkIfTalkNameExistsThenThrowException() {
        when(conferenceDao.findById(id)).thenReturn(Optional.of(dbConference));
        when(talkDao.findByNameIgnoreCase("name")).thenReturn(Arrays.asList(dbTalk));

        service.addTalkToConference(id, talk);
    }

    @Test(expected = LimitByNewTalkWasReachedException.class)
    public void addTalkIfMaxOfTalkWasReachedThenThrowException() {
        List<Talk> listTalk = Collections.nCopies(settings.getMaxUserTalks(), dbTalk);

        when(conferenceDao.findById(id)).thenReturn(Optional.of(dbConference));
        when(talkDao.findByReporterIgnoreCase("report")).thenReturn(listTalk);

        service.addTalkToConference(id, talk);
    }

    @Test(expected = RegistrationDateWasLateException.class)
    public void addTalkIfRegistrationDateWasLateThenThrowException() {
        when(conferenceDao.findById(id)).thenReturn(Optional.of(dbConference));

        service.addTalkToConference(id, talk);
    }
}
