package com.faos7.tickets.service;

import com.faos7.tickets.model.Status;

import java.time.Instant;

public interface InternalRequestService {

    Long createRequest(Integer routeId, Instant departureTime);

    Status checkStatus(Long requestId);

    void ProcessInternalRequests();

    Status remoteStatus();

}
