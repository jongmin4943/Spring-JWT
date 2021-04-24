package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); //내 서버가 응답 할 때 json을 js에서 처리할 수 있게 할지 설정
		config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용하겠따
		config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠따
		config.addAllowedMethod("*"); // 모든 타입의 요청을 허용하겠다.
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}
}
