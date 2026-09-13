package com.flowforge.project.repository;

import com.flowforge.project.entity.WorkflowNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowNodeRepository
        extends JpaRepository<WorkflowNode, UUID> {

    List<WorkflowNode> findByWorkflowId(UUID workflowId);
}