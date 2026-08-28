

package com.example.onTime.repository;

import com.example.onTime.models.Meeting;
import com.example.onTime.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByUser(User user);
    List<Meeting> findByUserId(Long userId);

}

