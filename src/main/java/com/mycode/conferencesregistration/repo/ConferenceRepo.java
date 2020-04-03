package com.mycode.conferencesregistration.repo;

import com.mycode.conferencesregistration.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Yurii Kovtun
 */
public interface ConferenceRepo extends JpaRepository<Conference, Long> {
    //    Optional<Conference> findById(Long id);
    Optional<Conference> findByNameIgnoreCase(String name);
}
