package io.leeseunghee.fcfsentrytest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;		// 회원ID

	private Long premiereId;	// 시사회ID

	@Builder
	public Entry(Long memberId, Long premiereId) {
		this.memberId = memberId;
		this.premiereId = premiereId;
	}
}
