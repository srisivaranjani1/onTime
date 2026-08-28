package com.example.onTime.controllers;

import com.example.onTime.models.Meeting;
import com.example.onTime.models.TodoItem;
import com.example.onTime.repository.MeetingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private MeetingRepository meetingRepository;

    @GetMapping
    public ResponseEntity<?> getAnalytics(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body("User not logged in");
        }

        List<Meeting> meetings =
                meetingRepository.findByUserId(userId);


        int totalMeetings = meetings.size();

        double totalHours = 0;

        int completedTodos = 0;
        int pendingTodos = 0;


        Map<LocalDate, Integer> meetingsPerDay =
                new TreeMap<>();

        Map<LocalDate, Double> hoursPerDay =
                new TreeMap<>();

        for (Meeting meeting : meetings) {

            if (meeting.getStartTime() == null ||
                    meeting.getEndTime() == null) {
                continue;
            }

            LocalDate date =
                    meeting.getStartTime().toLocalDate();

            // Meeting count
            meetingsPerDay.put(
                    date,
                    meetingsPerDay.getOrDefault(date, 0) + 1
            );

            // Meeting duration
            long minutes = Duration.between(
                    meeting.getStartTime(),
                    meeting.getEndTime()
            ).toMinutes();

            double hours = minutes / 60.0;

            totalHours += hours;

            hoursPerDay.put(
                    date,
                    hoursPerDay.getOrDefault(date, 0.0) + hours
            );


            List<TodoItem> todos =
                    meeting.getTodoItems();

            if (todos != null) {

                for (TodoItem todo : todos) {

                    if (todo.isCompleted()) {
                        completedTodos++;
                    } else {
                        pendingTodos++;
                    }
                }
            }
        }

        List<Map<String, Object>> dailyData =
                new ArrayList<>();

        for (LocalDate date : meetingsPerDay.keySet()) {

            Map<String, Object> dayData =
                    new LinkedHashMap<>();

            dayData.put("date", date.toString());

            dayData.put(
                    "day",
                    date.getDayOfWeek()
                            .toString()
                            .substring(0, 3)
            );

            dayData.put(
                    "meetings",
                    meetingsPerDay.get(date)
            );

            dayData.put(
                    "hours",
                    Math.round(
                            hoursPerDay
                                    .getOrDefault(date, 0.0)
                                    * 100.0
                    ) / 100.0
            );

            dailyData.add(dayData);
        }

        String mostProductiveDay = "N/A";

        double maxHours = 0;

        for (Map.Entry<LocalDate, Double> entry :
                hoursPerDay.entrySet()) {

            if (entry.getValue() > maxHours) {

                maxHours = entry.getValue();

                mostProductiveDay =
                        entry.getKey()
                                .getDayOfWeek()
                                .toString();
            }
        }


        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "totalMeetings",
                totalMeetings
        );

        response.put(
                "totalHours",
                Math.round(totalHours * 100.0) / 100.0
        );

        response.put(
                "completedTodos",
                completedTodos
        );

        response.put(
                "pendingTodos",
                pendingTodos
        );

        response.put(
                "mostProductiveDay",
                mostProductiveDay
        );

        response.put(
                "dailyData",
                dailyData
        );

        return ResponseEntity.ok(response);
    }
}