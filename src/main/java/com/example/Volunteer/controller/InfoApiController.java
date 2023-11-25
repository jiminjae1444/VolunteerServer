package com.example.Volunteer.controller;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-info")
public class InfoApiController {
    @Autowired
    private InfoService infoService;


    @GetMapping("/{username}")
    public ResponseEntity<Info> getInfoByUsername(@PathVariable String username) {
        Info info = infoService.findByUsername(username);

        if (info != null) {
            return ResponseEntity.ok(info);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
