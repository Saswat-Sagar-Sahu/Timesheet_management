package com.timesheet.management.usecase;

import com.timesheet.management.dto.SubmitTimesheetRequest;
import com.timesheet.management.dto.TimesheetDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SubmitTimesheetUseCase {

    SubmitTimesheetResponse execute(SubmitTimesheetRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class SubmitTimesheetResponse {
        private TimesheetDTO timesheet;
        private boolean status;
        private String message;
        private TimeSheetErrorCode errorCode;
    }

    enum TimeSheetErrorCode{
        S_001,
        E_001,
        E_002,
        E_003
    }
}

