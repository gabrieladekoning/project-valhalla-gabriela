package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessFunctionServiceTest {
    @Mock
    BusinessFunctionRepository businessFunctionRepository;
    @InjectMocks
    BusinessFunctionService businessFunctionService;
    private BusinessFunctionCreationRequest creationRequest;
    private BusinessFunction businessFunction;
    private BusinessFunction secondBusinessFunction;

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
           secondBusinessFunction = BusinessFunction.builder()
                .id(uuid)
                .applicationName(creationRequest.getApplicationName())
                .functionName("Very creative name")
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

    @Test
    @DisplayName("Returning a pageable list of business functions when successful")
    void listAll_ReturnListOfBusinessFunctionsInsidePageObject_WhenSuccessful() {
        List<BusinessFunction> expectedBusinessFunctions = new ArrayList<>();
        expectedBusinessFunctions.add(businessFunction);
        expectedBusinessFunctions.add(secondBusinessFunction);

        var pageable = PageRequest.of(0, 5);
        var businessFunctionPage = new PageImpl<>(expectedBusinessFunctions, pageable, expectedBusinessFunctions.size());

        when(businessFunctionRepository.findAll(pageable)).thenReturn(businessFunctionPage);

        var resultBusinessFunctionPage = businessFunctionService.listAll(pageable);

        verify(businessFunctionRepository).findAll(pageable);

        assertThat(resultBusinessFunctionPage).isNotNull();
        assertThat(resultBusinessFunctionPage.toList()).isNotEmpty();
        assertThat(resultBusinessFunctionPage.toList().get(0).getFunctionName()).isEqualTo(businessFunction.getFunctionName());
        assertThat(resultBusinessFunctionPage.toList().get(1).getFunctionName()).isEqualTo(secondBusinessFunction.getFunctionName());
    }
}