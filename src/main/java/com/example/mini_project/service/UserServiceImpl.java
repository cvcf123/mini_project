package com.example.mini_project.service;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.dto.UserResultDto;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    // 패스워드 암호화를 위해 SecurityConfig 에 @Bean 설정되어 있는 PasswordEncoder DI
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResultDto insertUser(UserDto userDto) {
        UserResultDto userResultDto = new UserResultDto();

        try{
            User user = User.builder() // 영속화 X
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword())) // 패스워드 암호화 후 저장
                    .createdAt(userDto.getCreatedAt())
                    .build();
            User savedUser = userRepository.save(user); // 전달받은 User 객체는 save() 를 통해 영속화
            System.out.println(savedUser);

            userResultDto.setResult("success");
        }
        catch(Exception e) {
            e.printStackTrace();
            // 우리의 코드 진행에 영향을 미치지 않고, transaction 을 관리하는 관리자에게 현재 transaction 을 rollback 시켜달라는 정중한 요청
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            userResultDto.setResult("fail");
        }
        return userResultDto;

    }
}
