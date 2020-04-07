package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import com.mycode.conferencesregistration.exceptions.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exceptions.DateConferenceIsBusyException;
import com.mycode.conferencesregistration.exceptions.ExistsConferenceNameException;
import com.mycode.conferencesregistration.repo.ConferenceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Yurii Kovtun
 */
@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepo conferenceRepo;

    @Override
    public List<Conference> findAll() {
        return conferenceRepo.findAll();
    }

    @Override
    public long addConference(ConferenceDto conference) {

        checkConferenceName(conference.getName());
        checkConferenceDate(conference.getDateStart());

        Conference dbConference = new Conference(
                conference.getName(),
                conference.getTopic(),
                conference.getDateStart(),
                conference.getAmountParticipants());

        return conferenceRepo.save(dbConference).getId();
    }

    @Override
    public void editConference(Long id, ConferenceDto newConference) {
        Conference conference = getConference(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        if (!conference.getName().equals(newConference.getName())) {
            checkConferenceName(newConference.getName());
        }
        if (!conference.getDateStart().equals(newConference.getDateStart())) {
            checkConferenceDate(newConference.getDateStart());
        }

        BeanUtils.copyProperties(newConference, conference, "id", "reports");

        conferenceRepo.save(conference);
    }

    private Optional<Conference> getConference(Long id) {
        return conferenceRepo.findById(id);
    }

    private void checkConferenceName(String name) {

        // Перевірка унікальності назви конференції
        if (!conferenceRepo.findByNameIgnoreCase(name).isEmpty()) {
            throw new ExistsConferenceNameException(name);
        }
    }

    private void checkConferenceDate(LocalDate date) {

        //Перевірка вільної дати проведення конференції
        if (!conferenceRepo.findByDateStart(date).isEmpty()) {
            throw new DateConferenceIsBusyException(date);
        }
    }

}
