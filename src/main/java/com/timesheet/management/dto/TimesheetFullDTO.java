package com.timesheet.management.dto;

import com.timesheet.management.entity.Timesheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetFullDTO {
    private Integer id;
    private Integer contractorId;
    private LocalDate startDate;
    private Timesheet.Status status;
    private String managerComment;
    private List<TimesheetEntryDTO> entries;
}

