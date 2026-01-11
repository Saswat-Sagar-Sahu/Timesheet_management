package com.timesheet.management.usecase;

import com.timesheet.management.dto.AddActivityRequest;
import com.timesheet.management.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AddActivityUseCase {
    AddActivityResponse execute(AddActivityRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class AddActivityResponse{
        private Activity activity;
        private boolean status;
        private String message;
        private ErrorCode errorCode;
    }

    enum ErrorCode{ S_001, E_001, E_002, E_003 }
}
