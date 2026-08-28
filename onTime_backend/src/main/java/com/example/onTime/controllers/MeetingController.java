package com.example.onTime.controllers;

import com.example.onTime.dto.MeetingRequest;
import com.example.onTime.models.Meeting;
import com.example.onTime.models.User;
import com.example.onTime.repository.MeetingRepository;
import com.example.onTime.repository.UserRepository;
import com.example.onTime.services.MeetingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@RequestBody MeetingRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            System.out.println("Creating meeting with title: " + request.getTitle());
            System.out.println("Start time: " + request.getStartTime());
            System.out.println("End time: " + request.getEndTime());
            System.out.println("Session userId: " + session.getAttribute("userId"));

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
            }

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Convert DTO to Entity
            Meeting meeting = new Meeting();
            meeting.setTitle(request.getTitle());
            meeting.setDescription(request.getDescription());
            meeting.setStartTime(request.getStartTime());
            meeting.setEndTime(request.getEndTime());
            meeting.setUser(userOpt.get()); // Set the user

            Meeting savedMeeting = meetingService.createMeeting(meeting);
            return ResponseEntity.ok(savedMeeting);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create meeting");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeeting(@PathVariable Long id) {
        meetingRepository.deleteById(id);
        return ResponseEntity.ok("Meeting deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Optional<Meeting> existingMeeting = meetingRepository.findById(id);
        if (existingMeeting.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Meeting meeting = existingMeeting.get();
        if (updates.containsKey("title")) {
            meeting.setTitle(updates.get("title"));
        }
        if (updates.containsKey("description")) {
            meeting.setDescription(updates.get("description"));
        }

        meetingRepository.save(meeting);
        return ResponseEntity.ok(meeting);
    }


    @GetMapping("/my")
public ResponseEntity<?> getMyMeetings(HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) return ResponseEntity.status(401).body("User not logged in");

    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

    List<Meeting> meetings = meetingService.getMeetingsForUser(userOpt.get());
    return ResponseEntity.ok(meetings);
}

}


