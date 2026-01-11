package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddActivityRequest;
import com.timesheet.management.entity.Activity;
import com.timesheet.management.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddActivityUseCaseImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private AddActivityUseCaseImpl addActivityUseCase;

    @Test
    void execute_shouldReturnError_whenMissingFields() {
        AddActivityRequest req = AddActivityRequest.builder().code(null).name(null).build();
        var resp = addActivityUseCase.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldReturnError_whenCodeExists() {
        AddActivityRequest req = AddActivityRequest.builder().code(10).name("Dev").build();
        when(activityRepository.existsById(10)).thenReturn(true);
        var resp = addActivityUseCase.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldCreateActivity_whenValid() {
        AddActivityRequest req = AddActivityRequest.builder().code(20).name("BA").description("Business Analysis").build();
        Activity saved = new Activity();
        saved.setCode(20);
        saved.setName("BA");
        saved.setDescription("Business Analysis");
        when(activityRepository.existsById(20)).thenReturn(false);
        when(activityRepository.save(any(Activity.class))).thenReturn(saved);

        var resp = addActivityUseCase.execute(req);
        assertThat(resp.isStatus()).isTrue();
        verify(activityRepository).save(any(Activity.class));
    }
}

