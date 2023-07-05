package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.exception.UserCreationException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.UUID;

@Service
@Slf4j
public class KeycloakService {
    private final Keycloak keycloak;

    public KeycloakService() {
        String serverUrl = "http://localhost:8180/auth/";
        String realm = "application-users";
        String clientId = "access-control-client";
        String clientSecret = "";

        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username("")
                .password("")
                .build();
    }

    public String createUser(ApplicationUserCreationRequest userDto) {
        log.debug("Creating user in Keycloak: {}", userDto.getEmail());
        var user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // not sure if I can use var here
        RealmResource realmResource = keycloak.realm("application-users");
        UsersResource usersResource = realmResource.users();
        // and here
        Response response = usersResource.create(user);
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw new UserCreationException("Failed to create user in Keycloak, HTTP status: " + response.getStatus());
        }

        var userId = String.valueOf(UUID.fromString(CreatedResponseUtil.getCreatedId(response)));

        var passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDto.getPassword());

        var userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCred);
        log.debug("User created in Keycloak with ID: {}", userId);
        return userId;
    }
}
