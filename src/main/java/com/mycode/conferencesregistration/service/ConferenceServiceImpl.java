package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.dao.ConferenceDao;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.ConferenceDto;
import com.mycode.conferencesregistration.domain.dto.ConferenceUpdateRequest;
import com.mycode.conferencesregistration.exception.ConferenceDateBusyException;
import com.mycode.conferencesregistration.exception.ConferenceNameExistsException;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yurii Kovtun
 */
@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceDao conferenceDao;

    @Override
    public List<ConferenceDto> findAll() {
        return conferenceDao.findAll()
                .stream()
                .map(item -> item.toDto())
                .collect(Collectors.toList());
    }

    @Override
    public long addConference(ConferenceCreationRequest conference) {
        checkConferenceName(conference.getName());
        checkConferenceDate(conference.getDateStart());

        Conference dbConference = new Conference(
                conference.getName(),
                conference.getTopic(),
                conference.getDateStart(),
                conference.getAmountParticipants());

        return conferenceDao.save(dbConference).getId();
    }

    @Override
    public void editConference(Long id, ConferenceUpdateRequest newConference) {
        Conference conference = conferenceDao.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        if (!conference.getName().equals(newConference.getName())) {
            checkConferenceName(newConference.getName());
        }
        if (!conference.getDateStart().equals(newConference.getDateStart())) {
            checkConferenceDate(newConference.getDateStart());
        }

        BeanUtils.copyProperties(newConference, conference);

        conferenceDao.save(conference);
    }

    private void checkConferenceName(String name) {
        // Перевірка унікальності назви конференції
        if (!conferenceDao.findByNameIgnoreCase(name).isEmpty()) {
            throw new ConferenceNameExistsException(name);
        }
    }

    private void checkConferenceDate(LocalDate date) {
        //Перевірка вільної дати проведення конференції
        if (!conferenceDao.findByDateStart(date).isEmpty()) {
            throw new ConferenceDateBusyException(date);
        }
    }

}
