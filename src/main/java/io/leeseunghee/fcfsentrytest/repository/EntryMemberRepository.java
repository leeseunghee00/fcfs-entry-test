package io.leeseunghee.fcfsentrytest.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntryMemberRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private static final String prefix = "entry_member:";

	public EntryMemberRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean isAlreadyEntered(Long premiereId, Long memberId) {
		String key = prefix + premiereId.toString();

		return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, memberId.toString()));
	}

	public void add(Long premiereId, Long memberId) {
		String key = prefix + premiereId;

		redisTemplate
			.opsForSet()
			.add(key, memberId.toString());
	}
}
