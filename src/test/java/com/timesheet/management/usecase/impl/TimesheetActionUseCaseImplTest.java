package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetActionRequest;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
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
class TimesheetActionUseCaseImplTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @InjectMocks
    private TimesheetActionUseCaseImpl impl;

    @Test
    void execute_shouldReturnError_whenTimesheetNotFound() {
        TimesheetActionRequest req = TimesheetActionRequest.builder().timesheetId(999).action(TimesheetActionRequest.Action.SUBMIT).build();
        when(timesheetRepository.findById(999)).thenReturn(Optional.empty());
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_submit_shouldReturnError_whenNoEntries() {
        Timesheet t = new Timesheet(); t.setId(1); t.setStatus(Timesheet.Status.DRAFT);
        when(timesheetRepository.findById(1)).thenReturn(Optional.of(t));
        TimesheetActionRequest req = TimesheetActionRequest.builder().timesheetId(1).action(TimesheetActionRequest.Action.SUBMIT).build();
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_submit_shouldSucceed_whenHasEntries() {
        Timesheet t = new Timesheet(); t.setId(2); t.setStatus(Timesheet.Status.DRAFT);
        List<TimesheetEntry> entries = new ArrayList<>();
        TimesheetEntry e = new TimesheetEntry(); e.setId(10); e.setDate(LocalDate.now());
        entries.add(e);
        t.setEntries(entries);
        when(timesheetRepository.findById(2)).thenReturn(Optional.of(t));
        when(timesheetRepository.save(any(Timesheet.class))).thenReturn(t);

        TimesheetActionRequest req = TimesheetActionRequest.builder().timesheetId(2).action(TimesheetActionRequest.Action.SUBMIT).build();
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isTrue();
        verify(timesheetRepository).save(any(Timesheet.class));
    }
}

