package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.AnnouncementDto.AnnouncementRequest;
import com.example.Human_Resource_Management.dto.AnnouncementDto.AnnouncementResponse;
import com.example.Human_Resource_Management.entity.Announcement;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.repository.AnnouncementRepository;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final EmployeeRepository employeeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public AnnouncementResponse create(AnnouncementRequest request) {

        // Business validation (double safety)
        if (request.getTitle().trim().isEmpty()
                && request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Announcement cannot be empty");
        }

        Employee employee = employeeRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setCreatedBy(employee);

        announcementRepository.save(announcement);

        AnnouncementResponse response = mapToResponse(announcement);

        // 🔥 REALTIME BROADCAST
        messagingTemplate.convertAndSend("/topic/announcements", response);

        return response;
    }

    public List<AnnouncementResponse> getAll() {
        return announcementRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AnnouncementResponse> getByEmployee(Long employeeId) {
        return announcementRepository.findByCreatedById(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AnnouncementResponse mapToResponse(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.getCreatedBy().getFirstName()
                        + " "
                        + announcement.getCreatedBy().getLastName()
        );
    }
}