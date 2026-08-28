package com.example.onTime.controllers;

import com.example.onTime.models.Meeting;
import com.example.onTime.models.TodoItem;
import com.example.onTime.repository.MeetingRepository;
import com.example.onTime.repository.TodoItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials="true"
)
public class TodoItemController {

    @Autowired
    private TodoItemRepository todoItemRepo;

    @Autowired
    private MeetingRepository meetingRepo;

    @PostMapping("/add/{meetingId}")
    public ResponseEntity<String> addTodo(@PathVariable Long meetingId,
                                          @RequestBody TodoItem todo,
                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Not logged in");

        Optional<Meeting> meetingOpt = meetingRepo.findById(meetingId);
        if (meetingOpt.isEmpty() || !meetingOpt.get().getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("Access denied to this meeting");
        }

        todo.setMeeting(meetingOpt.get());
        todoItemRepo.save(todo);
        return ResponseEntity.ok("Task added");
    }

    // Get all todos for a meeting
    @GetMapping("/{meetingId}")
    public ResponseEntity<List<TodoItem>> getTodos(@PathVariable Long meetingId) {
        List<TodoItem> items = todoItemRepo.findByMeetingId(meetingId);
        return ResponseEntity.ok(items);
    }

    // Toggle todo completion status
    @PutMapping("/toggle/{todoId}")
    public ResponseEntity<String> toggleComplete(@PathVariable Long todoId) {
        Optional<TodoItem> todoOpt = todoItemRepo.findById(todoId);
        if (todoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
        }

        TodoItem todo = todoOpt.get();
        todo.setCompleted(!todo.isCompleted());
        todoItemRepo.save(todo);
        return ResponseEntity.ok("Status updated");
    }


    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
        if (!todoItemRepo.existsById(todoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
        }

        todoItemRepo.deleteById(todoId);
        return ResponseEntity.ok("Todo deleted");
    }

    @GetMapping("/all-with-todos")
    public ResponseEntity<List<Meeting>> getMeetingsWithTodos(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        List<Meeting> meetings = meetingRepo.findByUserId(userId);
        return ResponseEntity.ok(meetings);
   }

}
