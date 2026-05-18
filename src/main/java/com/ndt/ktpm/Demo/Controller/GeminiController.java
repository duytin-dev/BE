package com.ndt.ktpm.Demo.Controller;

import com.ndt.ktpm.Demo.Domain.Request.GeminiPromptRequest;
import com.ndt.ktpm.Demo.Domain.Response.GeminiReplyResponse;
import com.ndt.ktpm.Demo.Service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class GeminiController {

	private final GeminiService geminiService;

	public GeminiController(GeminiService geminiService) {
		this.geminiService = geminiService;
	}

	@PostMapping("/ai/gemini")
	public ResponseEntity<?> chat(@RequestBody GeminiPromptRequest body) {
		if (body == null || body.prompt() == null || body.prompt().isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("error", "prompt không được để trống"));
		}
		try {
			String text = geminiService.generate(body.prompt().trim());
			return ResponseEntity.ok(new GeminiReplyResponse(text));
		} catch (IllegalStateException e) {
			if (e.getMessage() != null && e.getMessage().contains("GEMINI_API_KEY")) {
				return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
						.body(Map.of("error", e.getMessage()));
			}
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", e.getMessage()));
		}
	}
}
