package io.leeseunghee.fcfsentrytest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.leeseunghee.fcfsentrytest.service.PremiereService;
import io.leeseunghee.fcfsentrytest.service.dto.SavePremiereRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PremiereController {

	private final PremiereService premiereService;

	@PostMapping("/premieres")
	public ResponseEntity<?> createPremiere(@RequestBody SavePremiereRequest request) {
		premiereService.savePremiere(request);
		return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
	}
}
