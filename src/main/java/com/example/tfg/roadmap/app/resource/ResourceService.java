package com.example.tfg.roadmap.app.resource;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public Resource toggleIsComplete(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        // Toggle the status
        resource.setComplete(!resource.isComplete());
        return resourceRepository.save(resource);
    }
}
