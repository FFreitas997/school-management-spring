package org.example.schoolmanagementsystemspring.school.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public class SchoolInformation {

    @Column(name = "school_email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", length = 9, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "facebook_url")
    private String facebookURL;

    @Column(name = "instagram_url")
    private String instagramURL;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "zipCode")
    private String zipCode;
}
