package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddUserRequest;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.AddUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddUserUseCaseImpl addUserUseCase;

    @Test
    void execute_shouldReturnError_whenDobInFuture() {
        AddUserRequest req = AddUserRequest.builder()
                .empId("EMP-1")
                .name("John")
                .username("john")
                .role("CONTRACTOR")
                .dob(LocalDate.now().plusDays(1))
                .build();

        var resp = addUserUseCase.execute(req);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(AddUserUseCase.TimeSheetErrorCode.E_003);
    }

    @Test
    void execute_shouldReturnError_whenUnder18() {
        AddUserRequest req = AddUserRequest.builder()
                .empId("EMP-2")
                .name("Young")
                .username("young")
                .role("CONTRACTOR")
                .dob(LocalDate.now().minusYears(10))
                .build();

        var resp = addUserUseCase.execute(req);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(AddUserUseCase.TimeSheetErrorCode.E_003);
    }

    @Test
    void execute_shouldReturnError_whenInvalidRole() {
        AddUserRequest req = AddUserRequest.builder()
                .empId("EMP-3")
                .name("Jane")
                .username("jane")
                .role("INVALID")
                .dob(LocalDate.of(1990, Month.JANUARY, 1))
                .build();

        var resp = addUserUseCase.execute(req);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(AddUserUseCase.TimeSheetErrorCode.E_003);
    }

    @Test
    void execute_shouldCreateUser_whenValid() {
        AddUserRequest req = AddUserRequest.builder()
                .empId("EMP-4")
                .name("Alice")
                .username("alice")
                .role("MANAGER")
                .dob(LocalDate.of(1990, Month.JANUARY, 1))
                .build();

        User saved = new User();
        saved.setId(10);
        saved.setEmpId("EMP-4");
        saved.setName("Alice");
        saved.setUsername("alice");
        saved.setDob(req.getDob());
        saved.setStatus(true);
        saved.setRole(User.Role.MANAGER);

        when(userRepository.save(any(User.class))).thenReturn(saved);

        var resp = addUserUseCase.execute(req);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getUser()).isNotNull();
        assertThat(resp.getUser().getId()).isEqualTo(10);
        assertThat(resp.getUser().getRole()).isEqualTo(User.Role.MANAGER);
    }
}

