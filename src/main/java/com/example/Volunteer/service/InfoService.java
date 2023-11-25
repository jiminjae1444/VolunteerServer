package com.example.Volunteer.service;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.User;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoService {
    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private UserRepository userRepository;
    public Info findByUsername(String username) {
        // 해당 username을 가진 User를 찾아서 user_id를 통해 Info를 찾아옴
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return infoRepository.findByUser(user);
        }
        return null;
    }

    public Info findById(Long id) {
        return infoRepository.findById(id).orElse(null);
    }
    public Info getInfoById(Long infoId) {
        Optional<Info> infoOptional = infoRepository.findById(infoId);

        if (infoOptional.isPresent()) {
            return infoOptional.get();
        } else {
            // 사용자 ID에 해당하는 사용자 정보를 찾을 수 없을 때 처리할 내용
            throw new UserNotFoundException("사용자 정보를 찾을 수 없습니다. ID: " + infoId);
        }
    }
}

