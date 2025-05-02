package io.leeseunghee.fcfsentrytest.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.leeseunghee.fcfsentrytest.repository.EntryMemberRepository;
import io.leeseunghee.fcfsentrytest.repository.EntryRepository;
import io.leeseunghee.fcfsentrytest.repository.PremierTicketCountRepository;
import io.leeseunghee.fcfsentrytest.repository.PremiereRepository;
import io.leeseunghee.fcfsentrytest.service.dto.SaveEntryRequest;

@SpringBootTest
class EntryServiceTest {

	@Autowired
	private EntryService entryService;

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private PremiereRepository premiereRepository;

	@Autowired
	private EntryMemberRepository entryMemberRepository;

	@Autowired
	private PremierTicketCountRepository premierTicketCountRepository;

	@Test
	void optimistic_선착순_50명_동시_접속_100명_일_때() throws InterruptedException {
	    // given -- 테스트의 상태 설정
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when -- 테스트하고자 하는 행동
		for (int i = 0; i < threadCount; i++) {
			long memberId = i;
			executorService.submit(() -> {
				try {
					entryService.saveEntry(new SaveEntryRequest(memberId, 1L));
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		// Thread.sleep(1000);

		long count = entryRepository.count();

		// then -- 예상되는 변화 및 결과
		assertThat(count).isEqualTo(50);
	}

	@Test
	void distribute_선착순_50명_동시_접속_100명_일_때() throws InterruptedException {
	    // given -- 테스트의 상태 설정
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when -- 테스트하고자 하는 행동
		for (int i = 0; i < threadCount; i++) {
			long memberId = i + 1;
			executorService.submit(() -> {
				try {
					entryService.saveEntry2(new SaveEntryRequest(memberId, 1L));
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		long count = entryRepository.count();

		// then -- 예상되는 변화 및 결과
		assertThat(count).isEqualTo(50);
	}
}