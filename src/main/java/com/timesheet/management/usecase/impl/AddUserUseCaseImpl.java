package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddUserRequest;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.AddUserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@Slf4j(topic = "AddUserUseCaseImpl")
public class AddUserUseCaseImpl implements AddUserUseCase {

    private UserRepository userRepository;

    @Override
    public AddUserResponse execute(AddUserRequest request) {
        if (request == null || request.getEmpId() == null || request.getName() == null || request.getUsername() == null || request.getRole() == null || request.getDob() == null) {
            return AddUserResponse.builder()
                    .status(false)
                    .message("Invalid request: empId, name, username, role and dob are required")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        // dob validation: not in future and must be at least 18 years old
        LocalDate dob = request.getDob();
        if (dob.isAfter(LocalDate.now()) || dob.isAfter(LocalDate.now().minusYears(18))) {
            return AddUserResponse.builder()
                    .status(false)
                    .message("Invalid dob: date of birth cannot be in the future or indicate age less than 18")
                    .errorCode(TimeSheetErrorCode.E_003)
                    .build();
        }

        try {
            User user = new User();
            user.setEmpId(request.getEmpId());
            user.setName(request.getName());
            user.setUsername(request.getUsername());
            user.setDob(request.getDob());
            // set active status true by default on creation
            user.setStatus(true);
            try {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                return AddUserResponse.builder()
                        .status(false)
                        .message("Invalid role: must be one of CONTRACTOR, MANAGER, ADMIN")
                        .errorCode(TimeSheetErrorCode.E_003)
                        .build();
            }

            User saved = userRepository.save(user);

            return AddUserResponse.builder()
                    .status(true)
                    .message("User created")
                    .errorCode(TimeSheetErrorCode.S_001)
                    .user(saved)
                    .build();
        } catch (Exception e) {
            log.error("Error while creating user", e);
            return AddUserResponse.builder()
                    .status(false)
                    .message("An error occurred while creating user")
                    .errorCode(TimeSheetErrorCode.E_001)
                    .build();
        }
    }
}
