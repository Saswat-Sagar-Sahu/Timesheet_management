package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetDTO;
import com.timesheet.management.dto.TimesheetEntryDTO;
import com.timesheet.management.dto.UpdateTimesheetRequest;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.entity.User;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.repository.TimesheetRepository;
import com.timesheet.management.repository.UserRepository;
import com.timesheet.management.usecase.UpdateTimesheetUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j(topic = "UpdateTimesheetUseCaseImpl")
public class UpdateTimesheetUseCaseImpl implements UpdateTimesheetUseCase {

    private TimesheetRepository timesheetRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private ActivityRepository activityRepository;

    @Override
    public UpdateTimesheetResponse execute(UpdateTimesheetRequest request) {
        if (request == null || request.getTimesheetId() == null) {
            return UpdateTimesheetResponse.builder()
                    .status(false)
                    .message("Invalid request: timesheetId is required")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        Optional<Timesheet> tsOpt = timesheetRepository.findById(request.getTimesheetId());
        if (tsOpt.isEmpty()) {
            return UpdateTimesheetResponse.builder()
                    .status(false)
                    .message("Timesheet not found")
                    .errorCode(TimeSheetErrorCode.E_003)
                    .build();
        }

        Timesheet timesheet = tsOpt.get();

        if (timesheet.getStatus() == Timesheet.Status.SUBMITTED || timesheet.getStatus() == Timesheet.Status.APPROVED) {
            return UpdateTimesheetResponse.builder()
                    .status(false)
                    .message("Cannot update a submitted or approved timesheet")
                    .errorCode(TimeSheetErrorCode.E_002)
                    .build();
        }

        try {
            if (request.getStartDate() != null) {
                timesheet.setStartDate(request.getStartDate());
            }

            if (request.getEntries() != null) {
                List<TimesheetEntry> entries = new ArrayList<>();
                for (TimesheetEntryDTO dto : request.getEntries()) {
                    if (dto.getProjectId() == null || dto.getActivityCode() == null || dto.getDate() == null || dto.getHoursWorked() == null) {
                        return UpdateTimesheetResponse.builder()
                                .status(false)
                                .message("Each entry must have projectId, activityCode, date and hoursWorked")
                                .errorCode(TimeSheetErrorCode.E_002)
                                .build();
                    }
                    var projectOpt = projectRepository.findById(dto.getProjectId());
                    if (projectOpt.isEmpty()) {
                        return UpdateTimesheetResponse.builder()
                                .status(false)
                                .message("Project not found for id: " + dto.getProjectId())
                                .errorCode(TimeSheetErrorCode.E_003)
                                .build();
                    }
                    var activityOpt = activityRepository.findById(dto.getActivityCode());
                    if (activityOpt.isEmpty()) {
                        return UpdateTimesheetResponse.builder()
                                .status(false)
                                .message("Activity not found for code: " + dto.getActivityCode())
                                .errorCode(TimeSheetErrorCode.E_003)
                                .build();
                    }

                    TimesheetEntry entry = new TimesheetEntry();
                    entry.setProject(projectOpt.get());
                    entry.setActivity(activityOpt.get());
                    entry.setDate(dto.getDate());
                    entry.setHoursWorked(dto.getHoursWorked());
                    entry.setComments(dto.getComments());
                    entry.setTimesheet(timesheet);
                    entries.add(entry);
                }
                timesheet.setEntries(entries);
            } else {
                // Patch semantics: addEntries and removeEntryIds
                if ((request.getAddEntries() != null && !request.getAddEntries().isEmpty()) || (request.getRemoveEntryIds() != null && !request.getRemoveEntryIds().isEmpty())) {
                    // Ensure existing entries list is mutable
                    List<TimesheetEntry> existing = timesheet.getEntries() == null ? new ArrayList<>() : new ArrayList<>(timesheet.getEntries());

                    // Remove entries by id
                    if (request.getRemoveEntryIds() != null && !request.getRemoveEntryIds().isEmpty()) {
                        existing.removeIf(e -> request.getRemoveEntryIds().contains(e.getId()));
                    }

                    // Add new entries
                    if (request.getAddEntries() != null && !request.getAddEntries().isEmpty()) {
                        for (TimesheetEntryDTO dto : request.getAddEntries()) {
                            if (dto.getProjectId() == null || dto.getActivityCode() == null || dto.getDate() == null || dto.getHoursWorked() == null) {
                                return UpdateTimesheetResponse.builder()
                                        .status(false)
                                        .message("Each add entry must have projectId, activityCode, date and hoursWorked")
                                        .errorCode(TimeSheetErrorCode.E_002)
                                        .build();
                            }

                            var projectOpt = projectRepository.findById(dto.getProjectId());
                            if (projectOpt.isEmpty()) {
                                return UpdateTimesheetResponse.builder()
                                        .status(false)
                                        .message("Project not found for id: " + dto.getProjectId())
                                        .errorCode(TimeSheetErrorCode.E_003)
                                        .build();
                            }
                            var activityOpt = activityRepository.findById(dto.getActivityCode());
                            if (activityOpt.isEmpty()) {
                                return UpdateTimesheetResponse.builder()
                                        .status(false)
                                        .message("Activity not found for code: " + dto.getActivityCode())
                                        .errorCode(TimeSheetErrorCode.E_003)
                                        .build();
                            }

                            TimesheetEntry entry = new TimesheetEntry();
                            entry.setProject(projectOpt.get());
                            entry.setActivity(activityOpt.get());
                            entry.setDate(dto.getDate());
                            entry.setHoursWorked(dto.getHoursWorked());
                            entry.setComments(dto.getComments());
                            entry.setTimesheet(timesheet);
                            existing.add(entry);
                        }
                    }

                    timesheet.setEntries(existing);
                }
            }

            Timesheet saved = timesheetRepository.save(timesheet);

            List<TimesheetEntryDTO> savedEntries = null;
            if (saved.getEntries() != null) {
                savedEntries = saved.getEntries().stream().map(e -> TimesheetEntryDTO.builder()
                        .projectId(e.getProject() != null ? e.getProject().getId() : null)
                        .activityCode(e.getActivity() != null ? e.getActivity().getCode() : null)
                        .date(e.getDate())
                        .hoursWorked(e.getHoursWorked())
                        .comments(e.getComments())
                        .build()).collect(Collectors.toList());
            }

            TimesheetDTO result = TimesheetDTO.builder()
                    .id(saved.getId())
                    .contractorId(saved.getContractor().getId())
                    .startDate(saved.getStartDate())
                    .status(saved.getStatus())
                    .entries(savedEntries)
                    .build();

            return UpdateTimesheetResponse.builder()
                    .status(true)
                    .message("Timesheet updated")
                    .errorCode(TimeSheetErrorCode.S_001)
                    .timesheet(result)
                    .build();

        } catch (Exception e) {
            log.error("Error while updating timesheet", e);
            return UpdateTimesheetResponse.builder()
                    .status(false)
                    .message("An error occurred while updating timesheet")
                    .errorCode(TimeSheetErrorCode.E_001)
                    .build();
        }
    }
}
