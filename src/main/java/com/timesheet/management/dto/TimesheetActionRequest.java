package com.timesheet.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetActionRequest {
    private Integer timesheetId;
    private Integer contractorId; // optional
    private String comment; // manager comment or submit comment
    private Action action;

    public enum Action {
        SUBMIT, APPROVE, REJECT
    }
}

