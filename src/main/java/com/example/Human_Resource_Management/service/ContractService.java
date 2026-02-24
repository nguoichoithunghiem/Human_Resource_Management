package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.ContractDto.ContractRequest;
import com.example.Human_Resource_Management.dto.ContractDto.ContractResponse;
import com.example.Human_Resource_Management.entity.Contract;
import com.example.Human_Resource_Management.entity.Employee;
import com.example.Human_Resource_Management.repository.ContractRepository;
import com.example.Human_Resource_Management.repository.EmployeeRepository;
import com.example.Human_Resource_Management.specification.ContractSpecification;
import enums.ContractStatus;
import enums.ContractType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;

    public ContractResponse create(ContractRequest request) {

        if (request.getEndDate() != null &&
                request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException(
                    "Start date cannot be after end date");
        }

        Employee employee = employeeRepository.findById(
                        request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        ContractType type;
        try {
            type = ContractType.valueOf(
                    request.getContractType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid contract type");
        }

        Contract contract = new Contract();
        contract.setEmployee(employee);
        contract.setContractType(type);
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setSalary(request.getSalary());
        contract.setStatus(ContractStatus.ACTIVE);

        contractRepository.save(contract);

        return mapToResponse(contract);
    }

    public Page<ContractResponse> getAll(
            Long employeeId,
            String status,
            String contractType,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword,
            Pageable pageable
    ) {

        if (fromDate != null && toDate != null &&
                fromDate.isAfter(toDate)) {
            throw new RuntimeException(
                    "fromDate cannot be after toDate");
        }

        ContractStatus contractStatus = null;
        ContractType type = null;

        if (status != null) {
            try {
                contractStatus =
                        ContractStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid contract status");
            }
        }

        if (contractType != null) {
            try {
                type =
                        ContractType.valueOf(contractType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid contract type");
            }
        }

        Specification<Contract> spec =
                ContractSpecification.filter(
                        employeeId,
                        contractStatus,
                        type,
                        fromDate,
                        toDate,
                        keyword
                );

        return contractRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    private ContractResponse mapToResponse(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getEmployee().getFirstName() + " " +
                        contract.getEmployee().getLastName(),
                contract.getContractType().name(),
                contract.getStartDate().toString(),
                contract.getEndDate() != null
                        ? contract.getEndDate().toString()
                        : null,
                contract.getStatus().name()
        );
    }
}