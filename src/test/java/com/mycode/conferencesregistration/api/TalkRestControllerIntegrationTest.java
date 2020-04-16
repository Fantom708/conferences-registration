package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.Talk;
import com.mycode.conferencesregistration.domain.TypeTalk;
import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.domain.dto.TalkDto;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exception.LimitByNewTalkWasReachedException;
import com.mycode.conferencesregistration.exception.RegistrationDateWasLateException;
import com.mycode.conferencesregistration.exception.TalkNameExistsException;
import com.mycode.conferencesregistration.service.TalkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TalkRestController.class)
public class TalkRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TalkService service;

    private String requestMapping = "/conferences/%d/talks";
    private Long idConference = 1L;
    private Long idTalk = 5L;
    private TalkCreationRequest talk = new TalkCreationRequest("name", "description", TypeTalk.REPORT, "reporter");

    private String talkToJson(String name, String description, TypeTalk type, String reporter) {
        return String.format("{\n" +
                        "\"name\": \"%s\",\n" +
                        "\"description\": \"%s\",\n" +
                        "\"typeTalk\": \"%s\",\n" +
                        "\"reporter\": \"%s\"" +
                        "\n}",
                name, description, type, reporter);
    }

    private String talkToJson(TalkCreationRequest talk) {
        return talkToJson(talk.getName(), talk.getDescription(), talk.getTypeTalk(), talk.getReporter());
    }

    @Test
    public void getAllConferencesSuccess() throws Exception {
        when(service.getTalks(idConference)).thenReturn(
                new HashSet<TalkDto>() {{
                    add(new Talk("name", "description", TypeTalk.REPORT, "reporter").toDto());
                }});

        mockMvc.perform(get(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].typeTalk").value(TypeTalk.REPORT.toString()))
                .andExpect(jsonPath("$[0].reporter").value("reporter"));
    }

    @Test
    public void addTalkSuccess() throws Exception {
        when(service.addTalkToConference(eq(idConference), refEq(talk)))
                .thenReturn(idTalk);

        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson(talk)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idTalk));
    }

    @Test
    public void addTalkIfConferenceNotFound() throws Exception {
        when(service.addTalkToConference(eq(idConference), refEq(talk)))
                .thenThrow(new ConferenceNotFoundException(idConference));

        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson(talk)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addTalkIfNameExistsThenConflict() throws Exception {
        when(service.addTalkToConference(eq(idConference), refEq(talk)))
                .thenThrow(new TalkNameExistsException(talk.getReporter()));

        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson(talk)))
                .andExpect(status().isConflict());
    }

    @Test
    public void addTalkIfTalksLimitWasReachedThenBadRequest() throws Exception {
        when(service.addTalkToConference(eq(idConference), refEq(talk)))
                .thenThrow(new LimitByNewTalkWasReachedException(talk.getReporter(), 3));

        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson(talk)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTalkIfDateRegistrationLessThanAllowThenBadRequest() throws Exception {
        when(service.addTalkToConference(eq(idConference), refEq(talk)))
                .thenThrow(new RegistrationDateWasLateException(30));

        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson(talk)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTalkIfIncorrectNameThenBadRequest() throws Exception {
        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson("", "description", TypeTalk.WORKSHOP, "reporter")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTalkIfIncorrectDescriptionThenBadRequest() throws Exception {
        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson("name", "", TypeTalk.WORKSHOP, "reporter")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTalkIfIncorrectTypeThenBadRequest() throws Exception {
        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson("name", "description", null, "reporter")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTalkIfIncorrectReporterThenBadRequest() throws Exception {
        mockMvc.perform(post(String.format(requestMapping, idConference))
                .contentType(MediaType.APPLICATION_JSON)
                .content(talkToJson("name", "description", TypeTalk.WORKSHOP, "")))
                .andExpect(status().isBadRequest());
    }
}