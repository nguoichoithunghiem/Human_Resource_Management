package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Position;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PositionSpecification {

    public static Specification<Position> searchAndFilter(
            String keyword,
            BigDecimal minSalary,
            BigDecimal maxSalary
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // Search name
            if (keyword != null && !keyword.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%"));
            }

            // Filter min salary
            if (minSalary != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("baseSalary"), minSalary));
            }

            // Filter max salary
            if (maxSalary != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("baseSalary"), maxSalary));
            }

            return predicate;
        };
    }
}
