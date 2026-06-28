package cm.kametud.requestservice.controller;

import cm.kametud.requestservice.dto.CreateRequestDTO;
import cm.kametud.requestservice.dto.GigRequestDTO;
import cm.kametud.requestservice.model.GigRequest;
import cm.kametud.requestservice.service.GigRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GigRequestController {

    private final GigRequestService service;

    @PostMapping
    public ResponseEntity<GigRequest> create(@Valid @RequestBody CreateRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GigRequestDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GigRequestDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<GigRequest> close(@PathVariable UUID id) {
        return ResponseEntity.ok(service.closeRequest(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}