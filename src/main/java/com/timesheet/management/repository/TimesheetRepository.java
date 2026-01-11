package com.timesheet.management.repository;

import com.timesheet.management.dto.ProjectSummaryDTO;
import com.timesheet.management.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

    @Query("""
        SELECT new com.timesheet.management.dto.ProjectSummaryDTO(
            p.id, p.name, p.client, p.status,
            u.id, u.name, t.startDate, SUM(e.hoursWorked), t.status
        )
        FROM Timesheet t
        JOIN t.contractor u
        JOIN TimesheetEntry e ON e.timesheet = t
        JOIN Project p ON e.project = p
        WHERE p.id = :projectId
        GROUP BY p.id, u.id, t.startDate, t.status
        ORDER BY u.id, t.startDate desc
    """)
    List<ProjectSummaryDTO> getProjectSummary(@Param("projectId") Integer projectId);

    List<Timesheet> findByContractor_Id(Integer contractorId);

    List<Timesheet> findByContractor_IdAndStartDate(Integer contractorId, LocalDate startDate);
}