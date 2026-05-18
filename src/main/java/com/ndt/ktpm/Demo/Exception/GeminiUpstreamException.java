package com.ndt.ktpm.Demo.Exception;

/**
 * Lỗi trả về từ API Gemini (HTTP khác 200).
 */
public final class GeminiUpstreamException extends RuntimeException {

	private final int statusCode;

	public GeminiUpstreamException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
