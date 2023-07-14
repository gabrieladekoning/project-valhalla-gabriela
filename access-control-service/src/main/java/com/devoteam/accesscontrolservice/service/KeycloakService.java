package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.exception.UserCreationFailedException;
import com.devoteam.accesscontrolservice.mapper.KeycloakUserMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KeycloakService {
    private Keycloak keycloak;
    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret).build();
    }

    public String createUser(ApplicationUserCreationRequest creationRequest) {
        log.debug("Creating user in Keycloak: {}", creationRequest.getEmail());
        UserRepresentation user = KeycloakUserMapper.INSTANCE.mapToUserRepresentation(creationRequest);
        user.setEnabled(true);

        var realmResource = keycloak.realm(realm);
        var usersResource = realmResource.users();
        var response = usersResource.create(user);

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw new UserCreationFailedException("Failed to create user in Keycloak, HTTP status: " + response.getStatus());
        }

        var userId = String.valueOf(UUID.fromString(CreatedResponseUtil.getCreatedId(response)));
        var passwordCred = createCredentialRepresentation(creationRequest.getPassword());
        var userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCred);
        log.debug("User created in Keycloak with ID: {}", userId);
        return userId;
    }

    private CredentialRepresentation createCredentialRepresentation(String password) {
        var passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }
}