package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddProjectActivityRequest;
import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.ProjectActivity;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.AddProjectActivityUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "AddProjectActivityUseCaseImpl")
public class AddProjectActivityUseCaseImpl implements AddProjectActivityUseCase {

    private ProjectRepository projectRepository;
    private ActivityRepository activityRepository;
    private ProjectActivityRepository projectActivityRepository;

    @Override
    public AddProjectActivityResponse execute(AddProjectActivityRequest request) {
        if (request == null || request.getProjectId() == null || request.getActivityCode() == null) {
            return AddProjectActivityResponse.builder()
                    .status(false)
                    .message("projectId and activityCode are required")
                    .errorCode(ErrorCode.E_002)
                    .build();
        }

        try {
            Optional<Project> pOpt = projectRepository.findById(request.getProjectId());
            if (pOpt.isEmpty()) {
                return AddProjectActivityResponse.builder().status(false).message("Project not found").errorCode(ErrorCode.E_003).build();
            }
            Optional<Activity> aOpt = activityRepository.findById(request.getActivityCode());
            if (aOpt.isEmpty()) {
                return AddProjectActivityResponse.builder().status(false).message("Activity not found").errorCode(ErrorCode.E_003).build();
            }

            Project p = pOpt.get();
            Activity a = aOpt.get();

            if (projectActivityRepository.existsByProjectAndActivity(p, a)) {
                return AddProjectActivityResponse.builder().status(false).message("ProjectActivity already exists").errorCode(ErrorCode.E_003).build();
            }

            ProjectActivity pa = new ProjectActivity();
            pa.setProject(p);
            pa.setActivity(a);

            ProjectActivity saved = projectActivityRepository.save(pa);
            return AddProjectActivityResponse.builder().status(true).message("ProjectActivity created").errorCode(ErrorCode.S_001).projectActivity(saved).build();
        } catch (Exception e) {
            log.error("error while creating project activity", e);
            return AddProjectActivityResponse.builder().status(false).message("Error").errorCode(ErrorCode.E_001).build();
        }
    }
}

