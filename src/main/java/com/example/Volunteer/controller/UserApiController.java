package com.example.Volunteer.controller;

import com.example.Volunteer.model.User;
import com.example.Volunteer.repository.UserRepository;
import com.example.Volunteer.service.UserService;
import com.example.Volunteer.service.VolunteerApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
public class UserApiController {
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VolunteerApplicationService volunteerApplicationService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();

        }
    }


    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        User existingUser = userService.findByUsername(username);

        if (existingUser != null) {
            // 변경된 필드만 업데이트
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }

            // username이 변경된 경우 중복 체크
            if (!username.equals(updatedUser.getUsername())) {
                User userWithNewUsername = userService.findByUsername(updatedUser.getUsername());
                if (userWithNewUsername != null) {
                    // 이미 사용 중인 username이면 에러 응답
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(existingUser);
                }
                existingUser.setUsername(updatedUser.getUsername());
            }

            // 업데이트된 사용자 정보 저장
            userRepository.save(existingUser);

            // 업데이트된 사용자 정보를 반환
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/updateExpiredForms")
    public void updateExpiredForms() {
        volunteerApplicationService.updateVolunteerHoursForExpiredForms();
    }
    @PostMapping("/updateVolunteerGrade")
    public void updateVolunteerGrade() {
        volunteerApplicationService.updateVolunteerGradeForAllUsers();
    }
}


