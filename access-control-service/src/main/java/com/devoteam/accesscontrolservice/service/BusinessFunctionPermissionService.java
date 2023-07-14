package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionPermissionService {
    private final BusinessFunctionRepository businessFunctionRepository;
    private final PermissionRepository permissionRepository;
    private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

}