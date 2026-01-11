package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.SubmitTimesheetRequest;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.TimesheetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmitTimesheetUseCaseImplTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @InjectMocks
    private SubmitTimesheetUseCaseImpl impl;

    @Test
    void execute_shouldReturnError_whenRequestInvalid() {
        var resp = impl.execute(null);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.E_002);
    }

    @Test
    void execute_shouldReturnError_whenTimesheetNotFound() {
        when(timesheetRepository.findById(999)).thenReturn(Optional.empty());
        var resp = impl.execute(SubmitTimesheetRequest.builder().timesheetId(999).build());
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.E_003);
    }

    @Test
    void execute_shouldReturnError_whenAlreadySubmittedOrApproved() {
        Timesheet t = new Timesheet(); t.setId(1); t.setStatus(Timesheet.Status.SUBMITTED);
        when(timesheetRepository.findById(1)).thenReturn(Optional.of(t));
        var resp = impl.execute(SubmitTimesheetRequest.builder().timesheetId(1).build());
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.E_002);
    }

    @Test
    void execute_shouldReturnError_whenNoEntries() {
        Timesheet t = new Timesheet(); t.setId(2); t.setStatus(Timesheet.Status.DRAFT);
        when(timesheetRepository.findById(2)).thenReturn(Optional.of(t));
        var resp = impl.execute(SubmitTimesheetRequest.builder().timesheetId(2).build());
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.E_003);
    }

    @Test
    void execute_shouldSubmit_whenValid() {
        Timesheet t = new Timesheet(); t.setId(3); t.setStatus(Timesheet.Status.DRAFT);
        User u = new User(); u.setId(5); t.setContractor(u);
        List<TimesheetEntry> entries = new ArrayList<>();
        TimesheetEntry e = new TimesheetEntry(); e.setId(11); e.setDate(LocalDate.now());
        entries.add(e);
        t.setEntries(entries);

        when(timesheetRepository.findById(3)).thenReturn(Optional.of(t));
        when(timesheetRepository.save(any(Timesheet.class))).thenAnswer(invocation -> {
            Timesheet saved = invocation.getArgument(0);
            saved.setId(3);
            return saved;
        });

        var resp = impl.execute(SubmitTimesheetRequest.builder().timesheetId(3).comment("Ready").build());
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.S_001);
        verify(timesheetRepository).save(any(Timesheet.class));
    }

    @Test
    void execute_shouldReturnError_whenSaveThrows() {
        Timesheet t = new Timesheet(); t.setId(4); t.setStatus(Timesheet.Status.DRAFT);
        TimesheetEntry e = new TimesheetEntry(); e.setId(12); e.setDate(LocalDate.now());
        t.setEntries(List.of(e));
        when(timesheetRepository.findById(4)).thenReturn(Optional.of(t));
        when(timesheetRepository.save(any(Timesheet.class))).thenThrow(new RuntimeException("DB down"));

        var resp = impl.execute(SubmitTimesheetRequest.builder().timesheetId(4).comment("Ready").build());
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.SubmitTimesheetUseCase.TimeSheetErrorCode.E_001);
    }
}

