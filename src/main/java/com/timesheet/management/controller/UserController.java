package com.timesheet.management.controller;

import com.timesheet.management.dto.AddUserRequest;
import com.timesheet.management.dto.UpdateUserRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddUserUseCase;
import com.timesheet.management.usecase.UserQueryUseCase;
import com.timesheet.management.usecase.UserManageUseCase;
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

    @Autowired
    private UserQueryUseCase userQueryUseCase;

    @Autowired
    private UserManageUseCase userManageUseCase;

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

    @GetMapping
    public ResponseEntity<?> listUsers() {
        var resp = userQueryUseCase.listAll();
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getUsers());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        var resp = userQueryUseCase.getById(id);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getUser());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
        var resp = userManageUseCase.update(id, request);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getUser());
        }
        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }
}
