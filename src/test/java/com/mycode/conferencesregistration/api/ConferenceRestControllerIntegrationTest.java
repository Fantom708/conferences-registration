package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceUpdateRequest;
import com.mycode.conferencesregistration.exception.ConferenceDateBusyException;
import com.mycode.conferencesregistration.exception.ConferenceNameExistsException;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import com.mycode.conferencesregistration.service.ConferenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ConferenceRestController.class)
public class ConferenceRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConferenceService service;


    private String requestMapping = "/conferences";
    private Long id = 1L;
    private LocalDate date = LocalDate.of(2020, 4, 10);
    private ConferenceCreationRequest conference = new ConferenceCreationRequest("name", "topic", date, 150);
    private ConferenceUpdateRequest newConference = new ConferenceUpdateRequest("NewName", "NewTopic", date, 200);


    private String conferenceToJson(String name, String topic, LocalDate date, int participants) {
        return String.format("{\n" +
                        "\"name\": \"%s\",\n" +
                        "\"topic\": \"%s\",\n" +
                        "\"dateStart\": \"%s\",\n" +
                        "\"amountParticipants\": %d" +
                        "\n}",
                name, topic, date, participants);
    }

    private String conferenceToJson(ConferenceCreationRequest conference) {
        return conferenceToJson(conference.getName(), conference.getTopic(), conference.getDateStart(), conference.getAmountParticipants());
    }

    private String conferenceToJson(ConferenceUpdateRequest conference) {
        return conferenceToJson(conference.getName(), conference.getTopic(), conference.getDateStart(), conference.getAmountParticipants());
    }


    @Test
    public void getAllConferencesSuccess() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(
                new Conference("name", "topic", date, 300).toDto()));

        mockMvc.perform(get(requestMapping)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].topic").value("topic"))
                .andExpect(jsonPath("$[0].dateStart").value(date.toString()))
                .andExpect(jsonPath("$[0].amountParticipants").value(300));
    }

    @Test
    public void addConferencesSuccess() throws Exception {
        when(service.addConference(refEq(conference))).thenReturn(id);

        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson(conference)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void addConferencesIfNameExistsThenConflict() throws Exception {
        when(service.addConference(refEq(conference)))
                .thenThrow(new ConferenceNameExistsException("ERROR"));

        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson(conference)))
                .andExpect(status().isConflict());
    }

    @Test
    public void addConferencesIfDateBusiedThenBadRequest() throws Exception {
        when(service.addConference(refEq(conference)))
                .thenThrow(new ConferenceDateBusyException(date));

        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson(conference)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfIncorrectNameThenBadRequest() throws Exception {
        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson("", "topic", LocalDate.now(), 100)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfIncorrectTopicThenBadRequest() throws Exception {
        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson("name", "", LocalDate.now(), 100)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfIncorrectDateStartThenBadRequest() throws Exception {
        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson("name", "topic", null, 100)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addConferencesIfNumberOfParticipantsIsIncorrectThenBadRequest() throws Exception {
        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson("name", "topic", LocalDate.now(), 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editConferencesSuccess() throws Exception {
        mockMvc.perform(put(requestMapping + "/" + String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson(newConference)))
                .andExpect(status().isOk());
    }

    @Test
    public void editConferencesIfConferenceNotFoundThenConflict() throws Exception {
        doThrow(new ConferenceNotFoundException(id))
                .when(service)
                .editConference(eq(id), refEq(newConference));

        mockMvc.perform(put(requestMapping + "/" + String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceToJson(newConference)))
                .andExpect(status().isNotFound());
    }
}
