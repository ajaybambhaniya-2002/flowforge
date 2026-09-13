package com.flowforge.project.repository;

import com.flowforge.project.entity.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowTransitionRepository
        extends JpaRepository<WorkflowTransition, UUID> {

    List<WorkflowTransition> findByWorkflowId(UUID workflowId);
}