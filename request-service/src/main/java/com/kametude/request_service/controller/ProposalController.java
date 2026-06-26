package com.kametude.request_service.controller;

import com.kametude.request_service.dto.request.CreateProposalDTO;
import com.kametude.request_service.dto.response.ProposalResponseDTO;
import com.kametude.request_service.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping("/job-request/{jobRequestId}")
    public ResponseEntity<ProposalResponseDTO> submit(
            @PathVariable Long jobRequestId,
            @RequestBody CreateProposalDTO dto) {
        return ResponseEntity.ok(proposalService.submit(jobRequestId, dto));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ProposalResponseDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(proposalService.accept(id));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ProposalResponseDTO> withdraw(@PathVariable Long id) {
        return ResponseEntity.ok(proposalService.withdraw(id));
    }

    @GetMapping("/job-request/{jobRequestId}")
    public ResponseEntity<List<ProposalResponseDTO>> getByJobRequest(@PathVariable Long jobRequestId) {
        return ResponseEntity.ok(proposalService.getByJobRequest(jobRequestId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProposalResponseDTO>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(proposalService.getByStudent(studentId));
    }
}