package com.kametude.request_service.controller;

import com.kametude.request_service.dto.request.CreateJobRequestDTO;
import com.kametude.request_service.dto.request.UpdateJobRequestDTO;
import com.kametude.request_service.dto.response.JobRequestResponseDTO;
import com.kametude.request_service.service.JobRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-requests")
@RequiredArgsConstructor
public class JobRequestController {

    private final JobRequestService jobRequestService;

    @PostMapping("/client/{clientId}")
    public ResponseEntity<JobRequestResponseDTO> create(
            @PathVariable Long clientId,
            @RequestBody CreateJobRequestDTO dto) {
        return ResponseEntity.ok(jobRequestService.create(clientId, dto));
    }

    @GetMapping
    public ResponseEntity<List<JobRequestResponseDTO>> getAllOpen() {
        return ResponseEntity.ok(jobRequestService.getAllOpen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRequestResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobRequestService.getById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<JobRequestResponseDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(jobRequestService.getByClient(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobRequestResponseDTO> update(
            @PathVariable Long id,
            @RequestBody UpdateJobRequestDTO dto) {
        return ResponseEntity.ok(jobRequestService.update(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<JobRequestResponseDTO> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(jobRequestService.cancel(id));
    }

    @PatchMapping("/{id}/reopen")
    public ResponseEntity<JobRequestResponseDTO> reopen(@PathVariable Long id) {
        return ResponseEntity.ok(jobRequestService.reopen(id));
    }
}