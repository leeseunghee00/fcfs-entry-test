package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PremierTicketCountRepository {

	private final RedisTemplate<String, Integer> redisTemplate;

	public PremierTicketCountRepository(RedisTemplate<String, Integer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Long increment() {
		return redisTemplate
			.opsForValue()
			.increment("premiere_ticket_count");
	}
}
