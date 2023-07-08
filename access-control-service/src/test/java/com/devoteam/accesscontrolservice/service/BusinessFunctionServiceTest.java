package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapperImpl;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Import(BusinessFunctionMapperImpl.class)
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

    @DisplayName("Test saving a new business function successfully")
    @Test
    void createOrRetrieveBusinessFunction_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(creationRequest.getApplicationName(), creationRequest.getFunctionName())).willReturn(Optional.empty());
        given(businessFunctionRepository.save(ArgumentMatchers.any())).willReturn(businessFunction);

        BusinessFunctionCreationResponse savedBusinessFunctionResponse = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);

        Assertions.assertThat(savedBusinessFunctionResponse).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedBusinessFunctionResponse.getId()).isEqualTo(businessFunction.getId());
    }

    @DisplayName("Test retrieving an existing business function successfully")
    @Test
    void createOrRetrieveBusinessFunction_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(creationRequest.getApplicationName(), creationRequest.getFunctionName())).willReturn(Optional.of(businessFunction));

        BusinessFunctionCreationResponse savedBusinessFunctionResponse = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);

        Assertions.assertThat(savedBusinessFunctionResponse).isNotNull();
        Assertions.assertThat(savedBusinessFunctionResponse.getId()).isEqualTo(businessFunction.getId());
    }
}