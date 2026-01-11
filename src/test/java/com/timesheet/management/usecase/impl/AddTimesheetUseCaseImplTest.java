package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddTimesheetRequest;
import com.timesheet.management.dto.TimesheetEntryDTO;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddTimesheetUseCaseImplTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private AddTimesheetUseCaseImpl impl;

    @Test
    void execute_shouldReturnError_whenMissingFields() {
        AddTimesheetRequest req = AddTimesheetRequest.builder().contractorId(null).startDate(null).entries(null).build();
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldReturnError_whenContractorNotFound() {
        AddTimesheetRequest req = AddTimesheetRequest.builder().contractorId(10).startDate(LocalDate.now()).entries(Arrays.asList(new TimesheetEntryDTO())).build();
        when(userRepository.findById(10)).thenReturn(Optional.empty());
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldCreateTimesheet_whenValid() {
        TimesheetEntryDTO e = TimesheetEntryDTO.builder().projectId(45).activityCode(10).date(LocalDate.now()).hoursWorked(BigDecimal.valueOf(8)).comments("work").build();
        AddTimesheetRequest req = AddTimesheetRequest.builder().contractorId(1).startDate(LocalDate.now()).entries(Arrays.asList(e)).build();

        User u = new User(); u.setId(1);
        Project p = new Project(); p.setId(45);

        when(userRepository.findById(1)).thenReturn(Optional.of(u));
        when(projectRepository.findById(45)).thenReturn(Optional.of(p));
        when(activityRepository.findById(10)).thenReturn(Optional.of(new com.timesheet.management.entity.Activity()));

        Timesheet saved = new Timesheet(); saved.setId(200); saved.setContractor(u); saved.setStartDate(req.getStartDate());
        when(timesheetRepository.save(org.mockito.ArgumentMatchers.any(Timesheet.class))).thenReturn(saved);

        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getTimesheet()).isNotNull();
        assertThat(resp.getTimesheet().getId()).isEqualTo(200);
    }
}

