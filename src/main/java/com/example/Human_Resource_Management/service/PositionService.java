package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.dto.PositionDto.PositionRequest;
import com.example.Human_Resource_Management.dto.PositionDto.PositionResponse;
import com.example.Human_Resource_Management.entity.Position;
import com.example.Human_Resource_Management.repository.PositionRepository;
import com.example.Human_Resource_Management.specification.PositionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    private PositionResponse mapToResponse(Position p) {
        return new PositionResponse(
                p.getId(),
                p.getName(),
                p.getBaseSalary()
        );
    }

    public PositionResponse create(PositionRequest request) {

        Position position = new Position();
        position.setName(request.getName());
        position.setBaseSalary(request.getBaseSalary());

        positionRepository.save(position);

        return mapToResponse(position);
    }

    public List<PositionResponse> getAll() {
        return positionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PositionResponse getById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        return mapToResponse(position);
    }

    public PositionResponse update(Long id, PositionRequest request) {

        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        position.setName(request.getName());
        position.setBaseSalary(request.getBaseSalary());

        positionRepository.save(position);

        return mapToResponse(position);
    }

    public void delete(Long id) {
        positionRepository.deleteById(id);
    }

    // ✅ Pagination + Search + Filter
    public Page<PositionResponse> search(
            String keyword,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Position> spec =
                PositionSpecification.searchAndFilter(keyword, minSalary, maxSalary);

        Page<Position> result = positionRepository.findAll(spec, pageable);

        return result.map(this::mapToResponse);
    }
}
