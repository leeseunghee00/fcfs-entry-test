package io.leeseunghee.fcfsentrytest.controller;

import java.io.IOException;

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

	// service 메서드만 바꿔서 테스트 진행
	@PostMapping("/entry")
	public ResponseEntity<?> createEntry(@RequestBody SaveEntryRequest request) throws IOException {
		entryService.saveEntry_lua(request);
		return ResponseEntity.ok("Entry Success");
	}
}
