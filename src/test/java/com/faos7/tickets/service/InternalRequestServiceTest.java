package com.faos7.tickets.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.faos7.tickets.model.Route;
import com.faos7.tickets.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InternalRequestServiceTest {
    @Autowired
    RouteRepository routeRepository;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Route> routes = routeRepository.findAll();
        assertEquals(routes.size(), 4);
    }


}
