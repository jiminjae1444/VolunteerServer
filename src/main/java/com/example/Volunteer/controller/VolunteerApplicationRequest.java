package com.example.Volunteer.controller;

public class VolunteerApplicationRequest {
        private Long volunteerFormId;
        private Long userId;

        // 추가 필요한 필드들 (봉사 신청과 관련된 정보들)

        public Long getVolunteerFormId() {
            return volunteerFormId;
        }

        public void setVolunteerFormId(Long volunteerFormId) {
            this.volunteerFormId = volunteerFormId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        // 추가 getter 및 setter 메서드

    }

