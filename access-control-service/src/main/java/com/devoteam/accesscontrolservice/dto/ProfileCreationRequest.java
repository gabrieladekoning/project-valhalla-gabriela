package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreationRequest {
    @NotBlank(message = "Profile name is required")
    private String name;
}
