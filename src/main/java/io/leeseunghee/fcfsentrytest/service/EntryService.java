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

	private final EntryLuaService entryLuaService;

	/* synchronized */
	public synchronized void saveEntry_synchronized(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findById(request.premiereId())
			.orElseThrow();

		boolean isEntered = entryRepository.existsEntryByPremiereIdAndMemberId(request.premiereId(),
			request.memberId());

		if (isEntered) {
			return;
		}

		long ticketCount = entryRepository.countEntryByPremiereId(request.premiereId());

		if (ticketCount > premiere.getMaxQuantity()) {
			return;
		}

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}

	/* pessimistic lock */
	@Transactional
	public void saveEntry_pessimistic(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findWithPessimisticLockById(request.premiereId())
			.orElseThrow();

		boolean isEntered = entryRepository.existsEntryByPremiereIdAndMemberId(request.premiereId(),
			request.memberId());

		if (isEntered) {
			return;
		}

		long ticketCount = entryRepository.countEntryByPremiereId(request.premiereId());

		if (ticketCount > premiere.getMaxQuantity()) {
			return;
		}

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}

	/* optimistic lock */
	@Transactional
	public void saveEntry_optimistic(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findWithOptimisticLockById(request.premiereId())
			.orElseThrow();

		boolean isEntered = entryRepository.existsEntryByPremiereIdAndMemberId(request.premiereId(),
			request.memberId());

		if (isEntered) {
			return;
		}

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
		premiere.decrease();
	}

	/* redisson */
	@DistributeLock(key = "#request.premiereId()")
	public void saveEntry_redisson(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findById(request.premiereId())
			.orElseThrow();

		boolean isEntered = entryRepository.existsEntryByPremiereIdAndMemberId(request.premiereId(),
			request.memberId());

		if (isEntered) {
			return;
		}

		long ticketCount = ticketCountRepository.increment(premiere.getId());

		if (ticketCount > premiere.getMaxQuantity()) {
			ticketCountRepository.decrement(premiere.getId());
			return;
		}

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}

	/* redis */
	@Transactional
	public void saveEntry_redis(SaveEntryRequest request) {

		Premiere premiere = premiereRepository.findById(request.premiereId()).orElseThrow();

		boolean isEntered = entryMemberRepository.isAlreadyEntered(request.premiereId(), request.memberId());

		if (isEntered) {
			return;
		}

		Long ticketCount = ticketCountRepository.increment(premiere.getId());

		if (ticketCount > premiere.getMaxQuantity()) {
			return;
		}

		entryMemberRepository.add(request.premiereId(), request.memberId());

		Entry entry = Entry.builder()
			.memberId(request.memberId())
			.premiereId(request.premiereId())
			.build();

		entryRepository.save(entry);
	}

	/* redis + lua */
	@Transactional
	public void saveEntry_lua(SaveEntryRequest request) {
		Premiere premiere = premiereRepository.findById(request.premiereId()).orElseThrow();

		Long result = entryLuaService.tryEnter(
			premiere.getId(),
			request.memberId(),
			premiere.getMaxQuantity()
		);

		if (result == 1) {
			entryRepository.save(
				Entry.builder()
					.memberId(request.memberId())
					.premiereId(request.premiereId())
					.build()
			);
		}
	}
}
