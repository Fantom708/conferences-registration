package com.mycode.conferencesregistration.dao;

import com.mycode.conferencesregistration.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Yurii Kovtun
 */
public interface TalkDao extends JpaRepository<Talk, Long> {

    List<Talk> findByReporterIgnoreCase(String reporter);

    List<Talk> findByNameIgnoreCase(String name);
}
