package com.timesheet.management.usecase.impl;

import com.timesheet.management.Exception.TimeSheetException;
import com.timesheet.management.dto.AddTimesheetRequest;
import com.timesheet.management.dto.TimesheetDTO;
import com.timesheet.management.dto.TimesheetEntryDTO;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.AddTimesheetUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j(topic = "AddTimesheetUseCaseImpl")
public class AddTimesheetUseCaseImpl implements AddTimesheetUseCase {

    private TimesheetRepository timesheetRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private ActivityRepository activityRepository;

    @Override
    public AddTimesheetResponse execute(AddTimesheetRequest request) {
        // validation
        if (request == null || request.getContractorId() == null || request.getStartDate() == null || request.getEntries() == null || request.getEntries().isEmpty()) {
            return AddTimesheetResponse.builder()
                    .status(false)
                    .message("Invalid request: contractorId, startDate and at least one entry are required")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        Optional<User> contractorOpt = userRepository.findById(request.getContractorId());
        if (contractorOpt.isEmpty()) {
            return AddTimesheetResponse.builder()
                    .status(false)
                    .message("Contractor not found")
                    .errorCode(TimeSheetErrorCode.E_003)
                    .build();
        }

        User contractor = contractorOpt.get();

        Timesheet timesheet = new Timesheet();
        timesheet.setContractor(contractor);
        timesheet.setStartDate(request.getStartDate());
        timesheet.setStatus(Timesheet.Status.DRAFT);

        List<TimesheetEntry> entries = new ArrayList<>();

        try {
            for (TimesheetEntryDTO dto : request.getEntries()) {
                // validate project and activity existence
                if (dto.getProjectId() == null || dto.getActivityCode() == null || dto.getDate() == null || dto.getHoursWorked() == null) {
                    return AddTimesheetResponse.builder()
                            .status(false)
                            .message("Each entry must have projectId, activityCode, date and hoursWorked")
                            .errorCode(TimeSheetErrorCode.E_002)
                            .build();
                }

                Optional<Project> projectOpt = projectRepository.findById(dto.getProjectId());
                if (projectOpt.isEmpty()) {
                    return AddTimesheetResponse.builder()
                            .status(false)
                            .message("Project not found for id: " + dto.getProjectId())
                            .errorCode(TimeSheetErrorCode.E_003)
                            .build();
                }

                if (activityRepository.findById(dto.getActivityCode()).isEmpty()) {
                    return AddTimesheetResponse.builder()
                            .status(false)
                            .message("Activity not found for code: " + dto.getActivityCode())
                            .errorCode(TimeSheetErrorCode.E_003)
                            .build();
                }

                TimesheetEntry entry = new TimesheetEntry();
                entry.setProject(projectOpt.get());
                entry.setActivity(activityRepository.findById(dto.getActivityCode()).get());
                entry.setDate(dto.getDate());
                entry.setHoursWorked(dto.getHoursWorked());
                entry.setComments(dto.getComments());
                entry.setTimesheet(timesheet);
                entries.add(entry);
            }

            timesheet.setEntries(entries);

            Timesheet saved = timesheetRepository.save(timesheet);

            TimesheetDTO responseDto = TimesheetDTO.builder()
                    .id(saved.getId())
                    .contractorId(saved.getContractor().getId())
                    .startDate(saved.getStartDate())
                    .status(saved.getStatus())
                    .entries(request.getEntries())
                    .build();

            return AddTimesheetResponse.builder()
                    .status(true)
                    .message("Timesheet created")
                    .errorCode(TimeSheetErrorCode.S_001)
                    .timesheet(responseDto)
                    .build();

        } catch (Exception e) {
            log.error("Error while creating timesheet", e);
            return AddTimesheetResponse.builder()
                    .status(false)
                    .message("An error occurred while creating timesheet")
                    .errorCode(TimeSheetErrorCode.E_001)
                    .build();
        }
    }
}

