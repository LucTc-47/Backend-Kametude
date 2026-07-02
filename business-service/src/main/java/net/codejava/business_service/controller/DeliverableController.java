package net.codejava.business_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.codejava.business_service.dto.DeliverableRequest;
import net.codejava.business_service.dto.DeliverableResponse;
import net.codejava.business_service.service.DeliverableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliverables")
@RequiredArgsConstructor
public class DeliverableController {

    private final DeliverableService deliverableService;

    @PostMapping
    public ResponseEntity<DeliverableResponse> submit(@Valid @RequestBody DeliverableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliverableService.submit(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DeliverableResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliverableService.getByOrder(orderId));
    }
}