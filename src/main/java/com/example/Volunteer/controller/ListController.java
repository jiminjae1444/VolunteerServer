package com.example.Volunteer.controller;


import com.example.Volunteer.model.VolunteerList;
import com.example.Volunteer.repository.VolunteerListRepository;
import com.example.Volunteer.service.VolunteerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/volunteer/list")
public class ListController {

    @Autowired
    private VolunteerListService volunteerListService;
    @Autowired
    private VolunteerListRepository volunteerListRepository;

    private List<VolunteerList> volunteerList = new ArrayList<>();

    @GetMapping("/getVolunteerList")
    public List<VolunteerList> getVolunteerList() {
        // VolunteerListRepository에서 모든 데이터를 가져오기
        List<VolunteerList> allVolunteerList = volunteerListRepository.findAll();
        return allVolunteerList;
    }
}

//        // 현재 날짜를 가져오는 코드 (Java 8 이상에서 제공)
//        LocalDate currentDate = LocalDate.now();
//
//        // 마감일이 지나지 않은 봉사 리스트 필터링
//        List<VolunteerList> openVolunteerList = new ArrayList<>();
//        for (VolunteerList volunteer : allVolunteerList) {
//            LocalDate endDate = volunteer.getVolunteerForm().getEnd_date();
//            if (endDate != null && endDate.isAfter(currentDate)) {
//                openVolunteerList.add(volunteer);
//            }
//        }
//        return openVolunteerList;
//    }
        // 기타 필요한 엔드포인트 및 메서드 추가 가능