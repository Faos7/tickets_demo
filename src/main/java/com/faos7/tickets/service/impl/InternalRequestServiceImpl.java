package com.faos7.tickets.service.impl;

import com.faos7.tickets.model.InternalRequest;
import com.faos7.tickets.model.Status;
import com.faos7.tickets.repository.InternalRequestRepository;
import com.faos7.tickets.repository.RouteRepository;
import com.faos7.tickets.service.InternalRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Service
public class InternalRequestServiceImpl implements InternalRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalRequestService.class);

    @Autowired
    private InternalRequestRepository internalRequestRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Long createRequest(Integer routeId, Instant departureTime) {
        if (routeRepository.findById(routeId).isPresent()){
            InternalRequest request = new InternalRequest();
            request.setStatus(Status.PROCESSING);
            request.setDepartureTime(departureTime);
            request.setRouteId(routeId);
            return internalRequestRepository.save(request).getId();
        }
        LOGGER.info(String.format("Route with number %s not found", routeId));
        return null;
    }

    @Override
    public Status checkStatus(Long requestId) {
        LOGGER.info(String.format("Checking status of request with number %s", requestId));
        Optional<InternalRequest> internalRequest = internalRequestRepository.findById(requestId);
        return internalRequest.map(InternalRequest::getStatus).orElseGet(() -> {
            LOGGER.info(String.format("Request with number %s not found", requestId));
            return null;
        });
    }

    @Override
    public Status remoteStatus() {
        if (Math.random() < 0.4){
            return Status.SUCCESS;
        } else if (Math.random() > 0.6){
            return Status.ERROR;
        }
        return Status.PROCESSING;
    }

    /**
     * Did not use @Async in assumption that previous task may least for too long or thread pool may be exceeded.
     * In case that task does not depend on others (like log file is generated every minute, and then send to some other service, async task can be used)
     */
    @Override
    @Scheduled(cron = "0 * * * * ?")
    public void processInternalRequests() {
        LOGGER.debug("ProcessInternalRequests invoked");
        final String uri = "http://localhost:8080/status/update";

        Collection<Optional<InternalRequest>> processingRequests = internalRequestRepository.findManyByStatus(Status.PROCESSING);
        for (Optional<InternalRequest> optionalRequest: processingRequests) {
            if(optionalRequest.isPresent()) {
                InternalRequest request = optionalRequest.get();
                try {
                    LOGGER.info(String.format("Attempting to update status of request with id %s", request.getId()));
                    request.setStatus(restTemplate.postForEntity(uri, null, Status.class).getBody());
                    LOGGER.info(String.format("New status of request with id %s is %s", request.getId(), request.getStatus()));
                } finally {
                    internalRequestRepository.save(request);
                }
            }
        }
    }


}
