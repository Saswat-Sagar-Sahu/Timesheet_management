package com.timesheet.management.usecase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ProjectActivityManageUseCase {

    RemoveProjectActivityResponse remove(RemoveProjectActivityRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class RemoveProjectActivityRequest{
        private Integer projectId;
        private Integer activityCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class RemoveProjectActivityResponse{
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

