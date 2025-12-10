package com.example.mini_project.config;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.myapp.user.entity.User;
import com.mycom.myapp.user.entity.UserRole;
import com.mycom.myapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// entity.User 와 SpringSecurity 의 User 가 클래스 이름이 동일. 주의!!!
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{

	// SecurityConfig 에서 DI 설정
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	
	// 이전 버전
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		// UserRepository 를 이용
//		Optional<User> optionalUser = userRepository.findByEmail(email);
//		
//		if(optionalUser.isPresent()) {
//			User user = optionalUser.get(); // 영속화
//			List<UserRole> listUserRole = user.getUserRoles();
//			List<String> roleStrList = new ArrayList<>();
//			listUserRole.forEach( userRole -> roleStrList.add(userRole.getName()));
//			String[] roleStrArray = roleStrList.toArray(new String[0]);
//			
//			return org.springframework.security.core.userdetails.User.builder()
//					.username(user.getEmail())
//					.password(user.getPassword())
//					.roles(roleStrArray) // String[] 로 권한의 이름들만 전달하지만, 내부적으로 GrantedAuthority 로 변환 처리. (ROLE_ prefix)
//					.build();
//		}else {
//			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
//		}
//	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// UserRepository 를 이용
		Optional<User> optionalUser = userRepository.findByEmail(email);
		
		if(optionalUser.isPresent()) {
			User user = optionalUser.get(); // 영속화
			List<UserRole> listUserRole = user.getUserRoles();
			
			// 이전 코드에서 security 의 User 객체를 이용할 때는 Role 이름만으로 문자열 배열로 전달. 내부적으로 GrantedAuthority 전환
			// MyUserDetails 객체를 사용하려면 직접 변환 처리
			List<SimpleGrantedAuthority> authorities = 
					listUserRole.stream()
						.map(UserRole::getName)
						.map( name -> "ROLE_" + name) // Role 의 이름에 "ROLE_" prefix 
						.map(SimpleGrantedAuthority::new)
						.toList();
			
			return MyUserDetails.builder()
					// 인증 처리
					.username(user.getEmail()) // 사용자가 로그인 시 이메일을 입력
					.password(user.getPassword())
					.authorities(authorities) // roles() 대신 직접 GrantedAuthority 에 authorities 필드 적용
					// 추가 정보
					.id(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.build();
		}else {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
	}
}























