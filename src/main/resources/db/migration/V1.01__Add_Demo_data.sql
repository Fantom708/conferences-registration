insert into conference (id, date_start, name, topic, amount_participants)
    values(1,
    '2020-05-05',
    'MEDICAL DEVICE FORUM 2020',
    'Влияние Регламента о медицинских изделиях (MDR) на производителей. Новые обязательства для импортеров и дистрибьюторов.',
    200);

insert into conference (id, date_start, name, topic, amount_participants)
    values(2,
    '2020-05-10',
    'Мероприятия для юристов-практиков',
    'Онлайн конференция: как пережить весну 2020',
    100);

insert into conference (id, date_start, name, topic, amount_participants)
    values(3,
    '2020-05-15',
    'Курс Java Developer',
    'SPRING BOOT. REST API and etc.',
    500);

insert into conference (id, date_start, name, topic, amount_participants)
    values(4,
    '2020-06-07',
    'Вебінар "Data analytics and AI in turbulent times. Opportunities for all of us"',
    'Можливості під час кризи, як використовувати аналітику даних та штучний інтелект для моделювання розповсюдження коронавірусу та багато іншого',
    100);

insert into conference (id, date_start, name, topic, amount_participants)
    values(5,
    '2020-06-01',
    'Бітап "Клієнт транслейт: "мова клієнта" та як почати її розуміти?"',
    'Що таке "мова клієнта" та як почати її розуміти? Чому клієнти залишаються з нами? Як створювати додаткову цінність та скільки це коштує?',
    150);

insert into report (id, reporter, type_report, name, description)
    values(1,
    'Бойченко Tаня',
    'MASTER_CLASS',
    'Клієнт транслейт',
    'Таня має більше 10 років у HR, та останні 4 з них — будує команди для міжнародних клієнтів та практикує все, про що будемо говорити.');

insert into report (id, reporter, type_report, name, description)
    values(2,
    'Popov N.K.',
    'WORKSHOP',
    'Новый регламент MDR',
    'Новые роли и их обязательства в соответствии с MDR');

insert into report (id, reporter, type_report, name, description)
    values(3,
    'Ivanov A.N.',
    'WORKSHOP',
    'COVID-19',
    'Що треба робити, якщо ви заражені');

insert into conferences_reports (conference_id, report_id)
    values(5, 1);

insert into conferences_reports (conference_id, report_id)
    values(1, 2);

insert into conferences_reports (conference_id, report_id)
    values(1, 3);