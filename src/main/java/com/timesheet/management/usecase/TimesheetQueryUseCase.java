package com.timesheet.management.usecase;

import com.timesheet.management.dto.TimesheetFullDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public interface TimesheetQueryUseCase {

    GetTimesheetResponse getById(Integer id);

    ListTimesheetsResponse listByContractor(Integer contractorId, LocalDate startDate);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class GetTimesheetResponse{
        private TimesheetFullDTO timesheet;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class ListTimesheetsResponse{
        private List<TimesheetFullDTO> timesheets;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

