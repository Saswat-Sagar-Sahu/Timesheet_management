package com.timesheet.management.controller;

import com.timesheet.management.dto.AddUserRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private AddUserUseCase addUserUseCase;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest request) {
        AddUserUseCase.AddUserResponse resp = addUserUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getUser());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002, E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }
}

