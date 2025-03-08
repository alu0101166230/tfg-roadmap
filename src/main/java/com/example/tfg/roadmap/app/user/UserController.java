package com.example.tfg.roadmap.app.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tfg.roadmap.app.roadmap.RoadmapWithPercentageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

@GetMapping("/{id}/roadmap")
public ResponseEntity<List<RoadmapWithPercentageDto>> getUserRoadmaps(@PathVariable Long id) {
    List<RoadmapWithPercentageDto> result = userService.getUserRoadmaps(id);
    return result.isEmpty()?
    ResponseEntity.notFound().build() : 
    ResponseEntity.ok(result);
}





}
