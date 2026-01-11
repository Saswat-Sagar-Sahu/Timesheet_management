package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.UserQueryUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "UserQueryUseCaseImpl")
public class UserQueryUseCaseImpl implements UserQueryUseCase {

    private UserRepository userRepository;

    @Override
    public GetUserResponse getById(Integer id) {
        try {
            Optional<User> opt = userRepository.findById(id);
            if (opt.isEmpty()) {
                return GetUserResponse.builder().status(false).message("User not found").errorCode(ErrorCode.E_002).build();
            }
            return GetUserResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).user(opt.get()).build();
        } catch (Exception e) {
            log.error("Error fetching user", e);
            return GetUserResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }

    @Override
    public ListUsersResponse listAll() {
        try {
            List<User> list = userRepository.findAll();
            return ListUsersResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).users(list).build();
        } catch (Exception e) {
            log.error("Error listing users", e);
            return ListUsersResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

