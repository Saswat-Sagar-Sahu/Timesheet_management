package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetFullDTO;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.usecase.TimesheetQueryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimesheetQueryUseCaseImplTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @InjectMocks
    private TimesheetQueryUseCaseImpl impl;

    @Test
    void getById_shouldReturnError_whenNotFound() {
        when(timesheetRepository.findById(999)).thenReturn(Optional.empty());
        TimesheetQueryUseCase.GetTimesheetResponse resp = impl.getById(999);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(TimesheetQueryUseCase.ErrorCode.E_002);
    }

    @Test
    void getById_shouldReturnTimesheet_whenFound() {
        Timesheet t = new Timesheet(); t.setId(10); t.setStartDate(LocalDate.now());
        User u = new User(); u.setId(5); t.setContractor(u);
        TimesheetEntry e = new TimesheetEntry(); e.setId(1);
        t.setEntries(List.of(e));
        when(timesheetRepository.findById(10)).thenReturn(Optional.of(t));
        TimesheetQueryUseCase.GetTimesheetResponse resp = impl.getById(10);
        assertThat(resp.isStatus()).isTrue();
        TimesheetFullDTO dto = resp.getTimesheet();
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10);
    }

    @Test
    void listByContractor_shouldReturnList() {
        Timesheet t1 = new Timesheet(); t1.setId(1); t1.setStartDate(LocalDate.now());
        Timesheet t2 = new Timesheet(); t2.setId(2); t2.setStartDate(LocalDate.now().minusDays(7));
        when(timesheetRepository.findByContractor_Id(5)).thenReturn(Arrays.asList(t1,t2));
        TimesheetQueryUseCase.ListTimesheetsResponse resp = impl.listByContractor(5, null);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getTimesheets()).hasSize(2);
    }
}

