package com.flowforge.project.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "workflow_transitions")
//represents connection from one node to another
public class WorkflowTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "workflow_id")
    private UUID workflowId;

    @Column(nullable = false, name = "source_node_id")
    private UUID sourceNodeId;

    @Column(nullable = false, name = "target_node_id")
    private UUID targetNodeId;

    @Column(length = 150)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String conditionExpression;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(UUID workflowId) {
        this.workflowId = workflowId;
    }

    public UUID getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(UUID sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public UUID getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(UUID targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }
}