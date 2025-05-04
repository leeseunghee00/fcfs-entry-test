package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PremierTicketCountRepository {

	private final RedisTemplate<String, Integer> redisTemplate;
	private static final String prefix = "premiere_ticket_count:";

	public PremierTicketCountRepository(RedisTemplate<String, Integer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Long increment(Long premiereId) {
		String key = prefix + premiereId.toString();

		return redisTemplate
			.opsForValue()
			.increment(key);
	}

	public void decrement(Long premiereId) {
		String key = prefix + premiereId.toString();

		redisTemplate
			.opsForValue()
			.decrement(key);
	}
}
