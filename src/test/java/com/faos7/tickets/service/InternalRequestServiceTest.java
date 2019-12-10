package com.faos7.tickets.service;

import com.faos7.tickets.model.InternalRequest;
import com.faos7.tickets.model.Status;
import com.faos7.tickets.repository.InternalRequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.faos7.tickets.model.Route;
import com.faos7.tickets.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InternalRequestServiceTest {

    @MockBean
    RouteRepository routeRepository;

    @MockBean
    InternalRequestRepository internalRequestRepository;

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    InternalRequestService requestService;
    InternalRequest request;

    @Before
    public void setUp() {

        Route route = new Route();
        route.setNumber(1);
        Optional<Route> optionalRoute = Optional.of(route);

        request = new InternalRequest();
        request.setStatus(Status.PROCESSING);
        request.setDepartureTime(Instant.now());
        request.setRouteId(1);
        request.setId(1L);

        ResponseEntity<Status> resp = ResponseEntity.ok(Status.SUCCESS);

        Collection<Optional<InternalRequest>> processingRequests = new ArrayList<>();
        processingRequests.add(Optional.of(request));


        when(routeRepository.findById(1)).thenReturn(optionalRoute);
        doReturn(request).when(internalRequestRepository).save(any(InternalRequest.class));
        when(internalRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(internalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(internalRequestRepository.findManyByStatus(Status.PROCESSING)).thenReturn(processingRequests);
        when(restTemplate.postForEntity("http://localhost:8080/status/update",null, Status.class)).thenReturn(resp);
    }


    @Test
    public void whenCreateRequests_thenOnlyProperStored(){
        long _id1 = requestService.createRequest(1, Instant.now()); //correct
        assertEquals(_id1, 1L);
        Long _id2 = requestService.createRequest(null, Instant.now()); //first arg null -> id should be null (call of routeRepository.findById(null) causes IllegalArgumentException  )
        assertNull(_id2);
        Long _id3 = requestService.createRequest(11, Instant.now()); //route with number 11 is not in db -> id should be null
        assertNull(_id3);
        long _id4 = requestService.createRequest(1, null);
        assertEquals(_id4, 1L);
    }


    @Test
    public void whenCheckStatus_thenReturnStatus(){
        assertEquals(Status.PROCESSING, requestService.checkStatus(1L));
        assertNull(requestService.checkStatus(2L));
    }

    @Test
    public void whenProcessInternalRequests_thenStatusOfRequestIsChanged(){
        assertEquals(Status.PROCESSING, request.getStatus());
        requestService.processInternalRequests();
        assertEquals(Status.SUCCESS, request.getStatus());
    }
}
