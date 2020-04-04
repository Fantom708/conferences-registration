package com.mycode.conferencesregistration.repo;

import com.mycode.conferencesregistration.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface ConferenceRepo extends JpaRepository<Conference, Long> {

    List<Conference> findByNameIgnoreCase(String name);

    List<Conference> findByDateStart(LocalDate date);
}
