package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.UpdateUserRequest;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.UserManageUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManageUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManageUseCaseImpl impl;

    @Test
    void update_shouldReturnError_whenUserNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        var resp = impl.update(999, new UpdateUserRequest());
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void update_shouldReturnError_whenInvalidRole() {
        User u = new User(); u.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(u));
        var req = new UpdateUserRequest(); req.setRole("INVALID");
        var resp = impl.update(1, req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void update_shouldUpdate_whenValid() {
        User u = new User(); u.setId(2); u.setUsername("old");
        when(userRepository.findById(2)).thenReturn(Optional.of(u));
        when(userRepository.existsByUsernameAndIdNot("new", 2)).thenReturn(false);
        when(userRepository.existsByEmpIdAndIdNot("E1", 2)).thenReturn(false);
        UpdateUserRequest req = new UpdateUserRequest(); req.setUsername("new"); req.setEmpId("E1"); req.setRole("ADMIN"); req.setName("New"); req.setStatus(true);
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenAnswer(i->i.getArgument(0));
        var resp = impl.update(2, req);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getUser().getUsername()).isEqualTo("new");
        assertThat(resp.getUser().getRole()).isEqualTo(User.Role.ADMIN);
    }
}

