package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Employee;
import enums.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> searchAndFilter(
            String keyword,
            EmployeeStatus status,
            Long departmentId
    ) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            // Search keyword
            if (keyword != null && !keyword.isBlank()) {
                var searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("firstName")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("lastName")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("employeeCode")), "%" + keyword.toLowerCase() + "%")
                );
                predicates = cb.and(predicates, searchPredicate);
            }

            // Filter by status
            if (status != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("status"), status));
            }

            // Filter by department
            if (departmentId != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("department").get("id"), departmentId));
            }

            return predicates;
        };
    }
}
