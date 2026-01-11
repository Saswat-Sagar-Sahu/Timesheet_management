package com.timesheet.management.usecase;

import com.timesheet.management.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface ProjectQueryUseCase {

    GetProjectResponse getById(Integer id);

    ListProjectsResponse listAll();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class GetProjectResponse{
        private Project project;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class ListProjectsResponse{
        private List<Project> projects;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

