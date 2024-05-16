package org.example.schoolmanagementsystemspring.school.mapper;

import org.example.schoolmanagementsystemspring.school.dto.RequestSchool;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SchoolMapper implements Function<RequestSchool, School> {

    @Override
    public School apply(RequestSchool requestSchool) {
        return School.builder()
                .name(requestSchool.name())
                .schoolType(requestSchool.type())
                .email(requestSchool.email())
                .phoneNumber(requestSchool.phoneNumber())
                .facebookURL(requestSchool.facebookURL())
                .instagramURL(requestSchool.instagramURL())
                .address(requestSchool.address())
                .city(requestSchool.city())
                .zipCode(requestSchool.zipCode())
                .build();
    }
}
