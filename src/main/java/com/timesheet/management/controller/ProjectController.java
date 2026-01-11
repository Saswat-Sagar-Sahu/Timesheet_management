package com.timesheet.management.controller;

import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.model.BaseResponse;
import com.timesheet.management.usecase.AddProjectUseCase;
import com.timesheet.management.usecase.ProjectQueryUseCase;
import com.timesheet.management.usecase.ProjectActivityQueryUseCase;
import com.timesheet.management.usecase.ProjectActivityManageUseCase;
import com.timesheet.management.usecase.ProjectActivityManageUseCase.RemoveProjectActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private AddProjectUseCase addProjectUseCase;

    @Autowired
    private ProjectQueryUseCase projectQueryUseCase;

    @Autowired
    private ProjectActivityQueryUseCase projectActivityQueryUseCase;

    @Autowired
    private ProjectActivityManageUseCase projectActivityManageUseCase;

    @PostMapping
    public ResponseEntity<?> addProject(@RequestBody AddProjectRequest request) {
        AddProjectUseCase.AddProjectResponse resp = addProjectUseCase.execute(request);
        if (resp.isStatus()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resp.getProject());
        }

        HttpStatus httpStatus = switch (resp.getErrorCode()) {
            case E_002, E_003 -> HttpStatus.BAD_REQUEST;
            case E_001, S_001 -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body(new BaseResponse(false, resp.getMessage()));
    }

    @GetMapping
    public ResponseEntity<?> listProjects() {
        var resp = projectQueryUseCase.listAll();
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getProjects());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Integer id) {
        var resp = projectQueryUseCase.getById(id);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getProject());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getProjectActivities(@PathVariable Integer id) {
        var resp = projectActivityQueryUseCase.listByProject(id);
        if (resp.isStatus()) {
            return ResponseEntity.ok(resp.getActivities());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }

    @DeleteMapping("/{projectId}/activities/{activityCode}")
    public ResponseEntity<?> removeProjectActivity(@PathVariable Integer projectId, @PathVariable Integer activityCode) {
        RemoveProjectActivityRequest req = RemoveProjectActivityRequest.builder().projectId(projectId).activityCode(activityCode).build();
        var resp = projectActivityManageUseCase.remove(req);
        if (resp.isStatus()) {
            return ResponseEntity.ok(new BaseResponse(true, resp.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new BaseResponse(false, resp.getMessage()));
    }
}
