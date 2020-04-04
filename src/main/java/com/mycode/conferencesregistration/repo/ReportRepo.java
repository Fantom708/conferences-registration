package com.mycode.conferencesregistration.repo;

import com.mycode.conferencesregistration.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface ReportRepo extends JpaRepository<Report, Long> {

    List<Report> findByReporterIgnoreCase(String reporter);

    List<Report> findByNameIgnoreCase(String name);
}
