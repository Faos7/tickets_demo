package com.faos7.tickets.controller;

import com.faos7.tickets.model.ExternalCreateRequest;
import com.faos7.tickets.model.Status;
import com.faos7.tickets.service.InternalRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    InternalRequestService requestService;

    @PostMapping("/check")
    public ResponseEntity<Status> check(@RequestBody Map<String, Object> req)
            throws Exception {
        try {
            Long id = Long.parseLong(req.get("id").toString());
            Status resp = requestService.checkStatus(id);
            return resp != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();

        } catch (Exception e){
            LOGGER.error("Can't parse", e);
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/status/update")
    public ResponseEntity<Status> update() throws Exception {
        return ResponseEntity.ok(requestService.remoteStatus());
    }

    @PostMapping("/apply")
    public ResponseEntity<Long> apply(@Valid @RequestBody ExternalCreateRequest req) throws Exception {
        try {
            Long resp = requestService.createRequest(req.getNumber(), req.getDepartureTime());
            return resp != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();

        } catch (Exception e){
            LOGGER.error("Can't parse", e);
            return ResponseEntity.notFound().build();
        }
    }
}
