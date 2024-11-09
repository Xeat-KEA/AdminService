package org.codingtext.admin.repository;

import org.codingtext.admin.domain.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {
}
