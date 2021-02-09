\c raise_db
CREATE TYPE characteristic AS enum ('MEMORY', 'REACTION', 'LOGIC', 'CALCULATIONS');
CREATE TYPE status AS ENUM ('ACTIVE', 'UNCONFIRMED', 'BANNED');
CREATE TYPE test_status AS ENUM ('NEW', 'CONFIRMED', 'BANNED');

CREATE TABLE usr
(
    id                bigserial PRIMARY KEY,
    email             varchar(256) NOT NULL UNIQUE,
    name              varchar(40)  NOT NULL,
    surname           varchar(80)  NOT NULL,
    password          varchar(64)  NOT NULL,
    status            status       NOT NULL,
    registration_date DATE         NOT NULL
);


CREATE TABLE role
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    id   SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES usr(id),
    role_id BIGINT NOT NULL REFERENCES role(id)
);

CREATE TABLE role_permissions
(
    id         SERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL REFERENCES role(id),
    permission VARCHAR(50) NOT NULL
);


CREATE TABLE test_category
(
    id SERIAL PRIMARY KEY,
    category VARCHAR(60) NOT NULL UNIQUE,
    parent_id INTEGER REFERENCES test_category(id)
);

create table test
(
    id         SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES usr(id),
    status test_status NOT NULL,
    test_name  varchar(256) NOT NULL,
    difficulty INTEGER          NOT NULL,
    category_id INTEGER NOT NULL REFERENCES test_category(id)
);


create table question
(
    id      SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    content VARCHAR(512) NOT NULL,
    test_id INTEGER       NOT NULL REFERENCES test (id)
);

create table answer
(
    id          SERIAL PRIMARY KEY,
    content     VARCHAR(256) NOT NULL,
    correct     BOOLEAN      NOT NULL,
    question_id INTEGER       NOT NULL REFERENCES question (id)
);

create table test_characteristic
(
    id             SERIAL PRIMARY KEY,
    characteristic characteristic NOT NULL,
    test_id        INTEGER         NOT NULL REFERENCES test (id)
);

create table test_comment
(
    id        SERIAL PRIMARY KEY,
    content   VARCHAR(512) NOT NULL,
    timestamp timestamp,
    test_id   INTEGER       not null references test (id),
    user_id   INTEGER       not null references usr (id)
);

create table user_test_result
(
    id      SERIAL primary key,
    user_id INTEGER  not null references usr (id),
    test_id INTEGER  not null references test (id),
    result  INTEGER not null,
    UNIQUE(user_id, test_id)
);

