package com.example.demo.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.example.demo.entity.user.Role;

@Component
@Mapper
public interface RoleMapper {
	Role getRoleInfo(@Param("role") String role);
}
