package io.leeseunghee.fcfsentrytest.service.dto;

public record SaveEntryRequest(
	Long memberId,
	Long premiereId
) {
}
