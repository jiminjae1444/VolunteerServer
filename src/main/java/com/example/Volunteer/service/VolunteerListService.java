package com.example.Volunteer.service;

import com.example.Volunteer.controller.VolunteerEvent;
import com.example.Volunteer.model.VolunteerList;
import com.example.Volunteer.repository.VolunteerListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerListService {

    @Autowired
    private static VolunteerListRepository volunteerListRepository;

    public static List<VolunteerList> getListsByVolunteerFormTitle(String title) {
        return volunteerListRepository.findByVolunteerForm_Title(title);
    }
}