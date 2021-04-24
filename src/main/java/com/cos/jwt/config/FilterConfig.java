package com.cos.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cos.jwt.filter.MyFilter01;
import com.cos.jwt.filter.MyFilter02;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<MyFilter01> filter1() {
		FilterRegistrationBean<MyFilter01> bean = new FilterRegistrationBean<>(new MyFilter01());
		bean.addUrlPatterns("/*");
		bean.setOrder(0); //낮은 번호가 필터중에 가장 먼저 실행된다.
		return bean;
	}
	@Bean
	public FilterRegistrationBean<MyFilter02> filter2() {
		FilterRegistrationBean<MyFilter02> bean = new FilterRegistrationBean<>(new MyFilter02());
		bean.addUrlPatterns("/*");
		bean.setOrder(1); //낮은 번호가 필터중에 가장 먼저 실행된다.
		return bean;
	}
}
