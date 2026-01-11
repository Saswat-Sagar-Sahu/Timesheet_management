package com.timesheet.management.repository;

import com.timesheet.management.entity.ProjectActivity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectActivityRepository extends JpaRepository<ProjectActivity, Integer> {
    boolean existsByProjectAndActivity(Project project, Activity activity);
    Optional<ProjectActivity> findByProjectAndActivity(Project project, Activity activity);
}

