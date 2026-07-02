package net.codejava.business_service.controller;

import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.OrderResponse;
import net.codejava.business_service.service.OrderWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderWorkflowController {

    private final OrderWorkflowService workflowService;

    @PatchMapping("/{id}/accept")
    public ResponseEntity<OrderResponse> accept(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.accept(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<OrderResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.reject(id));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<OrderResponse> start(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.start(id));
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.deliver(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.complete(id));
    }

    @PatchMapping("/{id}/request-revision")
    public ResponseEntity<OrderResponse> requestRevision(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.requestRevision(id));
    }

    @PatchMapping("/{id}/dispute")
    public ResponseEntity<OrderResponse> dispute(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.dispute(id));
    }
}