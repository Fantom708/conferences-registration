package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.dto.ConferenceDtoAddRequest;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.service.ConferenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ConferenceController.class)
public class ConferenceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConferenceService conferenceService;


    private LocalDate date = LocalDate.of(2020, 4, 10);
    private ConferenceDtoAddRequest conference = new ConferenceDtoAddRequest("name", "topic", date, 150);


    private ResultActions addPostRequest(String... lines) throws Exception {
        return mockMvc.perform(post("/conferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Stream.of(lines)
                        .collect(joining(",\n", "{\n", "\n}"))
                )
        );
    }

    private ResultActions addPostRequest(ConferenceDtoAddRequest conference) throws Exception {
        return mockMvc.perform(post("/conferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\n\"name\": \"%s\",\n" +
                                "\"topic\": \"%s\",\n" +
                                "\"dateStart\": \"%s\",\n" +
                                "\"amountParticipants\": %d\n}",
                        conference.getName(), conference.getTopic(),
                        conference.getDateStart(), conference.getAmountParticipants()))
        );
    }

    private ResultActions addPutRequest(ConferenceDtoAddRequest conference) throws Exception {
        return mockMvc.perform(put("/conferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\n\"name\": \"%s\",\n" +
                                "\"topic\": \"%s\",\n" +
                                "\"dateStart\": \"%s\",\n" +
                                "\"amountParticipants\": %d\n}",
                        conference.getName(), conference.getTopic(),
                        conference.getDateStart(), conference.getAmountParticipants()))
        );
    }

    @Test
    public void addConferencesSuccess() throws Exception {
        Long id = 1L;
        when(conferenceService.addConference(refEq(conference))).thenReturn(id);

        addPostRequest(conference)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id));

        verify(conferenceService).addConference(refEq(conference));
    }

    @Test
    public void addConferencesIfExistsConferenceName() throws Exception {
        when(conferenceService.addConference(refEq(conference)))
                .thenThrow(new ExistsConferenceNameException("ERROR"));

        addPostRequest(conference)
                .andExpect(status().isConflict());
    }

    @Test
    public void addConferencesIfDateConferenceIsBusy() throws Exception {
        when(conferenceService.addConference(refEq(conference)))
                .thenThrow(new DateConferenceIsBusyException(date));

        addPostRequest(conference)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfIncorrectRequest() throws Exception {
        addPostRequest("\"name\": \"\"",
                "\"topic\": \"topic\"",
                "\"dateStart\": \"2020-04-10\"",
                "\"amountParticipants\": 100")
                .andExpect(status().isBadRequest());

        addPostRequest("\"name\": \"name\"",
                "\"dateStart\": \"2020-04-10\"",
                "\"amountParticipants\": 100")
                .andExpect(status().isBadRequest());

        addPostRequest("\"name\": \"name\"",
                "\"topic\": \"topic\"",
                "\"dateStart\": \"\"",
                "\"amountParticipants\": 100")
                .andExpect(status().isBadRequest());

        addPostRequest("\"name\": \"name\"",
                "\"topic\": \"topic\"",
                "\"dateStart\": \"2020-04-10\"",
                "\"amountParticipants\": 99")
                .andExpect(status().isBadRequest());
    }


    @Test
    public void editConferencesSuccess() throws Exception {
        addPostRequest(conference)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        conferenceService.editConference(1L, conference);
    }

    @Test
    public void editConferencesIfConferenceNotFound() throws Exception {
        Long id = 1L;
        doThrow(ConferenceNotFoundException.class)
                .when(conferenceService)
                .editConference(id, conference);

        addPutRequest(conference)
                .andExpect(status().isConflict());

        conferenceService.editConference(id, conference);
    }
}
