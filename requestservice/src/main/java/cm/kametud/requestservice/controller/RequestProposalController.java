package cm.kametud.requestservice.controller;

import cm.kametud.requestservice.dto.CreateProposalDTO;
import cm.kametud.requestservice.dto.ProposalDTO;
import cm.kametud.requestservice.model.RequestProposal;
import cm.kametud.requestservice.service.RequestProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/proposals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestProposalController {

    private final RequestProposalService service;

    @PostMapping
    public ResponseEntity<RequestProposal> create(@Valid @RequestBody CreateProposalDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ProposalDTO>> getByRequest(@PathVariable UUID requestId) {
        return ResponseEntity.ok(service.getByRequestId(requestId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProposalDTO>> getByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(service.getByStudentId(studentId));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ProposalDTO> accept(@PathVariable UUID id) {
        return ResponseEntity.ok(service.acceptProposal(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        service.rejectProposal(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}