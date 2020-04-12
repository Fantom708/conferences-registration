package com.mycode.conferencesregistration.api;

import com.mycode.conferencesregistration.domain.ReportType;
import com.mycode.conferencesregistration.domain.dto.ReportDtoGetResponse;
import com.mycode.conferencesregistration.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Yurii Kovtun
 */
@RunWith(MockitoJUnitRunner.class)
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ReportController(reportService))
                .build();
    }

    @Test
    public void getAllReportsSuccess() throws Exception {
        Long id = 1L;
        ReportDtoGetResponse report = new ReportDtoGetResponse(
                id, "name", "description", ReportType.REPORT, "reporter");
        when(reportService.getReports(id)).thenReturn(new HashSet<ReportDtoGetResponse>() {{
            add(report);
        }});

        mockMvc.perform(get("/conferences/1/talks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].typeReport").value("REPORT"))
                .andExpect(jsonPath("$[0].reporter").value("reporter"));
    }

}
