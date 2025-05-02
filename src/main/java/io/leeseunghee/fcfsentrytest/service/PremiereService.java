package io.leeseunghee.fcfsentrytest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.leeseunghee.fcfsentrytest.domain.Premiere;
import io.leeseunghee.fcfsentrytest.repository.PremiereRepository;
import io.leeseunghee.fcfsentrytest.service.dto.SavePremiereRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PremiereService {

	private final PremiereRepository premiereRepository;

	@Transactional
	public void savePremiere(SavePremiereRequest request) {

		Premiere premiere = Premiere.builder()
			.amount(request.amount())
			.maxQuantity(request.maxQuantity())
			.build();

		premiereRepository.save(premiere);
	}
}
