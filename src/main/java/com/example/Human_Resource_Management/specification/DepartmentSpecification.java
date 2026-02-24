package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Department;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    public static Specification<Department> searchAndFilter(
            String keyword,
            Integer minEmployees,
            Integer maxEmployees
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // Join employees
            var employeesJoin = root.join("employees", jakarta.persistence.criteria.JoinType.LEFT);

            // Tránh duplicate do join
            query.distinct(true);

            // Search keyword
            if (keyword != null && !keyword.isBlank()) {
                var searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")),
                                "%" + keyword.toLowerCase() + "%")
                );
                predicate = cb.and(predicate, searchPredicate);
            }

            // Count employees
            if (minEmployees != null || maxEmployees != null) {

                query.groupBy(root.get("id"));

                var countExp = cb.count(employeesJoin.get("id"));

                if (minEmployees != null) {
                    query.having(cb.ge(countExp, minEmployees));
                }

                if (maxEmployees != null) {
                    query.having(cb.le(countExp, maxEmployees));
                }
            }

            return predicate;
        };
    }
}
