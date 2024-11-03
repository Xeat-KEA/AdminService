package org.codingtext.admin.repository;

import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    List<Admin> findByAdminRole(AdminRole adminRole);
}
