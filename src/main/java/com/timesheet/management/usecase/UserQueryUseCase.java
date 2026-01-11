package com.timesheet.management.usecase;

import com.timesheet.management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface UserQueryUseCase {

    GetUserResponse getById(Integer id);

    ListUsersResponse listAll();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class GetUserResponse{
        private User user;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class ListUsersResponse{
        private List<User> users;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

