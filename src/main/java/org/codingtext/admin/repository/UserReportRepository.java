package org.codingtext.admin.repository;

import org.codingtext.admin.domain.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
