package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.AddProjectUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j(topic = "AddProjectUseCaseImpl")
public class AddProjectUseCaseImpl implements AddProjectUseCase {

    private ProjectRepository projectRepository;

    @Override
    public AddProjectResponse execute(AddProjectRequest request) {
        if (request == null || request.getProjectCode() == null || request.getName() == null) {
            return AddProjectResponse.builder()
                    .status(false)
                    .message("Invalid request: projectCode and name are required")
                    .errorCode(ErrorCode.E_002)
                    .build();
        }

        // check uniqueness of projectCode
        try {
            if (projectRepository.existsByProjectCode(request.getProjectCode())) {
                return AddProjectResponse.builder()
                        .status(false)
                        .message("Project with same projectCode already exists")
                        .errorCode(ErrorCode.E_003)
                        .build();
            }

            Project project = new Project();
            project.setProjectCode(request.getProjectCode());
            project.setName(request.getName());
            project.setClient(request.getClient());
            project.setStatus(true);

            Project saved = projectRepository.save(project);

            return AddProjectResponse.builder()
                    .status(true)
                    .message("Project created")
                    .errorCode(ErrorCode.S_001)
                    .project(saved)
                    .build();

        } catch (Exception e) {
            log.error("Error while creating project", e);
            return AddProjectResponse.builder()
                    .status(false)
                    .message("An error occurred while creating project")
                    .errorCode(ErrorCode.E_001)
                    .build();
        }
    }
}

