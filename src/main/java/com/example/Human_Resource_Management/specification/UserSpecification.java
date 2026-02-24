package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filter(
            Boolean enabled,
            String role,
            Long employeeId,
            String keyword
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // tránh duplicate do join roles
            query.distinct(true);

            // Filter enabled
            if (enabled != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("enabled"), enabled));
            }

            // Filter employee
            if (employeeId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("employee").get("id"), employeeId));
            }

            // Filter role
            if (role != null && !role.isBlank()) {
                var joinRole = root.join("roles");
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(joinRole.get("name")),
                                role.toLowerCase()));
            }

            // Search username hoặc tên employee
            if (keyword != null && !keyword.isBlank()) {

                var username = cb.like(
                        cb.lower(root.get("username")),
                        "%" + keyword.toLowerCase() + "%"
                );

                var firstName = cb.like(
                        cb.lower(root.get("employee").get("firstName")),
                        "%" + keyword.toLowerCase() + "%"
                );

                var lastName = cb.like(
                        cb.lower(root.get("employee").get("lastName")),
                        "%" + keyword.toLowerCase() + "%"
                );

                predicate = cb.and(predicate,
                        cb.or(username, firstName, lastName));
            }

            return predicate;
        };
    }
}
