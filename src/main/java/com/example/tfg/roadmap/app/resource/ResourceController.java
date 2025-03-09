package com.example.tfg.roadmap.app.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/{id}/complete")
    public ResponseEntity<Resource> toggleResourceStatus(@PathVariable Long id) {
        Resource updatedResource = resourceService.toggleIsComplete(id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedResource);
    }
}
