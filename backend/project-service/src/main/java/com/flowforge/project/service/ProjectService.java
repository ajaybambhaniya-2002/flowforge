package com.flowforge.project.service;

import com.flowforge.project.dto.request.CreateProjectRequest;
import com.flowforge.project.dto.request.UpdateProjectRequest;
import com.flowforge.project.dto.response.ProjectResponse;
import com.flowforge.project.entity.Project;
import com.flowforge.project.entity.ProjectStatus;
import com.flowforge.project.repository.ProjectRepository;

import com.flowforge.project.security.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;
    public  ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }
    public ProjectResponse createProject(
            CreateProjectRequest request) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long userId =
                authenticatedUser.getUserId();

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setProjectKey(request.getProjectKey());
        project.setStatus(ProjectStatus.ACTIVE);
        project.setOwnerId(userId);

        Project savedProject =
                this.projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        return this.projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id) {

        Project project = this.projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found with id: " + id)
                );

        return mapToResponse(project);
    }
    public ProjectResponse updateProject(UUID id, UpdateProjectRequest request){
        Project project = this.projectRepository.findById(id).orElseThrow(()->
                new RuntimeException("Project not found with id: " + id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        Project updateProject = this.projectRepository.save(project);
        return mapToResponse(updateProject);

    }

    public void deleteProject(UUID id) {

        Project project = this.projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found with id: " + id)
                );

        this.projectRepository.delete(project);
    }

    public ProjectResponse mapToResponse(Project project){
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return  response;

    }




}
