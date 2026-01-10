package com.timesheet.management.usecase;

import com.timesheet.management.dto.TimesheetDTO;
import com.timesheet.management.dto.UpdateTimesheetRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateTimesheetUseCase {

    UpdateTimesheetResponse execute(UpdateTimesheetRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class UpdateTimesheetResponse {
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

