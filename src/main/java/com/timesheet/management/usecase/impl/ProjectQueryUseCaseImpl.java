package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.ProjectQueryUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "ProjectQueryUseCaseImpl")
public class ProjectQueryUseCaseImpl implements ProjectQueryUseCase {

    private ProjectRepository projectRepository;

    @Override
    public GetProjectResponse getById(Integer id) {
        try {
            Optional<Project> opt = projectRepository.findById(id);
            if (opt.isEmpty()) {
                return GetProjectResponse.builder().status(false).message("Project not found").errorCode(ErrorCode.E_002).build();
            }
            return GetProjectResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).project(opt.get()).build();
        } catch (Exception e) {
            log.error("Error fetching project", e);
            return GetProjectResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }

    @Override
    public ListProjectsResponse listAll() {
        try {
            List<Project> list = projectRepository.findAll();
            return ListProjectsResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).projects(list).build();
        } catch (Exception e) {
            log.error("Error listing projects", e);
            return ListProjectsResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

