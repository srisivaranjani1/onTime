package com.example.onTime.services;

import com.example.onTime.models.Meeting;
import com.example.onTime.models.User;
import com.example.onTime.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Meeting createMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public List<Meeting> getMeetingsForUser(User user) {
        return meetingRepository.findByUser(user);
    }
}


