package io.leeseunghee.fcfsentrytest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.leeseunghee.fcfsentrytest.service.EntryService;
import io.leeseunghee.fcfsentrytest.service.dto.SaveEntryRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EntryController {

	private final EntryService entryService;

	@PostMapping("/entry")
	public ResponseEntity<?> createEntry(@RequestBody SaveEntryRequest request) {
		entryService.saveEntry2(request);
		return ResponseEntity.ok("Entry Success");
	}
}
