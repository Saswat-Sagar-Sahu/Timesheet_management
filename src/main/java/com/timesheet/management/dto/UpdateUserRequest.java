package com.timesheet.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String empId;
    private String name;
    private String username;
    private String role; // CONTRACTOR, MANAGER, ADMIN
    private Boolean status;
}
