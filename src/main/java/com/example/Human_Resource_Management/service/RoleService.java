package com.example.Human_Resource_Management.service;

import com.example.Human_Resource_Management.entity.Role;
import com.example.Human_Resource_Management.repository.RoleRepository;
import com.example.Human_Resource_Management.specification.RoleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role update(Long id, String name) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(name);
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        roleRepository.delete(role);
    }

    // ✅ Search + Pagination + Sort
    public Page<Role> search(
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        // hạn chế sort field để tránh lỗi
        List<String> allowedSort = List.of("id", "name");
        if (!allowedSort.contains(sortBy)) {
            sortBy = "id";
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Role> spec = RoleSpecification.search(keyword);

        return roleRepository.findAll(spec, pageable);
    }
}
