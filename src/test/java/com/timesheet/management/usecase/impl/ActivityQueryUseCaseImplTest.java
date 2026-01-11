package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.usecase.ActivityQueryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityQueryUseCaseImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityQueryUseCaseImpl impl;

    @Test
    void getByCode_shouldReturnError_whenNotFound() {
        when(activityRepository.findById(999)).thenReturn(Optional.empty());

        ActivityQueryUseCase.GetActivityResponse resp = impl.getByCode(999);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(ActivityQueryUseCase.ErrorCode.E_002);
    }

    @Test
    void getByCode_shouldReturnActivity_whenFound() {
        Activity a = new Activity();
        a.setCode(10);
        a.setName("Dev");
        when(activityRepository.findById(10)).thenReturn(Optional.of(a));

        ActivityQueryUseCase.GetActivityResponse resp = impl.getByCode(10);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getActivity()).isNotNull();
        assertThat(resp.getActivity().getCode()).isEqualTo(10);
    }

    @Test
    void listAll_shouldReturnList() {
        Activity a1 = new Activity(); a1.setCode(10); a1.setName("Dev");
        Activity a2 = new Activity(); a2.setCode(20); a2.setName("BA");
        when(activityRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        ActivityQueryUseCase.ListActivitiesResponse resp = impl.listAll();
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getActivities()).hasSize(2);
    }
}

