package com.example.Volunteer.service;

import com.example.Volunteer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자를 username을 기반으로 데이터베이스에서 검색합니다.
        com.example.Volunteer.model.User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        // 사용자 정보를 UserDetails 객체로 변환합니다.
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // 사용자의 권한 정보를 설정할 수 있습니다.
                .build();
    }
}
