package com.ndt.ktpm.Demo.Service;

import java.util.List;

public interface GeminiService {

	String generate(String prompt);

	/** Các model id (vd gemini-2.0-flash) mà key hiện tại có thể dùng với generateContent. */
	List<String> listGenerateContentModelIds();
}
