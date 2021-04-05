package com.bitc.login.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

//	프로그램 개발 시 모든 매개변수를 dto 타입으로 사용할 수 없기 때문에, 필요한 매개변숨만 묶어서 map(HashMap) 타입으로 사용
//	@Param("변수명") 어노테이션을 사용하여 mapper와 연동된 xml에서 parameterType에 map으로 선언하여 매개변수를 사용
//	dto 타입 아니고 그냥 할거면 아래와 같이 @Param 넣어줘야함
	int selectUserInfoYn(@Param("userId") String userId, @Param("userPw") String userPw) throws Exception;
}
