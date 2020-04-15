package com.mycode.conferencesregistration.service;

import com.mycode.conferencesregistration.config.ConferenceSettings;
import com.mycode.conferencesregistration.dao.ConferenceDao;
import com.mycode.conferencesregistration.dao.TalkDao;
import com.mycode.conferencesregistration.domain.Conference;
import com.mycode.conferencesregistration.domain.Talk;
import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.domain.dto.TalkDto;
import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import com.mycode.conferencesregistration.exception.LimitByNewTalkWasReachedException;
import com.mycode.conferencesregistration.exception.RegistrationDateWasLateException;
import com.mycode.conferencesregistration.exception.TalkNameExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yurii Kovtun
 */
@Service
@RequiredArgsConstructor
public class TalkServiceImpl implements TalkService {

    private final TalkDao talkDao;
    private final ConferenceDao conferenceDao;
    private final ConferenceSettings conferenceSettings;


    @Override
    public Set<TalkDto> getTalks(Long idConference) {

        Conference conference = conferenceDao.findById(idConference)
                .orElseThrow(() -> new ConferenceNotFoundException(idConference));

        return conference.getTalks()
                .stream()
                .map(item -> item.toDto())
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public long addTalkToConference(Long id, TalkCreationRequest report) {

        Conference conference = conferenceDao.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        // Перевірка унікальності назви звіту
        if (!talkDao.findByNameIgnoreCase(report.getName()).isEmpty()) {
            throw new TalkNameExistsException(report.getName());
        }

        // Перевірка максимально допустимої кількості звітів по звітуючому
        if (talkDao.findByReporterIgnoreCase(report.getReporter()).size() >= conferenceSettings.getMaxUserTalks()) {
            throw new LimitByNewTalkWasReachedException(report.getReporter(), conferenceSettings.getMaxUserTalks());
        }

        // Перевірка дати подачі заявки на участь у конференції
        if (ChronoUnit.DAYS.between(LocalDate.now(), conference.getDateStart()) <= conferenceSettings.getDayToConferenceRegistration()) {
            throw new RegistrationDateWasLateException(conferenceSettings.getDayToConferenceRegistration());
        }

        Talk dbTalk = new Talk(report.getName(), report.getDescription(), report.getTypeTalk(), report.getReporter());
        conference.getTalks().add(dbTalk);

        return talkDao.save(dbTalk).getId();
    }

}
