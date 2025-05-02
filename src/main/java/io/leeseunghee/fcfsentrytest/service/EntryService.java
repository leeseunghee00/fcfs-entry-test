package io.leeseunghee.fcfsentrytest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.leeseunghee.fcfsentrytest.domain.Entry;
import io.leeseunghee.fcfsentrytest.domain.Premiere;
import io.leeseunghee.fcfsentrytest.lock.DistributeLock;
import io.leeseunghee.fcfsentrytest.repository.EntryMemberRepository;
import io.leeseunghee.fcfsentrytest.repository.EntryRepository;
import io.leeseunghee.fcfsentrytest.repository.PremierTicketCountRepository;
import io.leeseunghee.fcfsentrytest.repository.PremiereRepository;
import io.leeseunghee.fcfsentrytest.service.dto.SaveEntryRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryService {

	private final EntryRepository entryRepository;
	private final PremiereRepository premiereRepository;

	private final EntryMemberRepository entryMemberRepository;
	private final PremierTicketCountRepository ticketCountRepository;

	/* redis */
	@Transactional
	public void saveEntry(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findById(request.premiereId()).orElseThrow();

		boolean isEntered = entryMemberRepository.isAlreadyEntered(request.memberId().toString());

		if (isEntered) {
			return;
		}

		Long ticketCount = ticketCountRepository.increment();

		if (ticketCount > premiere.getMaxQuantity()) {
			return;
		}

		entryMemberRepository.add(request.memberId());

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}

	/* redisson 적용 */
	@DistributeLock(key = "#request.premiereId()")
	public void saveEntry2(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findById(request.premiereId()).orElseThrow();

		boolean isEntered = entryRepository.existsEntryByPremiereIdAndMemberId(request.premiereId(), request.memberId());

		if (isEntered) {
			return;
		}

		long ticketCount = ticketCountRepository.increment();

		if (ticketCount > premiere.getMaxQuantity()) {
			return;
		}

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}
}
