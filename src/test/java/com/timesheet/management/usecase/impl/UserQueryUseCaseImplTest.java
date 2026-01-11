package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.UserQueryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserQueryUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryUseCaseImpl impl;

    @Test
    void getById_shouldReturnError_whenNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        UserQueryUseCase.GetUserResponse resp = impl.getById(999);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(UserQueryUseCase.ErrorCode.E_002);
    }

    @Test
    void getById_shouldReturnUser_whenFound() {
        User u = new User(); u.setId(10); u.setName("Jane");
        when(userRepository.findById(10)).thenReturn(Optional.of(u));
        UserQueryUseCase.GetUserResponse resp = impl.getById(10);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getUser()).isNotNull();
        assertThat(resp.getUser().getId()).isEqualTo(10);
    }

    @Test
    void listAll_shouldReturnUsers() {
        User u1 = new User(); u1.setId(1); u1.setName("A");
        User u2 = new User(); u2.setId(2); u2.setName("B");
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1,u2));
        UserQueryUseCase.ListUsersResponse resp = impl.listAll();
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getUsers()).hasSize(2);
    }
}

