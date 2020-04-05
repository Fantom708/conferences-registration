package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Yurii Kovtun
 */
@Service
public class ConferenceService {

    private final ConferenceRepo conferenceRepo;

    public ConferenceService(ConferenceRepo conferenceRepo) {
        this.conferenceRepo = conferenceRepo;
    }

    public List<Conference> findAll() {
        return conferenceRepo.findAll();
    }

    public Optional<Conference> getConference(Long id) {
        return conferenceRepo.findById(id);
    }

    @Transactional
    public Conference addConference(Conference conference) {
        checkConference(conference);

        return conferenceRepo.save(conference);
    }

    @Transactional
    public Conference editConference(Long id, Conference newConference) {
        Conference conference = getConference(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        checkConference(conference);

        BeanUtils.copyProperties(newConference, conference, "id", "reports");

        return conferenceRepo.save(conference);
    }

    private void checkConference(Conference conference) {
        // Перевірка унікальності назви конференції
        if (!conferenceRepo.findByNameIgnoreCase(conference.getName()).isEmpty()) {
            throw new ExistsConferenceNameException(conference.getName());
        }

        //Перевірка вільної дати проведення конференції
        if (!conferenceRepo.findByDateStart(conference.getDateStart()).isEmpty()) {
            throw new DateConferenceIsBusyException(conference.getDateStart());
        }
    }

}
