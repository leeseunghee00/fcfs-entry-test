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
public class Premiere {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer amount;

	private Integer maxQuantity;

	@Builder
	public Premiere(Integer amount, Integer maxQuantity) {
		this.amount = amount;
		this.maxQuantity = maxQuantity;
	}
}
