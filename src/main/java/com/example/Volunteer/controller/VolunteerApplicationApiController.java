//package com.example.Volunteer.controller;
//
//import com.example.Volunteer.model.Info;
//import com.example.Volunteer.model.User;
//import com.example.Volunteer.model.VolunteerApplication;
//import com.example.Volunteer.model.VolunteerForm;
//import com.example.Volunteer.service.InfoService;
//import com.example.Volunteer.service.UserService;
//import com.example.Volunteer.service.VolunteerApplicationService;
//import com.example.Volunteer.service.VolunteerFormService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.time.chrono.ChronoLocalDateTime;
//import java.util.Date;
//
//@RestController
//public class VolunteerApplicationApiController {
//    @Autowired
//    private VolunteerApplicationService volunteerApplicationService;
//    @Autowired
//    private InfoService infoService;
//    @Autowired
//    private VolunteerFormService volunteerFormService;
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/apply-volunteer")
//    public ResponseEntity<ApplicationResponse> applyForVolunteer(@RequestBody VolunteerApplication application) {
//            VolunteerForm volunteerForm = volunteerFormService.getVolunteerById(application.getVolunteerForm().getId());
//
//        Info info = infoService.getInfoById(application.getInfo().getId());
//
//        if (volunteerForm != null && info != null) {
//            if (volunteerForm.getPriority().equals("경력자만") && !info.getGrade().equals("경력자")) {
//                // 봉사가 경력자만을 위한 것이고 사용자가 경력자가 아닌 경우 거부
//                return ResponseEntity.ok(new ApplicationResponse(false, "봉사에 참여할 자격이 없습니다.", application.getId()));
//            } else {
//                // 봉사 시작일 및 마감일 확인
//                LocalDateTime currentDateTime = LocalDateTime.now();
//                if (currentDateTime.isBefore(ChronoLocalDateTime.from(volunteerForm.getStart_date())) || currentDateTime.isAfter(ChronoLocalDateTime.from(volunteerForm.getEnd_date()))) {
//                    // 시작일 전이나 마감일 후에는 거부
//                    return ResponseEntity.ok(new ApplicationResponse(false, "봉사 신청 기간이 아닙니다.", application.getId()));
//                } else {
//                    VolunteerApplicationService.applyForVolunteer(application.getVolunteerForm().getId(),application.getInfo().getId());
//                    return ResponseEntity.ok(new ApplicationResponse(true, "봉사 신청이 승인되었습니다.", application.getId()));
//                }
//            }
//        } else {
//            // 봉사나 사용자 정보가 없을 때
//            return ResponseEntity.ok(new ApplicationResponse(false, "봉사 정보 또는 사용자 정보를 찾을 수 없습니다.", application.getId()));
//        }
//    }
//}
