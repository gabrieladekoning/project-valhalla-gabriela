package com.devoteam.accesscontrolservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "application_users")
public class ApplicationUser {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID applicationUserId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @OneToMany(mappedBy = "applicationUser")
    private List<UserProfile> profiles;
}