-- create user

CREATE USER IF NOT EXISTS 'acra'@'%' IDENTIFIED BY 'acra_pw';
GRANT ALL PRIVILEGES ON *.* TO 'acra'@'%' WITH GRANT OPTION;

-- application database

DROP DATABASE IF EXISTS mc_questions;
CREATE DATABASE mc_questions;
USE mc_questions;

-- create new tables

CREATE TABLE answer (
    uuid binary(16),
    text varbinary(1000),
    PRIMARY KEY (uuid)
);

CREATE TABLE app_user (
    uuid binary(16),
    name varbinary(1000) UNIQUE,
    password varbinary(1000),
    role varbinary(300),
    PRIMARY KEY (uuid)
);

CREATE TABLE exam (
    uuid binary(16),
    date varbinary(500),
    PRIMARY KEY (uuid)
);

CREATE TABLE exam_question (
    exam_id binary(16),
    question_id binary(16),
    PRIMARY KEY (exam_id, question_id)
);

CREATE TABLE question (
    uuid binary(16),
    is_not_question varbinary(300),
    question_info varbinary(1000),
    question_text varbinary(1000) UNIQUE,
    author_uuid binary(16),
    correct_answer_uuid binary(16),
    PRIMARY KEY (uuid)
);

CREATE TABLE question_incorrect_answer (
    question_id binary(16),
    answer_id binary(16),
    PRIMARY KEY (question_id, answer_id)
);

CREATE TABLE settings (
    uuid binary(16),
    max_answers varbinary(300),
    max_correct_answers varbinary(300),
    max_not_questions varbinary(300),
    max_questions varbinary(300),
    max_used_questions varbinary(300),
    min_answers varbinary(300),
    min_questions varbinary(300),
    name varbinary(1000),
    years_until_questions_are_free varbinary(300),
    PRIMARY KEY (uuid)
);

-- add references

ALTER TABLE exam_question ADD CONSTRAINT exam_question_exam
    FOREIGN KEY (exam_id)
    REFERENCES exam (uuid);

ALTER TABLE exam_question ADD CONSTRAINT exam_question_question
    FOREIGN KEY (question_id)
    REFERENCES question (uuid);

ALTER TABLE question ADD CONSTRAINT question_answer
    FOREIGN KEY (correct_answer_uuid)
    REFERENCES answer (uuid);

ALTER TABLE question ADD CONSTRAINT question_app_user
    FOREIGN KEY (author_uuid)
    REFERENCES app_user (uuid);

ALTER TABLE question_incorrect_answer ADD CONSTRAINT question_incorrect_answer_answer
    FOREIGN KEY (answer_id)
    REFERENCES answer (uuid);

ALTER TABLE question_incorrect_answer ADD CONSTRAINT question_incorrect_answer_question
    FOREIGN KEY (question_id)
    REFERENCES question (uuid);
