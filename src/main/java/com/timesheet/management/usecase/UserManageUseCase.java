package com.timesheet.management.usecase;

import com.timesheet.management.dto.UpdateUserRequest;
import com.timesheet.management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UserManageUseCase {

    UpdateUserResponse update(Integer id, UpdateUserRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class UpdateUserResponse{
        private User user;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

