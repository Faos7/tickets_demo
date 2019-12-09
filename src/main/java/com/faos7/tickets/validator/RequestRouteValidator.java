package com.faos7.tickets.validator;

import com.faos7.tickets.constraints.CustomRoute;
import com.faos7.tickets.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class RequestRouteValidator implements ConstraintValidator<CustomRoute, Integer> {

    @Autowired
    RouteRepository routeRepository;

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return routeRepository.findById(integer).isPresent();
    }
}
