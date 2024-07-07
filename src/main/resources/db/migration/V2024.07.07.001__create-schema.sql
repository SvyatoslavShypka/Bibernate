CREATE SCHEMA IF NOT EXISTS access;

CREATE TABLE IF NOT EXISTS access.gender(
                              gender_id    numeric(1)    primary key,
                              name_ukr     varchar(50)   not null,
                              name_eng     varchar(50)   not null
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_gender_gender_id on access.gender (gender_id);

CREATE TABLE IF NOT EXISTS access.users(
                             user_id         UUID           primary key,
                             username        varchar(50)   not null,
                             password        varchar(500)   not null,
                             role            varchar(32)    not NULL,
                             enabled         boolean        not null,
                             nickname        varchar(100)    not null,
                             birthday        date,
                             gender_id       integer        not null default 0,
                             created_date    timestamp,
                             updated_date    timestamp,
                             constraint fk_users_gender foreign key(gender_id) references access.gender(gender_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_users_user_id   on access.users (user_id);
CREATE UNIQUE INDEX IF NOT EXISTS ix_users_username  on access.users (username);
CREATE INDEX IF NOT EXISTS ix_users_gender_id on access.users (gender_id);
