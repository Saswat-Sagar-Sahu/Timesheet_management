package com.timesheet.management.controller;

import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddActivityUseCase;
import com.timesheet.management.usecase.AddProjectActivityUseCase;
import com.timesheet.management.dto.AddProjectActivityRequest;
import com.timesheet.management.dto.AddActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    @Autowired
    private AddActivityUseCase addActivityUseCase;

    @Autowired
    private AddProjectActivityUseCase addProjectActivityUseCase;

    @PostMapping
    public ResponseEntity<?> addActivity(@RequestBody AddActivityRequest request) {
        var resp = addActivityUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getActivity());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @PostMapping("/project-activity")
    public ResponseEntity<?> addProjectActivity(@RequestBody AddProjectActivityRequest request) {
        var resp = addProjectActivityUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getProjectActivity());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }
}
