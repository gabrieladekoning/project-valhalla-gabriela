package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BusinessFunctionServiceTest {
    @Mock
    BusinessFunctionRepository businessFunctionRepository;
    @InjectMocks
    BusinessFunctionService businessFunctionService;
    private BusinessFunctionCreationRequest creationRequest;
    private BusinessFunction businessFunction;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        creationRequest = BusinessFunctionCreationRequest.builder()
                .applicationName("Application name")
                .functionName("Function name")
                .build();
        businessFunction = BusinessFunction.builder()
                .id(uuid)
                .applicationName(creationRequest.getApplicationName())
                .functionName(creationRequest.getFunctionName())
                .build();
    }

    @DisplayName("Saving a new business function successfully")
    @Test
    void createOrRetrieveBusinessFunction_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(creationRequest.getApplicationName(), creationRequest.getFunctionName())).willReturn(Optional.empty());
        given(businessFunctionRepository.save(ArgumentMatchers.any())).willReturn(businessFunction);

        var returnedBusinessFunction = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);

        assertThat(returnedBusinessFunction).hasNoNullFieldsOrProperties();
        assertThat(returnedBusinessFunction.getId()).isEqualTo(businessFunction.getId());
    }

    @DisplayName("Retrieving an existing business function successfully")
    @Test
    void createOrRetrieveBusinessFunction_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(creationRequest.getApplicationName(), creationRequest.getFunctionName())).willReturn(Optional.of(businessFunction));

        var returnedBusinessFunction = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);

        assertThat(returnedBusinessFunction).isNotNull();
        assertThat(returnedBusinessFunction.getId()).isEqualTo(businessFunction.getId());
    }

    @DisplayName("Handling when business function is not found")
    @Test
    void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFound() {
        given(businessFunctionRepository.findById(businessFunction.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionService.findByIdOrThrowNotFound(String.valueOf(businessFunction.getId())));
    }
}