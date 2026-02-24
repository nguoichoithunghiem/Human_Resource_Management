package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Attendance;
import enums.AttendanceStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceSpecification {

    public static Specification<Attendance> filter(
            Long employeeId,
            AttendanceStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // Filter employee
            if (employeeId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("employee").get("id"), employeeId));
            }

            // Filter status
            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            // Filter theo khoảng ngày (dựa trên checkIn)
            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("checkIn"),
                                fromDate.atStartOfDay()
                        ));
            }

            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(
                                root.get("checkIn"),
                                toDate.atTime(23,59,59)
                        ));
            }

            // Search theo tên nhân viên
            if (keyword != null && !keyword.isBlank()) {

                var firstName = cb.like(
                        cb.lower(root.get("employee").get("firstName")),
                        "%" + keyword.toLowerCase() + "%"
                );

                var lastName = cb.like(
                        cb.lower(root.get("employee").get("lastName")),
                        "%" + keyword.toLowerCase() + "%"
                );

                predicate = cb.and(predicate,
                        cb.or(firstName, lastName));
            }

            return predicate;
        };
    }
}
