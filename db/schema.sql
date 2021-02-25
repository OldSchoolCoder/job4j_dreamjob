CREATE TABLE post
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE photo
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE    job_user
(
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    email    TEXT,
    password TEXT
);