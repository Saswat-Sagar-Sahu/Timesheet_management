package com.timesheet.management.usecase;

import com.timesheet.management.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface ActivityQueryUseCase {

    GetActivityResponse getByCode(Integer code);

    ListActivitiesResponse listAll();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class GetActivityResponse{
        private Activity activity;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class ListActivitiesResponse{
        private List<Activity> activities;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002 }
}

