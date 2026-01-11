package com.timesheet.management.usecase;

import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AddProjectUseCase {

    AddProjectResponse execute(AddProjectRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class AddProjectResponse {
        private Project project;
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

