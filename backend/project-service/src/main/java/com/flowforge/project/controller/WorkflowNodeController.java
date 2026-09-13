package com.flowforge.project.controller;

import com.flowforge.common.response.ApiResponse;
import com.flowforge.project.dto.request.CreateWorkflowNodeRequest;
import com.flowforge.project.dto.request.UpdateWorkflowNodeRequest;
import com.flowforge.project.dto.response.WorkflowNodeResponse;
import com.flowforge.project.service.WorkflowNodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflow-nodes")
public class WorkflowNodeController {

    private final WorkflowNodeService workflowNodeService;

    public WorkflowNodeController(
            WorkflowNodeService workflowNodeService) {

        this.workflowNodeService = workflowNodeService;
    }


    // =========================
    // CREATE NODE
    // =========================

    @PostMapping("/createNode")
    public ResponseEntity<ApiResponse<WorkflowNodeResponse>> createNode(
            @RequestParam UUID workflowId,
            @RequestBody CreateWorkflowNodeRequest request) {

        WorkflowNodeResponse response =
                workflowNodeService.createNode(
                        workflowId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Workflow node created successfully",
                                response
                        )
                );
    }


    // =========================
    // GET ALL NODES BY WORKFLOW
    // =========================

    @GetMapping("/getNodesByWorkflow")
    public ResponseEntity<ApiResponse<List<WorkflowNodeResponse>>> getNodesByWorkflow(
            @RequestParam UUID workflowId) {

        List<WorkflowNodeResponse> response =
                workflowNodeService.getNodesByWorkflow(
                        workflowId
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                "Workflow nodes fetched successfully",
                                response
                        )
                );
    }


    // =========================
    // GET NODE BY ID
    // =========================

    @GetMapping("/getNodeById")
    public ResponseEntity<ApiResponse<WorkflowNodeResponse>> getNodeById(
            @RequestParam UUID id) {

        WorkflowNodeResponse response =
                workflowNodeService.getNodeById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                "Workflow node fetched successfully",
                                response
                        )
                );
    }


    // =========================
    // UPDATE NODE
    // =========================

    @PostMapping("/updateNode")
    public ResponseEntity<ApiResponse<WorkflowNodeResponse>> updateNode(
            @RequestParam UUID id,
            @RequestBody UpdateWorkflowNodeRequest request) {

        WorkflowNodeResponse response =
                workflowNodeService.updateNode(
                        id,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                "Workflow node updated successfully",
                                response
                        )
                );
    }


    // =========================
    // DELETE NODE
    // =========================

    @DeleteMapping("/deleteNode")
    public ResponseEntity<ApiResponse<Void>> deleteNode(
            @RequestParam UUID id) {

        workflowNodeService.deleteNode(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}