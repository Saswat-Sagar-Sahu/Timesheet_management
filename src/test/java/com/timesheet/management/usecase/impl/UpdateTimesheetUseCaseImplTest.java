package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetEntryDTO;
import com.timesheet.management.dto.UpdateTimesheetRequest;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.repository.TimesheetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTimesheetUseCaseImplTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private UpdateTimesheetUseCaseImpl impl;

    @Test
    void execute_replaceEntries_shouldReplace() {
        Timesheet t = new Timesheet(); t.setId(1); t.setStatus(Timesheet.Status.DRAFT);
        com.timesheet.management.entity.User u1 = new com.timesheet.management.entity.User(); u1.setId(1); t.setContractor(u1);
        TimesheetEntry existing = new TimesheetEntry(); existing.setId(10); existing.setDate(LocalDate.now());
        t.setEntries(List.of(existing));
        when(timesheetRepository.findById(1)).thenReturn(Optional.of(t));

        TimesheetEntryDTO dto = TimesheetEntryDTO.builder().projectId(45).activityCode(10).date(LocalDate.now()).hoursWorked(BigDecimal.valueOf(8)).comments("ok").build();
        UpdateTimesheetRequest req = UpdateTimesheetRequest.builder().timesheetId(1).entries(List.of(dto)).build();

        when(projectRepository.findById(45)).thenReturn(Optional.of(new Project()));
        when(activityRepository.findById(10)).thenReturn(Optional.of(new com.timesheet.management.entity.Activity()));
        when(timesheetRepository.save(org.mockito.ArgumentMatchers.any(Timesheet.class))).thenAnswer(i->i.getArgument(0));

        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getTimesheet().getEntries()).hasSize(1);
    }

    @Test
    void execute_patch_addAndRemove_shouldModify() {
        Timesheet t = new Timesheet(); t.setId(2); t.setStatus(Timesheet.Status.DRAFT);
        com.timesheet.management.entity.User u2 = new com.timesheet.management.entity.User(); u2.setId(2); t.setContractor(u2);
        TimesheetEntry existing = new TimesheetEntry(); existing.setId(20); existing.setDate(LocalDate.now());
        t.setEntries(List.of(existing));
        when(timesheetRepository.findById(2)).thenReturn(Optional.of(t));

        TimesheetEntryDTO add = TimesheetEntryDTO.builder().projectId(45).activityCode(10).date(LocalDate.now()).hoursWorked(BigDecimal.valueOf(4)).comments("new").build();
        UpdateTimesheetRequest req = UpdateTimesheetRequest.builder().timesheetId(2).addEntries(List.of(add)).removeEntryIds(List.of(20)).build();

        when(projectRepository.findById(45)).thenReturn(Optional.of(new Project()));
        when(activityRepository.findById(10)).thenReturn(Optional.of(new com.timesheet.management.entity.Activity()));
        when(timesheetRepository.save(org.mockito.ArgumentMatchers.any(Timesheet.class))).thenAnswer(i->i.getArgument(0));

        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getTimesheet().getEntries()).hasSize(1);
    }
}
