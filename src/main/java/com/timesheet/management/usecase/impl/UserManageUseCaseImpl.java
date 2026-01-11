package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.UpdateUserRequest;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.UserManageUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "UserManageUseCaseImpl")
public class UserManageUseCaseImpl implements UserManageUseCase {

    private UserRepository userRepository;

    @Override
    public UpdateUserResponse update(Integer id, UpdateUserRequest request) {
        if (request == null) {
            return UpdateUserResponse.builder().status(false).message("Invalid request").errorCode(ErrorCode.E_002).build();
        }

        try {
            Optional<User> opt = userRepository.findById(id);
            if (opt.isEmpty()) {
                return UpdateUserResponse.builder().status(false).message("User not found").errorCode(ErrorCode.E_002).build();
            }

            User user = opt.get();
            if (request.getName() != null) user.setName(request.getName());
            if (request.getUsername() != null) user.setUsername(request.getUsername());
            // duplicate checks
            if (request.getUsername() != null && userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
                return UpdateUserResponse.builder().status(false).message("Username already taken").errorCode(ErrorCode.E_002).build();
            }
            if (request.getEmpId() != null && userRepository.existsByEmpIdAndIdNot(request.getEmpId(), id)) {
                return UpdateUserResponse.builder().status(false).message("empId already taken").errorCode(ErrorCode.E_002).build();
            }
            if (request.getRole() != null) {
                try {
                    user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    return UpdateUserResponse.builder().status(false).message("Invalid role").errorCode(ErrorCode.E_002).build();
                }
            }
            if (request.getStatus() != null) user.setStatus(request.getStatus());

            User saved = userRepository.save(user);
            return UpdateUserResponse.builder().status(true).message("User updated").errorCode(ErrorCode.S_001).user(saved).build();
        } catch (Exception e) {
            log.error("Error updating user", e);
            return UpdateUserResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}
