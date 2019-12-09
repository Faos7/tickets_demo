package com.faos7.tickets.model;

import com.faos7.tickets.constraints.CustomRoute;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
public class ExternalCreateRequest {
    @NonNull
    @CustomRoute
    Integer number;
    @NonNull
    Instant departureTime;
}
