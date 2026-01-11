package com.timesheet.management.usecase;

import com.timesheet.management.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface ProjectActivityQueryUseCase {

    ListProjectActivitiesResponse listByProject(Integer projectId);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class ListProjectActivitiesResponse{
        private List<Activity> activities;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

