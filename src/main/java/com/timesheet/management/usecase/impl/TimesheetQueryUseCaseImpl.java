package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.TimesheetEntryDTO;
import com.timesheet.management.dto.TimesheetFullDTO;
import com.timesheet.management.entity.Timesheet;
import com.timesheet.management.entity.TimesheetEntry;
import com.timesheet.management.usecase.TimesheetQueryUseCase;
import com.timesheet.management.repository.TimesheetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j(topic = "TimesheetQueryUseCaseImpl")
public class TimesheetQueryUseCaseImpl implements TimesheetQueryUseCase {

    private TimesheetRepository timesheetRepository;

    @Override
    public GetTimesheetResponse getById(Integer id) {
        try {
            Optional<Timesheet> tsOpt = timesheetRepository.findById(id);
            if (tsOpt.isEmpty()) {
                return GetTimesheetResponse.builder().status(false).message("Timesheet not found").errorCode(ErrorCode.E_002).build();
            }
            Timesheet t = tsOpt.get();
            TimesheetFullDTO dto = mapToDto(t);
            return GetTimesheetResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).timesheet(dto).build();
        } catch (Exception e) {
            log.error("Error fetching timesheet", e);
            return GetTimesheetResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }

    @Override
    public ListTimesheetsResponse listByContractor(Integer contractorId, java.time.LocalDate startDate) {
        try {
            List<Timesheet> list;
            if (startDate == null) {
                list = timesheetRepository.findByContractor_Id(contractorId);
            } else {
                list = timesheetRepository.findByContractor_IdAndStartDate(contractorId, startDate);
            }

            List<TimesheetFullDTO> dtos = list.stream().map(this::mapToDto).collect(Collectors.toList());
            return ListTimesheetsResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).timesheets(dtos).build();
        } catch (Exception e) {
            log.error("Error listing timesheets", e);
            return ListTimesheetsResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }

    private TimesheetFullDTO mapToDto(Timesheet t) {
        List<TimesheetEntryDTO> entries = null;
        if (t.getEntries() != null) {
            entries = t.getEntries().stream().map(this::mapEntry).collect(Collectors.toList());
        }

        return TimesheetFullDTO.builder()
                .id(t.getId())
                .contractorId(t.getContractor() != null ? t.getContractor().getId() : null)
                .startDate(t.getStartDate())
                .status(t.getStatus())
                .managerComment(t.getManagerComment())
                .entries(entries)
                .build();
    }

    private TimesheetEntryDTO mapEntry(TimesheetEntry e) {
        return TimesheetEntryDTO.builder()
                .projectId(e.getProject() != null ? e.getProject().getId() : null)
                .activityCode(e.getActivity() != null ? e.getActivity().getCode() : null)
                .date(e.getDate())
                .hoursWorked(e.getHoursWorked())
                .comments(e.getComments())
                .build();
    }
}

