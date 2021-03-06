CREATE TABLE resume (
                        uuid      CHAR(36) PRIMARY KEY NOT NULL,
                        full_name TEXT                 NOT NULL
);

CREATE TABLE contact (
                         id          SERIAL,
                         resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
                         type        TEXT     NOT NULL,
                         value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

CREATE TABLE section (
                         id          SERIAL PRIMARY KEY,
                         resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
                         type        TEXT     NOT NULL,
                         content     TEXT     NOT NULL
);
CREATE UNIQUE INDEX section_idx
    ON section (resume_uuid, type);






/*
select * from resume;

create table resume
(
   uuid      char(36) not null
       constraint resume_pk
           primary key,
   full_name text     not null
);

alter table resume
   owner to postgres;



insert into resume (uuid, full_name)
values ('123', 'Full Name');


create table contact
(
   id          serial   not null
       constraint contact_pk
           primary key,
   resume_uuid char(36) not null
       constraint contact_resume_uuid_fk
           references resume
           on delete cascade,
   type        text     not null,
   value       text

);

alter table contact
   owner to postgres;

create unique index contact_uuid_type_index
   on contact (resume_uuid, type);


CREATE TABLE section (
                        id          SERIAL PRIMARY KEY,
                        resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
                        type        TEXT     NOT NULL,
                        content     TEXT     NOT NULL
);
CREATE UNIQUE INDEX section_idx
   ON section (resume_uuid, type);


SELECT * FROM resume r
                 JOIN contact c
                      ON r.uuid = c.resume_uuid
WHERE r.uuid = 'fa1aede7-b813-4349-9c6a-8135e95385e2';

SELECT * FROM resume r
                 left join contact c
                           ON r.uuid = c.resume_uuid;

*/