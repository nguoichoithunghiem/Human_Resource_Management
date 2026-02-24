package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.SalaryHistory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SalaryHistorySpecification {

    public static Specification<SalaryHistory> filter(
            Long employeeId,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // Filter theo employeeId
            if (employeeId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("employee").get("id"), employeeId));
            }

            // Filter theo khoảng ngày
            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("changedDate"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("changedDate"), toDate));
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
