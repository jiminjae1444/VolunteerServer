package com.example.Volunteer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login","/signup","/api/volunteer/create","/api/user/by-username/{username}","/api/user-info/{usernam}","/api/volunteer/list/create","/api/user/{username}","/api/volunteer/list/getVolunteerList","/api/volunteer/volunteerForms","/api/volunteer/getVolunteerForm/{volunteerFormId}","/api/volunteer-applications/info/{infoId}/applications","/api/volunteer-applications/{applicationId}","/api/user/updateVolunteerGrade","/api/user/updateExpiredForms","/api/volunteer-applications/apply").permitAll()
                .anyRequest().authenticated();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
