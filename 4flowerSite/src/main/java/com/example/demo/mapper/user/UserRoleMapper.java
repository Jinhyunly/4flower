package com.example.demo.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.example.demo.entity.user.UserRole;

@Component
@Mapper
public interface UserRoleMapper {
	void setUserRoleInfo(@Param("param") UserRole param);
}
