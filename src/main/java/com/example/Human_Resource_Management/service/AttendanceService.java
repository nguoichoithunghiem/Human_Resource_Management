package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.AttendanceDto.AttendanceRequest;
import com.example.Human_Resource_Management.dto.AttendanceDto.AttendanceResponse;
import com.example.Human_Resource_Management.entity.Attendance;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.repository.AttendanceRepository;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.security.CustomUserPrincipal;
import com.example.Human_Resource_Management.specification.AttendanceSpecification;
import enums.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceResponse checkIn(AttendanceRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);

        // ✅ set đúng kiểu
        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckIn(now);

        // logic đi trễ (sau 8:30)
        if (now.toLocalTime().isAfter(LocalTime.of(8, 30))) {
            attendance.setStatus(AttendanceStatus.LATE);
        } else {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }

        attendanceRepository.save(attendance);

        return new AttendanceResponse(
                attendance.getId(),
                employee.getFirstName() + " " + employee.getLastName(),
                attendance.getCheckIn() != null ? attendance.getCheckIn().toString() : null,
                attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : null,
                attendance.getStatus() != null ? attendance.getStatus().name() : null
        );
    }

    public AttendanceResponse checkOut(Long attendanceId) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Already checked out");
        }

        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOut(now);

        attendanceRepository.save(attendance);

        return new AttendanceResponse(
                attendance.getId(),
                attendance.getEmployee().getFirstName() + " " +
                        attendance.getEmployee().getLastName(),
                attendance.getCheckIn() != null ? attendance.getCheckIn().toString() : null,
                attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : null,
                attendance.getStatus() != null ? attendance.getStatus().name() : null
        );
    }



    public Page<AttendanceResponse> getAll(
            Long employeeId,
            String status,
            String fromDate,
            String toDate,
            String keyword,
            Pageable pageable
    ) {

        AttendanceStatus attendanceStatus = null;
        LocalDate from = null;
        LocalDate to = null;

        if (status != null) {
            attendanceStatus = AttendanceStatus.valueOf(status.toUpperCase());
        }

        if (fromDate != null) {
            from = LocalDate.parse(fromDate);
        }

        if (toDate != null) {
            to = LocalDate.parse(toDate);
        }

        Specification<Attendance> spec =
                AttendanceSpecification.filter(
                        employeeId,
                        attendanceStatus,
                        from,
                        to,
                        keyword
                );

        return attendanceRepository.findAll(spec, pageable)
                .map(a -> new AttendanceResponse(
                        a.getId(),
                        a.getEmployee().getFirstName() + " " +
                                a.getEmployee().getLastName(),
                        a.getCheckIn() != null ? a.getCheckIn().toString() : null,
                        a.getCheckOut() != null ? a.getCheckOut().toString() : null,
                        a.getStatus() != null ? a.getStatus().name() : null
                ));
    }
    public AttendanceResponse checkInWithSecurity(AttendanceRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();

        // USER chỉ được check-in chính mình
        if (user.hasRole("USER")) {
            if (!request.getEmployeeId().equals(user.getEmployeeId())) {
                throw new AccessDeniedException("Cannot check-in for other employees");
            }
        }

        // MANAGER có thể check cho phòng ban mình
        if (user.hasRole("MANAGER")) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (!employee.getDepartment().getId().equals(user.getDepartmentId())) {
                throw new AccessDeniedException("Access denied");
            }
        }

        return checkIn(request); // gọi logic cũ
    }
    public AttendanceResponse checkOutWithSecurity(Long attendanceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        // USER chỉ check-out bản ghi của mình
        if (user.hasRole("USER")) {
            if (!attendance.getEmployee().getId().equals(user.getEmployeeId())) {
                throw new AccessDeniedException("Access denied");
            }
        }

        // MANAGER chỉ check-out phòng ban mình
        if (user.hasRole("MANAGER")) {
            if (!attendance.getEmployee().getDepartment().getId()
                    .equals(user.getDepartmentId())) {
                throw new AccessDeniedException("Access denied");
            }
        }

        return checkOut(attendanceId); // gọi logic cũ
    }
    public Page<AttendanceResponse> getAllWithSecurity(
            Long employeeId,
            String status,
            String fromDate,
            String toDate,
            String keyword,
            Pageable pageable
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();

        // USER → chỉ xem chính mình
        if (user.hasRole("USER")) {
            employeeId = user.getEmployeeId();
        }

        // MANAGER → chỉ phòng ban mình
        if (user.hasRole("MANAGER")) {
            // override employeeId bằng department filter
            // nếu có employeeId khác phòng ban thì sẽ bị filter trong spec
        }

        return getAll(employeeId, status, fromDate, toDate, keyword, pageable);
    }

}
