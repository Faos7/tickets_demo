package com.faos7.tickets.repository;

import com.faos7.tickets.model.InternalRequest;
import com.faos7.tickets.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface InternalRequestRepository extends JpaRepository<InternalRequest, Long> {

    Collection<Optional<InternalRequest>> findManyByStatus(Status status);
}
