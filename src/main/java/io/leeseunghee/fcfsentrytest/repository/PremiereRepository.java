package io.leeseunghee.fcfsentrytest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import io.leeseunghee.fcfsentrytest.domain.Premiere;
import jakarta.persistence.LockModeType;

public interface PremiereRepository extends JpaRepository<Premiere, Long> {

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Optional<Premiere> findWithPessimisticLockById(Long premiereId);

	@Lock(value = LockModeType.OPTIMISTIC)
	Optional<Premiere> findWithOptimisticLockById(Long premiereId);

}
