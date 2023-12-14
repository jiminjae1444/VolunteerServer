package com.example.Volunteer.controller;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.model.VolunteerList;
import com.example.Volunteer.repository.VolunteerFormRepository;
import com.example.Volunteer.repository.VolunteerListRepository;
import com.example.Volunteer.service.VolunteerFormService;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/api/volunteer")
public class VolunteerFormApiController {
    @Autowired
    private VolunteerFormRepository volunteerFormRepository;
    @Autowired
    private VolunteerFormService volunteerFormService;
    @Autowired
    private VolunteerListRepository volunteerListRepository;
    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createVolunteer(@RequestBody VolunteerForminfo volunteerForminfo) {
        try {

            VolunteerForm volunteerForm1 = new VolunteerForm();
            volunteerForm1.setTitle(volunteerForminfo.getTitle());
            volunteerForm1.setSlots(parseInt(volunteerForminfo.getSlots()));
            volunteerForm1.setStart_date(LocalDate.parse(volunteerForminfo.getStart_date()));
            volunteerForm1.setEnd_date(LocalDate.parse(volunteerForminfo.getEnd_date()));
            volunteerForm1.setRecruitment_hours(parseInt(volunteerForminfo.getRecruitment_hours()));
            volunteerForm1.setPriority(volunteerForminfo.getPriority());
            volunteerForm1.setLocation(volunteerForminfo.getLocation());
            volunteerForm1.setDescription(volunteerForminfo.getDescription());
            volunteerFormRepository.save(volunteerForm1);

            VolunteerList list = new VolunteerList();
            list.setVolunteerForm(volunteerForm1);
            list.setVolunteerName(volunteerForminfo.getTitle());
            list.setVolunteerPersons(parseInt(volunteerForminfo.getSlots()));
            list.setVolunteerHour(parseInt(volunteerForminfo.getRecruitment_hours()));
            volunteerListRepository.save(list);

            System.out.println(volunteerForminfo.getPriority());
            return ResponseEntity.ok("모집글이 생성되었습니다.");
        } catch (DateTimeParseException e) {
            // 날짜 형식이 올바르지 않을 때 사용자에게 메시지 표시
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바른 날짜 형식을 입력하십시오.");
        } catch (Exception e) {
            // 그 외의 예외 발생 시에 대한 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }

}
    @GetMapping("/volunteerForms")
    public List<VolunteerForm> getVolunteerForm() {
        return volunteerFormRepository.findAll();
    }

    @GetMapping("/find-id-by-title")
    public ResponseEntity<Long> findVolunteerFormIdByTitle(@RequestParam String title) {
        Long formId = volunteerFormRepository.findIdByTitle(title);
        return ResponseEntity.ok(formId);
    }
    @GetMapping("/getByTitle/{title}")
    public ResponseEntity<VolunteerForm> getVolunteerFormByTitle(@PathVariable String title) {
        VolunteerForm volunteerForm = volunteerFormRepository.findByTitle(title);

        if (volunteerForm != null) {
            return new ResponseEntity<>(volunteerForm, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getVolunteerForm/{volunteerFormId}")
    public ResponseEntity<VolunteerForm> getVolunteerForm(@PathVariable long volunteerFormId) {
        VolunteerForm volunteerForm = volunteerFormService.getVolunteerFormById(volunteerFormId);
        return ResponseEntity.ok(volunteerForm);
    }
    @GetMapping("/experienced-forms-starting-two-days-before")
    public List<VolunteerForm> getExperiencedFormsStartingTwoDaysBefore() {
        return volunteerFormService.getExperiencedFormsStartingTwoDaysBefore();
    }
}

