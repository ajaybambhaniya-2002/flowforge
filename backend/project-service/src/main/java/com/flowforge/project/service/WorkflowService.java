package com.flowforge.project.service;

import com.flowforge.project.dto.request.CreateWorkflowRequest;
import com.flowforge.project.dto.request.UpdateWorkflowRequest;
import com.flowforge.project.dto.response.WorkflowResponse;
import com.flowforge.project.entity.Workflow;
import com.flowforge.project.entity.WorkflowStatus;
import com.flowforge.project.repository.WorkflowRepository;
import com.flowforge.project.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }


    // =========================
    // CREATE WORKFLOW
    // =========================

    public WorkflowResponse createWorkflow(
            CreateWorkflowRequest request) {

        Long userId = getAuthenticatedUserId();

        Workflow workflow = new Workflow();

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());

        // New workflow always starts as DRAFT
        workflow.setStatus(WorkflowStatus.DRAFT);

        // First version
        workflow.setVersion(1);

        // Owner / creator
        workflow.setCreatedBy(userId);

        Workflow savedWorkflow =
                workflowRepository.save(workflow);

        return mapToResponse(savedWorkflow);
    }


    // =========================
    // GET ALL WORKFLOWS
    // =========================

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getAllWorkflows() {

        List<Workflow> workflows;

        if (isAdmin()) {

            workflows = workflowRepository.findAll();

        } else {

            Long userId = getAuthenticatedUserId();

            workflows =
                    workflowRepository.findByCreatedBy(userId);
        }

        return workflows.stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================
    // GET WORKFLOW BY ID
    // =========================

    @Transactional(readOnly = true)
    public WorkflowResponse getWorkflowById(UUID id) {

        Workflow workflow =
                workflowRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow not found with id: " + id
                                )
                        );

        // ADMIN can access all workflows
        validateWorkflowAccess(workflow);

        return mapToResponse(workflow);
    }


    // =========================
    // UPDATE WORKFLOW
    // =========================

    public WorkflowResponse updateWorkflow(
            UUID id,
            UpdateWorkflowRequest request) {

        Workflow workflow =
                workflowRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow not found with id: " + id
                                )
                        );

        // ADMIN can update any workflow
        // USER can update only own workflow
        validateWorkflowAccess(workflow);

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());

        /*
         * For now we DON'T allow changing status/version
         * through normal update.
         *
         * Publishing/versioning will be implemented separately.
         */

        Workflow updatedWorkflow =
                workflowRepository.save(workflow);

        return mapToResponse(updatedWorkflow);
    }


    // =========================
    // DELETE WORKFLOW
    // =========================

    public void deleteWorkflow(UUID id) {

        Workflow workflow =
                workflowRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow not found with id: " + id
                                )
                        );

        // ADMIN can delete any workflow
        // USER can delete only own workflow
        validateWorkflowAccess(workflow);

        workflowRepository.delete(workflow);
    }


    // =========================
    // AUTHENTICATED USER
    // =========================

    private Long getAuthenticatedUserId() {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return authenticatedUser.getUserId();
    }


    // =========================
    // ADMIN CHECK
    // =========================

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


    // =========================
    // ACCESS VALIDATION
    // =========================

    private void validateWorkflowAccess(
            Workflow workflow) {

        // ADMIN can access any workflow
        if (isAdmin()) {
            return;
        }

        Long userId = getAuthenticatedUserId();

        // USER can access only their own workflow
        if (!workflow.getCreatedBy().equals(userId)) {

            throw new AccessDeniedException(
                    "You do not have access to this workflow"
            );
        }
    }


    // =========================
    // ENTITY → RESPONSE
    // =========================

    public WorkflowResponse mapToResponse(
            Workflow workflow) {

        WorkflowResponse response =
                new WorkflowResponse();

        response.setId(workflow.getId());
        response.setName(workflow.getName());
        response.setDescription(workflow.getDescription());
        response.setStatus(workflow.getStatus());
        response.setVersion(workflow.getVersion());
        response.setCreatedBy(workflow.getCreatedBy());
        response.setCreatedAt(workflow.getCreatedAt());
        response.setUpdatedAt(workflow.getUpdatedAt());

        return response;
    }
}