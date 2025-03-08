package com.example.tfg.roadmap.app.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.tfg.roadmap.app.roadmap.Roadmap;
import com.example.tfg.roadmap.app.roadmap.RoadmapMapper;

import lombok.RequiredArgsConstructor;

import com.example.tfg.roadmap.app.roadmap.RoadmapWithPercentageDto;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;

        public List<RoadmapWithPercentageDto> getUserRoadmaps(Long userId){
           RoadmapMapper mapper = new RoadmapMapper();
            Optional<User> user =  userRepository.findById(userId);
            

            if (user.isPresent()) {
                return user.get()
                .getRoadmaps()
                .stream()
                .filter(roadmap -> !roadmap.isOriginal())
                .map(mapper::mapToRoadmapWithPercentageDto)
                .collect(Collectors.toList());
            } 
            
            return new ArrayList<>();
        }

  

}
