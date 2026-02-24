package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.LeaveRequest;
import enums.LeaveStatus;
import org.springframework.data.jpa.domain.Specification;

public class LeaveSpecification {

    public static Specification<LeaveRequest> filter(
            Long employeeId,
            LeaveStatus status,
            String keyword
    ) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            // Filter theo employeeId
            if (employeeId != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("employee").get("id"), employeeId));
            }

            // Filter theo status
            if (status != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("status"), status));
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

                predicates = cb.and(predicates,
                        cb.or(firstName, lastName));
            }

            return predicates;
        };
    }
}
