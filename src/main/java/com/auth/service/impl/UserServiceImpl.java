package com.auth.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.dao.UserRepository;
import com.auth.entity.UserEntity;
import com.auth.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        
        if (user == null) {
        	throw new RuntimeException("User not found: " + username);
        }
        
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleEntity().getRole_name());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(authority));
    }

	@Override
	public UserEntity save(UserEntity user) throws Exception {
		UserEntity userEntity = userRepository.findByUsername(user.getUsername());
		
		if (userEntity != null) {
			throw new RuntimeException("username is duplicated: " + user.getUsername());
		}
		
		user.setId(0);
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setStatus("active");
		return userRepository.save(user);
	}

	@Override
	public List<UserEntity> findAll() throws Exception {
		List<UserEntity> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(long id) throws Exception {
		userRepository.deleteById(id);
	}

	@Override
	public UserEntity findUserByUsername(String username) throws Exception {
		UserEntity entity = userRepository.findByUsername(username);
		if (entity == null) {
			throw new RuntimeException("username is Not found " + username);
		}
		return entity;
	}
	
	@Override
	public UserEntity update(UserEntity user) throws Exception {
		UserEntity entity = userRepository.findById(user.getId()).get();
		if (entity != null) {
			entity.setFirst_name(user.getFirst_name());
			entity.setLast_name(user.getLast_name());
			entity.setAddress(user.getAddress());
			entity.setTel(user.getTel());
			entity.setGender(user.getGender());
			userRepository.save(entity);
		}
		
		return entity;
	}
}
