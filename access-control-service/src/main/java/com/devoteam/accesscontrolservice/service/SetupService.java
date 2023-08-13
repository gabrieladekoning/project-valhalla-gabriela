package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.SetupAllRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetupService {
    private final UserService userService;
    private final BusinessFunctionService businessFunctionService;

    public void setupAll(SetupAllRequest setupAllRequest) {
        log.info("Setting up based on application name: '{}', function name: '{}', permission name: '{}', profile name: '{}', and email: '{}'",
                setupAllRequest.getApplicationName(), setupAllRequest.getFunctionName(), setupAllRequest.getPermissionName(), setupAllRequest.getProfileName(), setupAllRequest.getEmail());

        log.info("Finding user by email");
        userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail());
        log.info("Creating or retrieving business function");
        upsertBusinessFunction(setupAllRequest);
        log.info("Creating or retrieving permission");


    }

    private BusinessFunction upsertBusinessFunction(SetupAllRequest setupAllRequest) {
        var businessFunctionCreationRequest = new BusinessFunctionCreationRequest(
                setupAllRequest.getApplicationName(),
                setupAllRequest.getFunctionName());
        return businessFunctionService.createOrRetrieveBusinessFunction(businessFunctionCreationRequest);
    }

}