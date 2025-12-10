package com.example.mini_project.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;

	private final String username;
	private final String password;
	
	private final Collection<? extends GrantedAuthority> authorities;
	
	private final Long id;
	private final String name;
	private final String email;
	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
