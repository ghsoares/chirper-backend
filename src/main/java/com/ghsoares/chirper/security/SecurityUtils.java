package com.ghsoares.chirper.security;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtils {
	public static Optional<UserDetailsImpl> getUserDetails() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			return Optional.of((UserDetailsImpl)principal);
		}
		return Optional.empty();
	}
	
	public static String encodeString(String string) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(string);
	}

	public static boolean compareStrings(String s1, String s2) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(s1, s2);
	}

	public static String generateBasicToken(String user, String password) {
		String token = user + ":" + password;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}
}
