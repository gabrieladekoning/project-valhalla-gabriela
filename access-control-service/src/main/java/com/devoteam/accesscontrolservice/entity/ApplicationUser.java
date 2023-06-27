package com.devoteam.accesscontrolservice.entity;

import jakarta.persistence.*;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @NotBlank(message = "First name is required")
    @Column(name = "first_name")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Column(name = "email")
    private String email;
}
