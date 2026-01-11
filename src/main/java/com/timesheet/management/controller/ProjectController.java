package com.timesheet.management.controller;

import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private AddProjectUseCase addProjectUseCase;

    @PostMapping
    public ResponseEntity<?> addProject(@RequestBody AddProjectRequest request) {
        AddProjectUseCase.AddProjectResponse resp = addProjectUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getProject());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002, E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }
}

