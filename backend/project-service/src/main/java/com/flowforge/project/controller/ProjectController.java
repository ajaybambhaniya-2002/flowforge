package com.flowforge.project.controller;

import com.flowforge.common.response.ApiResponse;
import com.flowforge.project.dto.request.CreateProjectRequest;
import com.flowforge.project.dto.request.UpdateProjectRequest;
import com.flowforge.project.dto.response.ProjectResponse;
import com.flowforge.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        return  ResponseEntity.status(HttpStatus.OK).
                body(new ApiResponse<>(true,"All Project Data Fetch successfully",this.projectService.getAllProjects()));

    }

    @GetMapping("/getProjectById")
    public ResponseEntity<ApiResponse<ProjectResponse>>getProjectByid( @RequestParam UUID id){
        ProjectResponse response = this.projectService.getProjectById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Project Data Fetch successfully",response));
    }

    @PostMapping("/createProject")
    public ResponseEntity<ApiResponse<ProjectResponse>>createProject(  @Valid @RequestBody CreateProjectRequest request){
        ProjectResponse response = this.projectService.createProject(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"Project created successfully",response));
    }

    @PostMapping("/updateProject")
    public ResponseEntity<ApiResponse<ProjectResponse>>updateProject(  @Valid @RequestParam UUID id, @RequestBody UpdateProjectRequest request){
        ProjectResponse response = this.projectService.updateProject(id,request);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Project updated successfully",response));
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @RequestParam UUID id) {

        this.projectService.deleteProject(id);

        return ResponseEntity.noContent().build();
    }

}
