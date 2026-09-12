package com.flowforge.project.service;

import com.flowforge.project.dto.request.CreateProjectRequest;
import com.flowforge.project.dto.request.UpdateProjectRequest;
import com.flowforge.project.dto.response.ProjectResponse;
import com.flowforge.project.entity.Project;
import com.flowforge.project.entity.ProjectStatus;
import com.flowforge.project.exception.ProjectNotFoundException;
import com.flowforge.project.repository.ProjectRepository;

import com.flowforge.project.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
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

        List<Project> projects;

        if (isAdmin()) {

            projects = projectRepository.findAll();

        } else {

            Long userId = getAuthenticatedUserId();

            projects = projectRepository
                    .findByOwnerId(userId);
        }

        return projects.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id) {

        Project project = this.projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id
                        )
                );

        // ADMIN can access all projects
        validateProjectAccess(project);

        return mapToResponse(project);
    }


    public ProjectResponse updateProject(
            UUID id,
            UpdateProjectRequest request) {

        Project project =
                this.projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ProjectNotFoundException(
                                        "Project not found with id: " + id
                                ));

        // ADMIN can update any project
        validateProjectAccess(project);

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());

        Project updatedProject =
                this.projectRepository.save(project);

        return mapToResponse(updatedProject);
    }





    public void deleteProject(UUID id) {

        Project project =
                this.projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ProjectNotFoundException(
                                        "Project not found with id: " + id
                                ));

        // ADMIN can delete any project
        validateProjectAccess(project);

        this.projectRepository.delete(project);
    }
    private Long getAuthenticatedUserId() {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return authenticatedUser.getUserId();
    }
    private boolean isAdmin() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }
        public ProjectResponse mapToResponse(Project project){
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return  response;

    }
    private void validateProjectAccess(Project project) {

        // ADMIN can access any project
        if (isAdmin()) {
            return;
        }

        // USER can access only their own project
        Long userId = getAuthenticatedUserId();

        if (!project.getOwnerId().equals(userId)) {

            throw new AccessDeniedException(
                    "You do not have access to this project"
            );
        }
    }



}
