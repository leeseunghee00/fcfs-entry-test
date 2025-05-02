package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.leeseunghee.fcfsentrytest.domain.Premiere;

public interface PremiereRepository extends JpaRepository<Premiere, Long> {
}
