package com.example.Volunteer.repository;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface ApplicationRepository extends CrudRepository<VolunteerApplication,Long> {
    List<VolunteerApplication> findByVolunteerForm(Long volunteerFormId);


    List<VolunteerApplication> findByInfoId(Long infoId);

    Long countByVolunteerFormTitle(String volunteerFormTitle);

    boolean existsByInfoAndVolunteerForm(Info info, VolunteerForm volunteerForm);
//    @Modifying
//    @Transactional
//    @Query("UPDATE VolunteerApplication a SET a.status = '신청완료' WHERE a.volunteerForm.startDate < :currentDate")
//    void updateStatusForApplicationsWithStartDateInPast(@Param("currentDate") LocalDate currentDate);
@Query("SELECT va FROM VolunteerApplication va " +
        "JOIN va.volunteerForm vf " +
        "WHERE vf.start_date < :currentDate")
List<VolunteerApplication> findByStatusAndApplicationDateBefore(@Param("currentDate") LocalDate currentDate);
}
