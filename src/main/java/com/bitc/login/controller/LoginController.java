package com.bitc.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitc.login.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
//	사용자가 login 페이지에 접속하여 id/pw 를 입력
//	login 양식 페이지
//	웹페이지와 매핑시키기
	@RequestMapping(value="/login/login", method=RequestMethod.GET)
	public String login() throws Exception {
		return "/login/login";
	}
	
//	서버의 controller에서 service 를 이용하여 db에 저장된 유저 정보를 조회
//	login id 체크를 하는 페이지(loginCheck)
	@RequestMapping(value="/login/loginCheck", method=RequestMethod.POST)
//	매개변수 받을 때 dto 파일 만들어서 dto 타입으로 받아도 되고, @RequestParam 으로 만들어서 받아도 된다. 아래의 userId와 userPw는 login.html에서 name="userId", name="userPw"로 보낸 것과 이름을 똑같이 적어줘야한다. 아니면 오류난다. 
//	HttpServletRequest는 요청한 모든 정보를 다 담고 있다. 여기 안에 세션 정보도 들고 있어서, 세션이 살아있는지 확인이 가능해서 여기서 사용함.
	public String loginCheck(@RequestParam String userId, @RequestParam String userPw, HttpServletRequest request) throws Exception {
//		입력받은 정보를 바탕으로 DB에 연결하여 해당 id와 pw를 가지고 있는 사용자가 있는지 조회함.
		int count = loginService.selectUserInfoYn(userId, userPw); // 워크밴치에서 member 테이블 만들 때 user_id 부분에 UQ 유니크를 넣었기 때문에, id를 검색하면 동일하면 1, 아니면 0 으로 무조건 0 , 1로만 나와서 int count로 넣어줬다.

		
		if (count == 1) {
//			웹은 각각의 페이지로 이동할 때마다 기본적으로 모든 데이터가 초기화됨
//			다른 페이지로 이동 후에도 데이터를 유지하고 있으려면 세션 및 쿠키에 데이터를 저장해야 함
//			중요 정보는 세션에 저장
			// 조회된 결과값을 바탕으로 결과가 존재하면 세션에 등록
//			HttpSession 클래스를 사용하면 웹 페이지의 세션에 데이터를 쉽게 저장하거나 가져올 수 있음
//			HttpSevletRequest 클래스는 서버로 요청된 데이터에 대한 내용을 모두 가지고 있음
//			-> 그래서 HttpSevletRequest에 세션에 대한 정보가 들어 있음
//			getSession() : 세션 가져오기
//			setAttribute(세션명, 데이터) : 세션에 지정한 이름으로 데이터 저장하기
//			getAttribute(세션명) : 세션에 저장된 데이터 가져오기
//			setMaxInactiveInterval(시간) : 지정한 시간동안 세션이 살아있음 (초 단위)
			
//			현재 서버에 접속한 클라이언트의 세션 정보를 가져옴
			HttpSession session = request.getSession();
//			이름 지정해서 값을 넣을 수 있음
			session.setAttribute("userId", userId);
//			30초 동안 세션이 살아있음
			session.setMaxInactiveInterval(30); // 세션 보존 시간 설정
			
//			세션은 해당 서버에서 로그아웃(세션삭제)전까지 계속 서버에 남아있음, 그래서 그 해당 서버에 어느 사이트, 브라우저를 이용해서 들어가도 계속 남아있는 것을 확인 할 수 있음. 
			
			
			return "redirect:/login/loginOK";
		}
		else {
			// 결과가 없으면 loginFail 페이지로 이동
			return "redirect:/login/loginFail";
		}
	}
	
//	조회 결과가 존재할 경우 loginOK 페이지로 이동
	@RequestMapping(value="/login/loginOK", method=RequestMethod.GET)
	public String loginOK(HttpServletRequest request) throws Exception {
		
//		HttpSession session = request.getSession();
		
		return "/login/loginOK";
		
	}
	
//	조회 결과가 존재하지 않을 경우 오류 메시지 출력 후 loginFail 페이지로 이동 
	@RequestMapping(value="/login/loginFail", method=RequestMethod.GET)
	public String loginFail() throws Exception {
		return "/login/loginFail";
	}
	
	@RequestMapping(value="/login/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest request) throws Exception {
		
//		세션을 받아와서 삭제하는 부분 필요 - 위에 loginCheck 에서 만들었던 session.setAttribute로 받아온 세션을 loginOK에서 매개변수 HttpServletRequest 타입으로 받아오는 것이 존재해야한다. 
//		즉 서버 클라1(웹브라우저) 클라2 클라3 이렇게 있다고 하면. 클라이언트에서 서버에 접속하려고 할 때마다 id가 새로 생성된다. 그래서 loginOK에서 HttpServletRequest request 의 매개변수로 받아와서 그것을 여기 logout에 가져와서 삭제를 한다.
//		=> 즉 loginCheck 에서 세션 정보 받은 것을 loginOK에 저장, 또 그 정보를 logout에 넘겨받아서 저장, 삭제하는 작업
		
//		세션 정보 받아오기
		HttpSession session = request.getSession();
		
//		세션에 저장된 내용을 지정하여 삭제하기
		session.removeAttribute("userId");
//		모든 세션 정보를 삭제
		session.invalidate();
		
		return "/login/logout";
	}
	
	@RequestMapping(value="/login/sessionTest", method=RequestMethod.GET)
	public String sessionTest() throws Exception {
		return "/login/sessionTest";
	}
	
//	매 페이지 이동 시마다 로그인 여부를 확인 (인터셉터로 확인)
	
}









