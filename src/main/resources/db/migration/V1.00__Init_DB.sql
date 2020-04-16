create sequence hibernate_sequence start 1 increment 1;

create table conference (
     id int8 not null,
     date_start date not null,
     name varchar(100) not null,
     topic varchar(255) not null,
     amount_participants int4 not null,
     primary key (id)
);

create table talk (
     id int8 not null,
     reporter varchar(60) not null,
     type_talk varchar(20) not null,
     name varchar(100) not null,
     description varchar(2048) not null,
     primary key (id)
);

create table conferences_talks (
     conference_id int8 not null,
     talk_id int8 not null,
     primary key (conference_id, talk_id)
);

alter table if exists conferences_talks
     add constraint conference_talk_fk
     foreign key (conference_id) references conference
     MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;

alter table if exists conferences_talks
     add constraint talk_conf_fk
     foreign key (talk_id) references talk
     MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;