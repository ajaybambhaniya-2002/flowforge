package com.flowforge.project.controller;

import com.flowforge.common.response.ApiResponse;
import com.flowforge.project.dto.request.CreateWorkflowRequest;
import com.flowforge.project.dto.request.UpdateWorkflowRequest;

import com.flowforge.project.dto.response.WorkflowResponse;
import com.flowforge.project.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    // =========================
    // CREATE WORKFLOW
    // =========================

    @PostMapping("/createWorkFlow")
    public ResponseEntity<ApiResponse<WorkflowResponse>> createWorkflow(
            @RequestBody CreateWorkflowRequest request) {

        WorkflowResponse response =
                workflowService.createWorkflow(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"Workflow created successfully",response));
    }


    // =========================
    // GET ALL WORKFLOWS
    // =========================

    @GetMapping("/getAllWorkFlow")
    public ResponseEntity<ApiResponse<List<WorkflowResponse>>> getAllWorkflows() {
        return  ResponseEntity.status(HttpStatus.OK).
                body(new ApiResponse<>(true,"All Workflow Data Fetch successfully", this.workflowService.getAllWorkflows()));
    }


    // =========================
    // GET WORKFLOW BY ID
    // =========================

    @GetMapping("/getWorkFLowById")
    public ResponseEntity<ApiResponse<WorkflowResponse>> getWorkflowById(
            @RequestParam UUID id) {
        WorkflowResponse response =
                workflowService.getWorkflowById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Workflow Data Fetch successfully",response));
    }


    // =========================
    // UPDATE WORKFLOW
    // =========================

    @PostMapping("/updateWorkFlow")
    public ResponseEntity<ApiResponse<WorkflowResponse>> updateWorkflow(
            @RequestParam UUID id,
            @RequestBody UpdateWorkflowRequest request) {
        WorkflowResponse response =
                workflowService.updateWorkflow(id, request);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Workflow updated successfully",response));
    }


    // =========================
    // DELETE WORKFLOW
    // =========================

    @DeleteMapping("/deleteWorkFlow")
    public ResponseEntity<ApiResponse<Void>> deleteWorkflow(
            @RequestParam UUID id) {

        workflowService.deleteWorkflow(id);

        return ResponseEntity.noContent().build();
    }
}