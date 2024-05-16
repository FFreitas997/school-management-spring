package org.example.schoolmanagementsystemspring.school.mapper;

import org.example.schoolmanagementsystemspring.school.dto.SchoolResponse;
import org.example.schoolmanagementsystemspring.school.entity.School;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SchoolResponseMapper implements Function<School, SchoolResponse> {

    @Override
    public SchoolResponse apply(School school) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .type(school.getSchoolType())
                .email(school.getEmail())
                .phoneNumber(school.getPhoneNumber())
                .facebookURL(school.getFacebookURL())
                .instagramURL(school.getInstagramURL())
                .address(school.getAddress())
                .city(school.getCity())
                .zipCode(school.getZipCode())
                .build();
    }
}
