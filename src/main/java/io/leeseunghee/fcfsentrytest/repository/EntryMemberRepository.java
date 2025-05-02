package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntryMemberRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public EntryMemberRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean isAlreadyEntered(String memberId) {
		return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("entry_member", memberId));
	}

	public void add(Long memberId) {
		redisTemplate
			.opsForSet()
			.add("entry_member", memberId.toString());
	}
}
