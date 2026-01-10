package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.SubmitTimesheetRequest;
import com.timesheet.management.dto.TimesheetDTO;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.usecase.SubmitTimesheetUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "SubmitTimesheetUseCaseImpl")
public class SubmitTimesheetUseCaseImpl implements SubmitTimesheetUseCase {

    private TimesheetRepository timesheetRepository;

    @Override
    public SubmitTimesheetResponse execute(SubmitTimesheetRequest request) {
        if (request == null || request.getTimesheetId() == null) {
            return SubmitTimesheetResponse.builder()
                    .status(false)
                    .message("Invalid request: timesheetId is required")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        Optional<Timesheet> tsOpt = timesheetRepository.findById(request.getTimesheetId());
        if (tsOpt.isEmpty()) {
            return SubmitTimesheetResponse.builder()
                    .status(false)
                    .message("Timesheet not found")
                    .errorCode(TimeSheetErrorCode.E_003)
                    .build();
        }

        Timesheet timesheet = tsOpt.get();

        // Only allow submit from DRAFT or REJECTED
        if (timesheet.getStatus() == Timesheet.Status.SUBMITTED || timesheet.getStatus() == Timesheet.Status.APPROVED) {
            return SubmitTimesheetResponse.builder()
                    .status(false)
                    .message("Cannot submit a timesheet that is already submitted or approved")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        List<TimesheetEntry> entries = timesheet.getEntries();
        if (entries == null || entries.isEmpty()) {
            return SubmitTimesheetResponse.builder()
                    .status(false)
                    .message("Cannot submit an empty timesheet")
                    .errorCode(TimeSheetErrorCode.E_003)
                    .build();
        }

        try {
            timesheet.setStatus(Timesheet.Status.SUBMITTED);
            timesheet.setManagerComment(request.getComment());

            Timesheet saved = timesheetRepository.save(timesheet);

            SubmitTimesheetResponse response = SubmitTimesheetResponse.builder()
                    .status(true)
                    .message("Timesheet submitted")
                    .errorCode(TimeSheetErrorCode.S_001)
                    .timesheet(TimesheetDTO.builder()
                            .id(saved.getId())
                            .contractorId(saved.getContractor().getId())
                            .startDate(saved.getStartDate())
                            .status(saved.getStatus())
                            .entries(null)
                            .build())
                    .build();

            return response;

        } catch (Exception e) {
            log.error("Error while submitting timesheet", e);
            return SubmitTimesheetResponse.builder()
                    .status(false)
                    .message("An error occurred while submitting timesheet")
                    .errorCode(TimeSheetErrorCode.E_001)
                    .build();
        }
    }
}

