package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.LeaveRequestDTO.LeaveRequestDTO;
import com.example.Human_Resource_Management.dto.LeaveRequestDTO.LeaveResponse;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.entity.LeaveRequest;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.repository.LeaveRequestRepository;
import com.example.Human_Resource_Management.specification.LeaveSpecification;
import enums.LeaveStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveResponse requestLeave(LeaveRequestDTO requestDTO) {

        // Check date logic
        if (requestDTO.getStartDate()
                .isAfter(requestDTO.getEndDate())) {
            throw new RuntimeException(
                    "Start date cannot be after end date");
        }

        Employee employee = employeeRepository
                .findById(requestDTO.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(requestDTO.getStartDate());
        leave.setEndDate(requestDTO.getEndDate());
        leave.setReason(requestDTO.getReason());
        leave.setStatus(LeaveStatus.PENDING);

        leaveRepository.save(leave);

        return mapToResponse(leave);
    }

    public Page<LeaveResponse> getAll(
            Long employeeId,
            String status,
            String keyword,
            Pageable pageable
    ) {

        LeaveStatus leaveStatus = null;

        if (status != null) {
            try {
                leaveStatus = LeaveStatus
                        .valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid leave status");
            }
        }

        Specification<LeaveRequest> spec =
                LeaveSpecification.filter(
                        employeeId,
                        leaveStatus,
                        keyword
                );

        return leaveRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    private LeaveResponse mapToResponse(LeaveRequest leave) {
        return new LeaveResponse(
                leave.getId(),
                leave.getEmployee().getFirstName() + " " +
                        leave.getEmployee().getLastName(),
                leave.getStartDate().toString(),
                leave.getEndDate().toString(),
                leave.getStatus().name(),
                leave.getReason()
        );
    }
}