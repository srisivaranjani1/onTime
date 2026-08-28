package com.example.onTime.repository;

import com.example.onTime.models.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByMeetingId(Long meetingId);
}
