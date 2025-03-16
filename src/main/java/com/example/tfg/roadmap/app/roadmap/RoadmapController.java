package com.example.tfg.roadmap.app.roadmap;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roadmap")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping
    public ResponseEntity<Roadmap> create(@RequestBody RoadmapDto roadmapDto) { //TODO: OJO, no me esta poniendo el is inittial
        Roadmap savedRoadmap = roadmapService.createRoadmap(roadmapDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoadmap);
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<Roadmap> cloneRoadmap(@PathVariable Long id, @RequestParam Long userId) {
        Roadmap savedRoadmap = roadmapService.cloneRoadmap(id, userId);
        return savedRoadmap.getId() == null? 
        ResponseEntity.notFound().build():
        ResponseEntity.status(HttpStatus.CREATED).body(savedRoadmap);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Roadmap>> getOriginalRoadmaps() {
        List<Roadmap> originalRoadmaps = roadmapService.getOriginalRoadmaps();
        return ResponseEntity.ok(originalRoadmaps);
    }

}
