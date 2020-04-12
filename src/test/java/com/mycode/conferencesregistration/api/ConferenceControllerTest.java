package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.dto.ConferenceDtoGetResponse;
import com.mycode.conferencesregistration.service.ConferenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
public class ConferenceControllerTest {

    @Mock
    private ConferenceService conferenceService;

    private MockMvc mockMvc;


    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ConferenceController(conferenceService))
                .build();
    }

    @Test
    public void ifUnknownPage() throws Exception {
        mockMvc.perform(get("/unknown").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllConferencesSuccess() throws Exception {
        LocalDate date = LocalDate.of(2020, 4, 10);
        List<ConferenceDtoGetResponse> listConference = asList(
                new ConferenceDtoGetResponse(1L, "name1", "topic1", date, 100, null),
                new ConferenceDtoGetResponse(2L, "name2", "topic2", date, 200, null));
        when(conferenceService.findAll()).thenReturn(listConference);

        mockMvc.perform(get("/conferences").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].topic").value("topic1"))
                .andExpect(jsonPath("$[0].dateStart").value(date.toString()))
                .andExpect(jsonPath("$[0].amountParticipants").value(100))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].topic").value("topic2"))
                .andExpect(jsonPath("$[1].dateStart").value(date.toString()))
                .andExpect(jsonPath("$[1].amountParticipants").value(200));
    }

}
