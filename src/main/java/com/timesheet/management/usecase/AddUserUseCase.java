package com.timesheet.management.usecase;

import com.timesheet.management.dto.AddUserRequest;
import com.timesheet.management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AddUserUseCase {

    AddUserResponse execute(AddUserRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class AddUserResponse {
        private User user;
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

