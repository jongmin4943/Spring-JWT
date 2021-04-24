package com.cos.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username, password post로 전송하면 
// 이게 동작함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 시도중");
		
		//1.username,password받아서 정상인지 로그인 시도
		try {
//			BufferedReader br = request.getReader();
//			String input = null;
//			while((input=br.readLine()) != null) {
//				System.out.println(input);
//			}
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			//PrincipalDetailsService의 loadUserbyUsername() 함수가 실행됨.
			//authentication 객체가 리턴됬다는 뜻은 db와 결과 일치-> 인증됨s
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println("로그인완료 : "+principalDetails.getUser().getUsername());
			//authentication 객체가 session영역에 저장됨. -> 로그인이 되었다는 뜻이다.
			//리턴 해주는 이우는 권한관리를 security가 대신 해주기 때문에 편하려고 하는것
			//JWT토큰을 사용하면 세션을 만들 이유가 없지만 권한처리땜 session에 넣어준다.
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("========================");
		
		//2.authenticationManager로 로그인 시도시 PrinciaplDetailsService가 호출되고
		// loadUserByUsername()함수가 실행되고 PrincipalDetails 반환.
		//3.PrincipalDetails 를 세션에 담고 (권한 관리를위해)
		//4.JWT토큰을 만들어서 응답해줌
		return null;
	}
	
	//위 함수인 attemptAuthentication 실행 후 인증이 정상적으로 되면 밑 함수인 successfulAuthentication가 실행
	//JWT 토큰을 만들어서 request한 요청자에게 jwt토큰을 response해주면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		//Hash암호방식
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())//토큰이름
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))//만료시간
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		response.addHeader("Authorization",JwtProperties.TOKEN_PREFIX+jwtToken);
	}
}
