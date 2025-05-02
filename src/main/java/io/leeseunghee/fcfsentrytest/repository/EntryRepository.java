package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.leeseunghee.fcfsentrytest.domain.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

	boolean existsEntryByPremiereIdAndMemberId(Long premierId, Long memberId);

	int countEntryByPremiereId(Long premiereId);
}
