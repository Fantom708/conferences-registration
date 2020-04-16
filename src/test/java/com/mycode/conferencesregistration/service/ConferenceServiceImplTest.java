package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.dao.ConferenceDao;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceUpdateRequest;
import com.mycode.conferencesregistration.exception.ConferenceDateBusyException;
import com.mycode.conferencesregistration.exception.ConferenceNameExistsException;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import org.assertj.core.api.Assertions;
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
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
public class ConferenceServiceImplTest {

    private ConferenceService service;

    @Mock
    private ConferenceDao dao;

    private Long id = 1L;
    private LocalDate date = LocalDate.of(2020, 4, 10);
    private Conference dbConference = new Conference(
            "name", "topic", date, 100);
    private ConferenceCreationRequest newConference = new ConferenceCreationRequest(
            "name", "topic", date, 100);
    private ConferenceUpdateRequest updConference = new ConferenceUpdateRequest("nameNew", "topicNew", date, 200);

    @Before
    public void init() {
        service = new ConferenceServiceImpl(dao);
    }

    @Test
    public void findAllConference() {
        when(dao.findAll()).thenReturn(singletonList(dbConference));

        Assertions.assertThat(service.findAll()).containsOnly(dbConference.toDto());
    }

    @Test
    public void findAllConferenceReturnEmptyList() {
        when(dao.findAll()).thenReturn(emptyList());

        Assertions.assertThat(service.findAll()).hasSize(0);
    }

    @Test
    public void addConferenceSuccess() {
        dbConference.setId(id);

        when(dao.findByNameIgnoreCase("name")).thenReturn(emptyList());
        when(dao.findByDateStart(date)).thenReturn(emptyList());
        when(dao.save(refEq(dbConference, "id"))).thenReturn(dbConference);

        Assertions.assertThat(service.addConference(newConference)).isEqualTo(id);
    }

    @Test(expected = ConferenceNameExistsException.class)
    public void addConferenceIfNameExists() {
        when(dao.findByNameIgnoreCase("name")).thenReturn(Arrays.asList(dbConference));

        service.addConference(newConference);
    }

    @Test(expected = ConferenceDateBusyException.class)
    public void addConferenceIfDateBusied() {
        when(dao.findByDateStart(date)).thenReturn(Arrays.asList(dbConference));

        service.addConference(newConference);
    }

    @Test
    public void editConferenceSuccess() {
        when(dao.findById(id)).thenReturn(java.util.Optional.of(dbConference));
        when(dao.findByNameIgnoreCase("nameNew")).thenReturn(emptyList());

        service.editConference(id, updConference);
    }

    @Test(expected = ConferenceNotFoundException.class)
    public void editConferenceIfConferenceNotFoundThenThrowException() {
        when(dao.findById(id)).thenReturn(Optional.empty());

        service.editConference(id, updConference);
    }

    @Test(expected = ConferenceNameExistsException.class)
    public void editConferenceIfNameExistsThenThrowException() {
        when(dao.findById(id)).thenReturn(Optional.of(dbConference));
        when(dao.findByNameIgnoreCase("nameNew")).thenReturn(Arrays.asList(dbConference));

        service.editConference(id, updConference);
    }

    @Test(expected = ConferenceDateBusyException.class)
    public void editConferenceIfDateBusyThenThrowException() {
        dbConference.setDateStart(LocalDate.of(2020, 1, 1));

        when(dao.findById(id)).thenReturn(Optional.of(dbConference));
        when(dao.findByDateStart(date)).thenReturn(Arrays.asList(dbConference));

        service.editConference(id, updConference);
    }
}
