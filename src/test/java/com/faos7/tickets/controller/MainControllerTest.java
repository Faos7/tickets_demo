package com.faos7.tickets.controller;

import com.faos7.tickets.model.Route;
import com.faos7.tickets.model.Status;
import com.faos7.tickets.repository.RouteRepository;
import com.faos7.tickets.service.InternalRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @MockBean
    private InternalRequestService requestService;

    @MockBean
    RouteRepository routeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        Route route = new Route();
        route.setNumber(1);
        Optional<Route> optionalRoute = Optional.of(route);
        when(routeRepository.findById(1)).thenReturn(optionalRoute);
        when(requestService.createRequest(anyInt(),any(Instant.class))).thenReturn(null);
        when(requestService.createRequest(eq(1), any(Instant.class))).thenReturn(1L);
        when(requestService.checkStatus(anyLong())).thenReturn(null);
        when(requestService.checkStatus(1L)).thenReturn(Status.SUCCESS);
        when(requestService.checkStatus(2L)).thenReturn(Status.PROCESSING);
        when(requestService.checkStatus(3L)).thenReturn(Status.ERROR);
        when(requestService.remoteStatus()).thenReturn(Status.SUCCESS);

    }

    @Test
    public void whenPostRequestToApplyAndValidRequest_thenCorrectResponse() throws Exception {

        String reqBody_ok = "{\"number\":1, \"departureTime\": \"2014-01-01T23:28:56.782Z\"}";

        mockMvc.perform(post("/apply")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody_ok))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

    @Test
    public void whenPostRequestToApplyAndInValidRequest_NoRouteInDb_thenCorrectResponse() throws Exception {

        String reqBody = "{\"number\":92, \"departureTime\": \"2014-01-01T23:28:56.782Z\"}";

        mockMvc.perform(post("/apply")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenPostRequestToApplyAndInValidRequest_NoRouteNumber_thenCorrectResponse() throws Exception {

        String reqBody = "{\"departureTime\": \"2014-01-01T23:28:56.782Z\"}";

        mockMvc.perform(post("/apply")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenPostRequestToApplyAndInValidRequest_NoDepartureTime_thenCorrectResponse() throws Exception {

        String reqBody = "{\"number\":92}";

        mockMvc.perform(post("/apply")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenPostRequestToApplyAndInValidRequest_EmptyRequest_thenCorrectResponse() throws Exception {

        mockMvc.perform(post("/apply")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenPostRequestToCheckAndValidRequest_expectSUCCESS_thenCorrectResponse() throws Exception {
        String reqBody = "{\"id\":1}";

        mockMvc.perform(post("/check")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"SUCCESS\""));
    }

    @Test
    public void whenPostRequestToCheckAndValidRequest_expectERROR_thenCorrectResponse() throws Exception {
        String reqBody = "{\"id\":3}";

        mockMvc.perform(post("/check")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"ERROR\""));
    }

    @Test
    public void whenPostRequestToCheckAndValidRequest_expectPROCESSING_thenCorrectResponse() throws Exception {
        String reqBody = "{\"id\":2}";

        mockMvc.perform(post("/check")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"PROCESSING\""));
    }

    @Test
    public void whenPostRequestToCheckAndInValidRequest_noSuchInternalRequest_thenCorrectResponse() throws Exception {
        String reqBody = "{\"id\":5}";

        mockMvc.perform(post("/check")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenPostRequestToCheckAndInValidRequest_emptyRequest_thenCorrectResponse() throws Exception {
        mockMvc.perform(post("/check")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testStatusUpdateEndpoint() throws Exception {
       mockMvc.perform(post("/status/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"SUCCESS\""));
    }
}
