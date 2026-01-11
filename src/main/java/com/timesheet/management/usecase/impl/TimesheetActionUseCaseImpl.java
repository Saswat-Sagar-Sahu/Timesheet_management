package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetActionRequest;
import com.timesheet.management.dto.TimesheetDTO;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.usecase.TimesheetActionUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "TimesheetActionUseCaseImpl")
public class TimesheetActionUseCaseImpl implements TimesheetActionUseCase {

    private TimesheetRepository timesheetRepository;

    @Override
    public TimesheetActionResponse execute(TimesheetActionRequest request) {
        if (request == null || request.getTimesheetId() == null || request.getAction() == null) {
            return TimesheetActionResponse.builder()
                    .status(false)
                    .message("Invalid request: timesheetId and action are required")
                    .errorCode(ErrorCode.E_002)
                    .build();
        }

        Optional<Timesheet> tsOpt = timesheetRepository.findById(request.getTimesheetId());
        if (tsOpt.isEmpty()) {
            return TimesheetActionResponse.builder()
                    .status(false)
                    .message("Timesheet not found")
                    .errorCode(ErrorCode.E_003)
                    .build();
        }

        Timesheet timesheet = tsOpt.get();

        try {
            switch (request.getAction()) {
                case SUBMIT -> {
                    // allow submit only from DRAFT or REJECTED
                    if (timesheet.getStatus() == Timesheet.Status.SUBMITTED || timesheet.getStatus() == Timesheet.Status.APPROVED) {
                        return TimesheetActionResponse.builder().status(false).message("Cannot submit: timesheet already submitted or approved").errorCode(ErrorCode.E_002).build();
                    }
                    List<TimesheetEntry> entries = timesheet.getEntries();
                    if (entries == null || entries.isEmpty()) {
                        return TimesheetActionResponse.builder().status(false).message("Cannot submit an empty timesheet").errorCode(ErrorCode.E_003).build();
                    }
                    timesheet.setStatus(Timesheet.Status.SUBMITTED);
                    timesheet.setManagerComment(request.getComment());
                }
                case APPROVE -> {
                    // only submitted timesheet can be approved
                    if (timesheet.getStatus() != Timesheet.Status.SUBMITTED) {
                        return TimesheetActionResponse.builder().status(false).message("Only submitted timesheet can be approved").errorCode(ErrorCode.E_002).build();
                    }
                    timesheet.setStatus(Timesheet.Status.APPROVED);
                    timesheet.setManagerComment(request.getComment());
                }
                case REJECT -> {
                    // only submitted timesheet can be rejected
                    if (timesheet.getStatus() != Timesheet.Status.SUBMITTED) {
                        return TimesheetActionResponse.builder().status(false).message("Only submitted timesheet can be rejected").errorCode(ErrorCode.E_002).build();
                    }
                    timesheet.setStatus(Timesheet.Status.REJECTED);
                    timesheet.setManagerComment(request.getComment());
                }
            }

            Timesheet saved = timesheetRepository.save(timesheet);

            TimesheetDTO dto = TimesheetDTO.builder()
                    .id(saved.getId())
                    .contractorId(saved.getContractor() != null ? saved.getContractor().getId() : null)
                    .startDate(saved.getStartDate())
                    .status(saved.getStatus())
                    .entries(null)
                    .build();

            return TimesheetActionResponse.builder().status(true).message("Action applied").errorCode(ErrorCode.S_001).timesheet(dto).build();
        } catch (Exception e) {
            log.error("Error applying timesheet action", e);
            return TimesheetActionResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

