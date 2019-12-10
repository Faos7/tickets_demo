package com.faos7.tickets;

import com.faos7.tickets.model.Route;
import com.faos7.tickets.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TicketsApplicationTests {

	@Autowired
	RouteRepository routeRepository;

	@Test
	public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
		List<Route> routes = routeRepository.findAll();
		assertEquals(routes.size(), 4);
	}

}
