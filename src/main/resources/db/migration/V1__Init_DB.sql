create sequence hibernate_sequence start 1 increment 1;

create table conference (
     id int8 not null,
     date_start date not null,
     name varchar(100) not null,
     topic varchar(255) not null,
     amount_participants int4 not null,
     primary key (id)
);

create table report (
     id int8 not null,
     reporter varchar(60) not null,
     type_report varchar(20) not null,
     name varchar(100) not null,
     description varchar(2048) not null,
     primary key (id)
);

create table conferences_reports (
     report_id int8 not null,
     conference_id int8 not null,
     primary key (report_id, conference_id)
);

alter table if exists conferences_reports
     add constraint conference_rep_fk
     foreign key (conference_id) references report;

alter table if exists conferences_reports
     add constraint report_conf_fk
     foreign key (report_id) references conference;