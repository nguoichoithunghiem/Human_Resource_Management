package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    public static Specification<Role> search(String keyword) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (keyword != null && !keyword.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
