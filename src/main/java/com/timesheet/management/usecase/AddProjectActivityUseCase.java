package com.timesheet.management.usecase;

import com.timesheet.management.entity.ProjectActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AddProjectActivityUseCase {

    AddProjectActivityResponse execute(com.timesheet.management.dto.AddProjectActivityRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class AddProjectActivityResponse{
        private ProjectActivity projectActivity;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{S_001, E_001, E_002, E_003}
}

