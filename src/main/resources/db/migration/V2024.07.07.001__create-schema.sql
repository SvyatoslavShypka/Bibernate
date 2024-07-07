
DROP TABLE IF EXISTS participants;
CREATE TABLE IF NOT EXISTS participants(
                             id         SERIAL PRIMARY KEY,
                             first_name        varchar(50)   not null,
                             last_name        varchar(50)   not null,
                             city        varchar(50)   not null,
                             company        varchar(50)   not null,
                             position        varchar(50)   not null,
                             years_of_experience integer,
                             created_at    timestamp DEFAULT NOW()
);
