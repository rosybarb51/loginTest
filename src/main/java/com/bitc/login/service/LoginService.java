package com.bitc.login.service;

public interface LoginService {

	int selectUserInfoYn(String userId, String userPw) throws Exception;
}
