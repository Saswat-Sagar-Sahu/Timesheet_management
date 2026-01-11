package com.timesheet.management.usecase;

import com.timesheet.management.dto.TimesheetActionRequest;
import com.timesheet.management.dto.TimesheetDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface TimesheetActionUseCase {

    TimesheetActionResponse execute(TimesheetActionRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class TimesheetActionResponse {
        private TimesheetDTO timesheet;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{
        S_001,
        E_001,
        E_002,
        E_003
    }
}

