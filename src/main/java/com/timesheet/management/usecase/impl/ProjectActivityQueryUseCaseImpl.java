package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.ProjectActivityQueryUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j(topic = "ProjectActivityQueryUseCaseImpl")
public class ProjectActivityQueryUseCaseImpl implements ProjectActivityQueryUseCase {

    private ProjectRepository projectRepository;
    private ProjectActivityRepository projectActivityRepository;

    @Override
    public ListProjectActivitiesResponse listByProject(Integer projectId) {
        try {
            Optional<Project> pOpt = projectRepository.findById(projectId);
            if (pOpt.isEmpty()) {
                return ListProjectActivitiesResponse.builder().status(false).message("Project not found").errorCode(ErrorCode.E_002).build();
            }
            Project p = pOpt.get();
            var list = projectActivityRepository.findAllByProject(p);
            List<Activity> activities = list.stream().map(pa -> pa.getActivity()).collect(Collectors.toList());
            return ListProjectActivitiesResponse.builder().status(true).message("Success").errorCode(ErrorCode.S_001).activities(activities).build();
        } catch (Exception e) {
            log.error("Error listing project activities", e);
            return ListProjectActivitiesResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

