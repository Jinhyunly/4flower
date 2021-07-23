package com.example.demo.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.example.demo.entity.user.User;

@Component
@Mapper
public interface UserMapper {
	User findUserByLoginId(@Param("loginId") String loginId);
  int setUserInfo(@Param("param") User param);

}
