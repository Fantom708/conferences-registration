package com.mycode.conferencesregistration;

import com.mycode.conferencesregistration.api.TalkRestController;
import com.mycode.conferencesregistration.domain.TypeTalk;
import com.mycode.conferencesregistration.domain.dto.ConferenceCreationRequest;
import com.mycode.conferencesregistration.domain.dto.TalkCreationRequest;
import com.mycode.conferencesregistration.service.ConferenceService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Yurii Kovtun
 */
@Component
@AllArgsConstructor
@Profile("dev")
public class DemoRunner implements ApplicationRunner {

    private final ConferenceService conferenceService;
    private final TalkRestController talkRestController;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.getSourceArgs().length > 0 && args.getSourceArgs()[0].equals("demo")) {
            System.out.println("ADD demo date.....");

            long id = conferenceService.addConference(new ConferenceCreationRequest("MEDICAL DEVICE FORUM 2020",
                    "Влияние Регламента о медицинских изделиях (MDR) на производителей. Новые обязательства для импортеров и дистрибьюторов.",
                    LocalDate.of(2020, 8, 5),
                    200));
            talkRestController.addTalk(new TalkCreationRequest("Новый регламент MDR", "Новые роли и их обязательства в соответствии с MDR",
                    TypeTalk.REPORT, "Popov N.K."), id);
            talkRestController.addTalk(new TalkCreationRequest("COVID-19", "Що треба робити, якщо ви заражені",
                    TypeTalk.WORKSHOP, "Ivanov A.N."), id);

            conferenceService.addConference(new ConferenceCreationRequest("Курс Java Developer",
                    "Онлайн конференция: как пережить весну 2020",
                    LocalDate.of(2020, 8, 1),
                    100));
            conferenceService.addConference(new ConferenceCreationRequest("SPRING BOOT. REST API and etc.",
                    "Онлайн конференция: как пережить весну 2020",
                    LocalDate.of(2020, 8, 15),
                    500));
            conferenceService.addConference(new ConferenceCreationRequest("Вебінар \"Data analytics and AI in turbulent times. Opportunities for all of us\"",
                    "Можливості під час кризи, як використовувати аналітику даних та штучний інтелект для моделювання розповсюдження коронавірусу та багато іншого",
                    LocalDate.of(2020, 6, 7),
                    510));
            id = conferenceService.addConference(new ConferenceCreationRequest("Бітап \"Клієнт транслейт: \"мова клієнта\" та як почати її розуміти?\"",
                    "Що таке \"мова клієнта\" та як почати її розуміти? Чому клієнти залишаються з нами? Як створювати додаткову цінність та скільки це коштує?",
                    LocalDate.of(2020, 6, 1),
                    150));

            talkRestController.addTalk(new TalkCreationRequest("Клієнт транслейт", "Таня має більше 10 років у HR, та останні 4 з них — будує команди для міжнародних клієнтів та практикує все, про що будемо говорити.",
                    TypeTalk.MASTER_CLASS, "Бойченко Tаня"), id);

            System.out.println("ADDING finish.");
        }
    }

}
