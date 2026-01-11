package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.usecase.ActivityQueryUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "ActivityQueryUseCaseImpl")
public class ActivityQueryUseCaseImpl implements ActivityQueryUseCase {

    private ActivityRepository activityRepository;

    @Override
    public GetActivityResponse getByCode(Integer code) {
        try {
            Optional<Activity> opt = activityRepository.findById(code);
            if (opt.isEmpty()) {
                return GetActivityResponse.builder().status(false).message("Activity not found").errorCode(ErrorCode.E_002).build();
            }
            return GetActivityResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).activity(opt.get()).build();
        } catch (Exception e) {
            log.error("Error fetching activity", e);
            return GetActivityResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }

    @Override
    public ListActivitiesResponse listAll() {
        try {
            List<Activity> list = activityRepository.findAll();
            return ListActivitiesResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).activities(list).build();
        } catch (Exception e) {
            log.error("Error listing activities", e);
            return ListActivitiesResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

