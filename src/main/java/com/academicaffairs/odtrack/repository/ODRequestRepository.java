package com.academicaffairs.odtrack.repository;

import com.academicaffairs.odtrack.entity.ODRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ODRequestRepository extends JpaRepository<ODRequest, Long> {
}
