package com.timesheet.management.controller;

import com.timesheet.management.dto.AddTimesheetRequest;
import com.timesheet.management.dto.SubmitTimesheetRequest;
import com.timesheet.management.dto.UpdateTimesheetRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddTimesheetUseCase;
import com.timesheet.management.usecase.UpdateTimesheetUseCase;
import com.timesheet.management.usecase.TimesheetActionUseCase;
import com.timesheet.management.usecase.TimesheetQueryUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/timesheets")
public class TimesheetController {

    @Autowired
    private AddTimesheetUseCase addTimesheetUseCase;

    @Autowired
    private UpdateTimesheetUseCase updateTimesheetUseCase;

    @Autowired
    private TimesheetQueryUseCase timesheetQueryUseCase;

    @Autowired
    private TimesheetActionUseCase timesheetActionUseCase;

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

    @PatchMapping("/{id}/entries")
    public ResponseEntity<?> patchTimesheetEntries(@PathVariable Integer id, @RequestBody UpdateTimesheetRequest request) {
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

        // convert to TimesheetActionRequest and delegate
        com.timesheet.management.dto.TimesheetActionRequest actionReq = com.timesheet.management.dto.TimesheetActionRequest.builder()
                .timesheetId(id)
                .comment(request.getComment())
                .action(com.timesheet.management.dto.TimesheetActionRequest.Action.SUBMIT)
                .build();

        var resp = timesheetActionUseCase.execute(actionReq);
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

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveTimesheet(@PathVariable Integer id, @RequestBody(required = false) com.timesheet.management.dto.TimesheetActionRequest actionRequest) {
        if (actionRequest == null) {
            actionRequest = com.timesheet.management.dto.TimesheetActionRequest.builder().timesheetId(id).action(com.timesheet.management.dto.TimesheetActionRequest.Action.APPROVE).build();
        } else {
            actionRequest.setTimesheetId(id);
            actionRequest.setAction(com.timesheet.management.dto.TimesheetActionRequest.Action.APPROVE);
        }

        var resp = timesheetActionUseCase.execute(actionRequest);
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

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectTimesheet(@PathVariable Integer id, @RequestBody(required = false) com.timesheet.management.dto.TimesheetActionRequest actionRequest) {
        if (actionRequest == null) {
            actionRequest = com.timesheet.management.dto.TimesheetActionRequest.builder().timesheetId(id).action(com.timesheet.management.dto.TimesheetActionRequest.Action.REJECT).build();
        } else {
            actionRequest.setTimesheetId(id);
            actionRequest.setAction(com.timesheet.management.dto.TimesheetActionRequest.Action.REJECT);
        }

        var resp = timesheetActionUseCase.execute(actionRequest);
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimesheetById(@PathVariable Integer id) {
        TimesheetQueryUseCase.GetTimesheetResponse resp = timesheetQueryUseCase.getById(id);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getTimesheet());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }

    @GetMapping
    public ResponseEntity<?> listByContractor(
            @RequestParam Integer contractorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        TimesheetQueryUseCase.ListTimesheetsResponse resp = timesheetQueryUseCase.listByContractor(contractorId, startDate);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getTimesheets());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }
}
