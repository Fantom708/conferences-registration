package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceDtoAddRequest;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
public class ConferenceServiceImplTest {

    private ConferenceService conferenceService;

    @Mock
    private ConferenceRepo conferenceRepo;

    @Before
    public void init() {
        conferenceService = new ConferenceServiceImpl(conferenceRepo);
    }

    @Test
    public void findAllConference() {
        Conference conference =
                new Conference("name", "topic", LocalDate.now(), 200);

        when(conferenceRepo.findAll()).thenReturn(singletonList(conference));

        assertThat(conferenceService.findAll(), hasItem(conference.toDtoGetResponse()));
    }

    @Test
    public void findAllConferenceReturnEmptyList() {
        when(conferenceRepo.findAll()).thenReturn(emptyList());

        assertThat(conferenceService.findAll(), is(empty()));
    }

    @Test
    public void addConferenceSuccess() {
        Long id = 1L;
        Conference dbConference = new Conference("name", "topic", LocalDate.now(), 100);
        dbConference.setId(id);

        when(conferenceRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
        when(conferenceRepo.findByDateStart(notNull())).thenReturn(emptyList());
        when(conferenceRepo.save(notNull())).thenReturn(dbConference);

        assertThat(conferenceService.addConference(dbConference.toDtoAddRequest()), is(id));
    }

    @Test(expected = ExistsConferenceNameException.class)
    public void addConferenceIfNameExists() {
        String name = "Курсы языковой школы MasterCLASS";
        Conference dbConference = new Conference(name, "topic", LocalDate.now(), 100);

        when(conferenceRepo.findByNameIgnoreCase(name)).thenReturn(Arrays.asList(dbConference));

        conferenceService.addConference(dbConference.toDtoAddRequest());
    }

    @Test(expected = DateConferenceIsBusyException.class)
    public void addConferenceIfDateBusy() {
        LocalDate date = LocalDate.now();
        Conference dbConference = new Conference("name", "topic", date, 100);

        when(conferenceRepo.findByDateStart(date)).thenReturn(Arrays.asList(dbConference));

        conferenceService.addConference(dbConference.toDtoAddRequest());
    }

    @Test
    public void editConferenceSuccess() {
        Long id = 1L;
        ConferenceDtoAddRequest newConference = new ConferenceDtoAddRequest("nameNew", "topicNew", LocalDate.now(), 200);
        Conference dbConference = new Conference("name", "topic", LocalDate.now(), 100);

        when(conferenceRepo.findById(id)).thenReturn(java.util.Optional.of(dbConference));
        when(conferenceRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
//        when(conferenceRepo.findByDateStart(notNull())).thenReturn(emptyList());

        //TODO  что детектить если возвращаем void
        conferenceService.editConference(id, newConference);
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void editConferenceIfUnknownId() {
        ConferenceDtoAddRequest newConference = new ConferenceDtoAddRequest("nameNew", "topicNew", LocalDate.now(), 200);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.empty());

        conferenceService.editConference(1L, newConference);
    }

    @Test(expected = ExistsConferenceNameException.class)
    public void editConferenceIfNameExists() {
        ConferenceDtoAddRequest newConference = new ConferenceDtoAddRequest("newName", "topicNew", LocalDate.now(), 200);
        Conference dbConference = new Conference("name", "topic", LocalDate.now(), 100);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(conferenceRepo.findByNameIgnoreCase(notNull())).thenReturn(Arrays.asList(dbConference));

        conferenceService.editConference(1L, newConference);
    }

    @Test(expected = DateConferenceIsBusyException.class)
    public void editConferenceIfDateBusy() {
        ConferenceDtoAddRequest newConference = new ConferenceDtoAddRequest("newName", "topicNew",
                LocalDate.of(2020, 1, 1), 200);
        Conference dbConference = new Conference("name", "topic",
                LocalDate.of(2020, 4, 9), 100);

        when(conferenceRepo.findById(notNull())).thenReturn(Optional.of(dbConference));
        when(conferenceRepo.findByNameIgnoreCase(notNull())).thenReturn(emptyList());
        when(conferenceRepo.findByDateStart(notNull())).thenReturn(Arrays.asList(dbConference));

        conferenceService.editConference(1L, newConference);
    }
}
