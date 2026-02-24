package com.example.Human_Resource_Management.specification;

import com.example.Human_Resource_Management.entity.Contract;
import enums.ContractStatus;
import enums.ContractType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContractSpecification {

    public static Specification<Contract> filter(
            Long employeeId,
            ContractStatus status,
            ContractType contractType,
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

            // Filter contractType
            if (contractType != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("contractType"), contractType));
            }

            // Filter theo khoảng ngày startDate
            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("startDate"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("startDate"), toDate));
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
