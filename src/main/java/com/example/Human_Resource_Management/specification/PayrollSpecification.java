package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Payroll;
import enums.PayrollStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PayrollSpecification {

    public static Specification<Payroll> searchAndFilter(
            String keyword,
            PayrollStatus status,
            Integer month,
            Integer year,
            Long employeeId,
            BigDecimal minNetSalary,
            BigDecimal maxNetSalary
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            var employeeJoin = root.join("employee");

            // Search theo tên nhân viên
            if (keyword != null && !keyword.isBlank()) {
                var search = cb.or(
                        cb.like(cb.lower(employeeJoin.get("firstName")),
                                "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(employeeJoin.get("lastName")),
                                "%" + keyword.toLowerCase() + "%")
                );
                predicate = cb.and(predicate, search);
            }

            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            if (month != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("month"), month));
            }

            if (year != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("year"), year));
            }

            if (employeeId != null) {
                predicate = cb.and(predicate,
                        cb.equal(employeeJoin.get("id"), employeeId));
            }

            if (minNetSalary != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("netSalary"), minNetSalary));
            }

            if (maxNetSalary != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("netSalary"), maxNetSalary));
            }

            return predicate;
        };
    }
}
