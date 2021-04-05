package com.bitc.login.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

// HandlerInterceptorAdapter 클래스는 삭제되었기 때문에 HandlerInterceptor 인터페이스를 사용
// preHandle() : 지정한 컨트롤러가 동작하기 직전에 먼저 수행
// postHandle() : 지정한 컨트롤러가 동작 후 View를 표시하기 직전에 수행
// afterCompletion() : 모든 동작이 완료된 후 수행

public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		현재 클라이언트 정보를 받아와서 세션 생성
		HttpSession session = request.getSession();
		
		if ((String)session.getAttribute("userId") == null) {
			System.out.println("===============iterceptor=============");
			System.out.println("비 로그인 상태 : ");
			System.out.println((String)session.getAttribute("userId"));
			
			response.sendRedirect("/login/loginFail");
			return false;
		}
		else {
			System.out.println("===============iterceptor=============");
			System.out.println("로그인 상태 : ");
			System.out.println((String)session.getAttribute("userId"));
			
			session.setMaxInactiveInterval(600);
			return true;
		}
	}
}
