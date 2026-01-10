package com.timesheet.management.controller;

import com.timesheet.management.dto.AddTimesheetRequest;
import com.timesheet.management.dto.SubmitTimesheetRequest;
import com.timesheet.management.dto.UpdateTimesheetRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddTimesheetUseCase;
import com.timesheet.management.usecase.SubmitTimesheetUseCase;
import com.timesheet.management.usecase.UpdateTimesheetUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/timesheets")
public class TimesheetController {

    @Autowired
    private AddTimesheetUseCase addTimesheetUseCase;

    @Autowired
    private UpdateTimesheetUseCase updateTimesheetUseCase;

    @Autowired
    private SubmitTimesheetUseCase submitTimesheetUseCase;

    @PostMapping
    public ResponseEntity<?> addTimesheet(@RequestBody AddTimesheetRequest request) {
        AddTimesheetUseCase.AddTimesheetResponse resp = addTimesheetUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getTimesheet());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimesheet(@PathVariable Integer id, @RequestBody UpdateTimesheetRequest request) {
        request.setTimesheetId(id);
        UpdateTimesheetUseCase.UpdateTimesheetResponse resp = updateTimesheetUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getTimesheet());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitTimesheet(@PathVariable Integer id, @RequestBody(required = false) SubmitTimesheetRequest request) {
        if (request == null) {
            request = SubmitTimesheetRequest.builder().timesheetId(id).build();
        } else {
            request.setTimesheetId(id);
        }

        SubmitTimesheetUseCase.SubmitTimesheetResponse resp = submitTimesheetUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getTimesheet());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }
}

