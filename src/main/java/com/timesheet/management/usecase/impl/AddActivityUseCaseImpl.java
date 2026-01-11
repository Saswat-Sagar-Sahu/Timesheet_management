package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddActivityRequest;
import com.timesheet.management.entity.Activity;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.usecase.AddActivityUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j(topic = "AddActivityUseCaseImpl")
public class AddActivityUseCaseImpl implements AddActivityUseCase {

    private ActivityRepository activityRepository;

    @Override
    public AddActivityResponse execute(AddActivityRequest request) {
        if (request == null || request.getCode() == null || request.getName() == null) {
            return AddActivityResponse.builder().status(false).message("code and name required").errorCode(ErrorCode.E_002).build();
        }

        try {
            if (activityRepository.existsById(request.getCode())) {
                return AddActivityResponse.builder().status(false).message("Activity code already exists").errorCode(ErrorCode.E_003).build();
            }

            Activity a = new Activity();
            a.setCode(request.getCode());
            a.setName(request.getName());
            a.setDescription(request.getDescription());

            Activity saved = activityRepository.save(a);
            return AddActivityResponse.builder().status(true).message("Activity created").errorCode(ErrorCode.S_001).activity(saved).build();
        } catch (Exception e) {
            log.error("error while creating activity", e);
            return AddActivityResponse.builder().status(false).message("Error").errorCode(ErrorCode.E_001).build();
        }
    }
}
