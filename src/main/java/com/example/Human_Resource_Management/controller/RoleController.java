package com.example.Human_Resource_Management.controller;

import com.example.Human_Resource_Management.entity.Role;
import com.example.Human_Resource_Management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // ✅ Toàn bộ controller chỉ ADMIN
public class RoleController {

    private final RoleService roleService;

    // ✅ ADMIN ONLY
    @PostMapping
    public Role create(@RequestParam String name) {
        return roleService.create(name);
    }

    // ✅ ADMIN ONLY
    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    // ✅ ADMIN ONLY
    @PutMapping("/{id}")
    public Role update(@PathVariable Long id,
                       @RequestParam String name) {
        return roleService.update(id, name);
    }

    // ✅ ADMIN ONLY
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return "Deleted successfully";
    }

    // ✅ ADMIN ONLY
    @GetMapping("/search")
    public Page<Role> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return roleService.search(keyword, page, size, sortBy, sortDir);
    }
}
