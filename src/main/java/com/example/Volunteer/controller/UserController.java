package com.example.Volunteer.controller;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.Organization;
import com.example.Volunteer.model.User;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.OrganizationRepository;
import com.example.Volunteer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private InfoRepository infoRepository;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> AuthRequest) {
        String username = AuthRequest.get("username");
        String password = AuthRequest.get("password");
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (password.equals(user.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "로그인 성공");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "로그인 실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "사용자를 찾을 수 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        try {
            if(userRepository.existsByUsername(signupRequest.getUsername())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("이미 사용 중인 아이디입니다.");
            }
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setPassword(signupRequest.getPassword());
            user.setName(signupRequest.getName());
            user.setPhoneNumber(signupRequest.getPhoneNumber());
            user.setUserType(signupRequest.getUserType());
            userRepository.save(user);

            if("ORGANIZATION".equals(signupRequest.getUserType())) {
                Organization organization = new Organization();
                organization.setUser(user);
                organization.setOrg_name(signupRequest.getOrg_name());
                organization.setOrg_address(signupRequest.getOrg_address());
                organization.setOrg_number(signupRequest.getOrg_number());
                organizationRepository.save(organization);
            }
            Info newInfo = new Info();
            newInfo.setUser(user);
            newInfo.setGrade("초보자");
            newInfo.setTotal_hours(0);

            infoRepository.save(newInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }
}

