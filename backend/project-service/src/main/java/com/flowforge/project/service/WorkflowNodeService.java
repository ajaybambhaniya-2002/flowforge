package com.flowforge.project.service;

import com.flowforge.project.dto.request.CreateWorkflowNodeRequest;
import com.flowforge.project.dto.request.UpdateWorkflowNodeRequest;
import com.flowforge.project.dto.response.WorkflowNodeResponse;
import com.flowforge.project.entity.Workflow;
import com.flowforge.project.entity.WorkflowNode;
import com.flowforge.project.exception.ProjectNotFoundException;
import com.flowforge.project.repository.WorkflowNodeRepository;
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
public class WorkflowNodeService {

    private final WorkflowNodeRepository workflowNodeRepository;
    private final WorkflowRepository workflowRepository;

    public WorkflowNodeService(
            WorkflowNodeRepository workflowNodeRepository,
            WorkflowRepository workflowRepository) {

        this.workflowNodeRepository = workflowNodeRepository;
        this.workflowRepository = workflowRepository;
    }

    // =========================
    // CREATE NODE
    // =========================

    public WorkflowNodeResponse createNode(
            UUID workflowId,
            CreateWorkflowNodeRequest request) {

        Workflow workflow = getWorkflowAndValidateAccess(workflowId);

        WorkflowNode node = new WorkflowNode();

        node.setWorkflowId(workflow.getId());
        node.setName(request.getName());
        node.setDescription(request.getDescription());
        node.setNodeType(request.getNodeType());
        node.setPositionX(request.getPositionX());
        node.setPositionY(request.getPositionY());

        WorkflowNode savedNode =
                workflowNodeRepository.save(node);

        return mapToResponse(savedNode);
    }


    // =========================
    // GET ALL NODES
    // =========================

    @Transactional(readOnly = true)
    public List<WorkflowNodeResponse> getNodesByWorkflow(
            UUID workflowId) {

        getWorkflowAndValidateAccess(workflowId);

        return workflowNodeRepository
                .findByWorkflowId(workflowId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================
    // GET NODE BY ID
    // =========================

    @Transactional(readOnly = true)
    public WorkflowNodeResponse getNodeById(UUID id) {

        WorkflowNode node =
                workflowNodeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow node not found with id: " + id
                                ));

        // Validate access through the parent workflow
        getWorkflowAndValidateAccess(node.getWorkflowId());

        return mapToResponse(node);
    }


    // =========================
    // UPDATE NODE
    // =========================

    public WorkflowNodeResponse updateNode(
            UUID id,
            UpdateWorkflowNodeRequest request) {

        WorkflowNode node =
                workflowNodeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow node not found with id: " + id
                                ));

        // USER can update only nodes
        // belonging to their own workflow.
        getWorkflowAndValidateAccess(node.getWorkflowId());

        node.setName(request.getName());
        node.setDescription(request.getDescription());
        node.setNodeType(request.getNodeType());
        node.setPositionX(request.getPositionX());
        node.setPositionY(request.getPositionY());

        WorkflowNode updatedNode =
                workflowNodeRepository.save(node);

        return mapToResponse(updatedNode);
    }


    // =========================
    // DELETE NODE
    // =========================

    public void deleteNode(UUID id) {

        WorkflowNode node =
                workflowNodeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow node not found with id: " + id
                                ));

        // USER can delete only nodes
        // belonging to their own workflow.
        getWorkflowAndValidateAccess(node.getWorkflowId());

        workflowNodeRepository.delete(node);
    }


    // =========================
    // WORKFLOW + ACCESS
    // =========================

    private Workflow getWorkflowAndValidateAccess(
            UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new ProjectNotFoundException(
                                        "Workflow not found with id: "
                                                + workflowId
                                ));

        validateWorkflowAccess(workflow);

        return workflow;
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
    // WORKFLOW ACCESS
    // =========================

    private void validateWorkflowAccess(
            Workflow workflow) {

        // ADMIN can access everything
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

    public WorkflowNodeResponse mapToResponse(
            WorkflowNode node) {

        WorkflowNodeResponse response =
                new WorkflowNodeResponse();

        response.setId(node.getId());
        response.setWorkflowId(node.getWorkflowId());
        response.setName(node.getName());
        response.setDescription(node.getDescription());
        response.setNodeType(node.getNodeType());
        response.setPositionX(node.getPositionX());
        response.setPositionY(node.getPositionY());

        return response;
    }
}