package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter03 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// 토큰 : cos 를 만들어줘야함. id,pw가 정상적으로 들어와서 로그인이 완료되면
		// 토큰을 만들어 주고 그걸 응답해준다.
		// 요청할 때 마다 header에 Authorization 에 value값으로 토큰을 가지고 온다
		// 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증( RSA or HS256 )
		if (req.getMethod().equals("POST")) {
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			System.out.println("POST요청");
			System.out.println("필터3");

			if (headerAuth.equals("cos")) {
				chain.doFilter(req, res);
			} else {
				PrintWriter out = res.getWriter();
				out.println("인증안됨");
			}
		}
	}

}
