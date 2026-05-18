package com.ndt.ktpm.Demo.Service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ndt.ktpm.Demo.Service.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class GeminiServiceImpl implements GeminiService {

	private static final String GEMINI_URL_TEMPLATE =
			"https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

	private final String apiKey;
	private final String model;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(20))
			.build();

	public GeminiServiceImpl(
			@Value("${gemini.api.key:}") String apiKey,
			@Value("${gemini.model:gemini-1.5-flash}") String model) {
		this.apiKey = apiKey;
		this.model = model;
	}

	@Override
	public String generate(String prompt) {
		if (apiKey == null || apiKey.isBlank()) {
			throw new IllegalStateException("Chưa cấu hình GEMINI_API_KEY trên server.");
		}

		ObjectNode body = objectMapper.createObjectNode();
		ArrayNode contents = body.putArray("contents");
		ObjectNode content = contents.addObject();
		ArrayNode parts = content.putArray("parts");
		parts.addObject().put("text", prompt);

		String uri = String.format(GEMINI_URL_TEMPLATE, model, apiKey);
		String jsonBody;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (Exception e) {
			throw new IllegalStateException("Không tạo được JSON request", e);
		}

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(uri))
					.timeout(Duration.ofSeconds(120))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
			JsonNode root = objectMapper.readTree(response.body());

			if (response.statusCode() != 200) {
				String err = root.path("error").path("message").asText(response.body());
				throw new IllegalStateException("Gemini HTTP " + response.statusCode() + ": " + err);
			}

			JsonNode candidates = root.path("candidates");
			if (!candidates.isArray() || candidates.isEmpty()) {
				throw new IllegalStateException("Gemini không trả candidates: " + response.body());
			}

			JsonNode textNode = candidates.get(0).path("content").path("parts").path(0).path("text");
			if (textNode.isMissingNode() || textNode.asText().isEmpty()) {
				throw new IllegalStateException("Gemini không trả text.");
			}
			return textNode.asText();
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Lỗi gọi Gemini: " + e.getMessage(), e);
		}
	}
}
