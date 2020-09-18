package com.auth.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.auth.entity.UserEntity;



public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	UserEntity findByUsername(String username);
	
	@Query("select user from UserEntity user where user.id = :id")
	UserEntity findUserById(long id);

}
