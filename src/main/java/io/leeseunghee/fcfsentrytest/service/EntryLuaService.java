package io.leeseunghee.fcfsentrytest.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryLuaService {

	private final RedisScript<Long> redisScript = redisScript();
	private final RedisTemplate<String, String> redisTemplate;

	public Long tryEnter(Long premiereId, Long memberId, int maxQuantity) {
		String entryMemberKey = "entry_member:" + premiereId.toString();
		String ticketKey = "premiere_ticket_count:" + premiereId;
		String orderListKey = "entry_order:" + premiereId;

		List<String> keys = List.of(entryMemberKey, ticketKey, orderListKey);

		Long result = redisTemplate.execute(
			redisScript,
			keys,
			memberId.toString(),
			String.valueOf(maxQuantity),
			String.valueOf(System.currentTimeMillis())
		);

		return result;
	}

	private RedisScript<Long> redisScript() {
		String script = """
			local entryMemberKey = KEYS[1]
			local ticketKey = KEYS[2]
			local orderListKey = KEYS[3]
			
			local memberId = ARGV[1]
			local maxQuantity = tonumber(ARGV[2])
			local timestamp = tonumber(ARGV[3])
			
			if redis.call('SISMEMBER', entryMemberKey, memberId) == 1 then
			    return 0
			end
			
			local currentCount = tonumber(redis.call('INCR', ticketKey))
			if currentCount > maxQuantity then
			    return 0
			end
			
			redis.call('SADD', entryMemberKey, memberId)
			redis.call('RPUSH', orderListKey, memberId)
			
			return 1
			""";

		return RedisScript.of(script, Long.class);
	}
}