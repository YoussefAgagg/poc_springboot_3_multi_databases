-- CREATE SCHEMA IF NOT EXISTS acme;

CREATE TABLE IF NOT EXISTS user (
    id             BIGINT auto_increment PRIMARY KEY ,
    username      VARCHAR(100),
    firstName     VARCHAR(50),
    lastName      VARCHAR(50),
    email         VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    createdDT     DATETIME NOT NULL DEFAULT now(),
    modifiedDT    DATETIME NOT NULL DEFAULT now()
--    constraint pk_book primary key (id)
);
-- ENGINE=InnoDB DEFAULT CHARSET=UTF8;

-- ALTER TABLE employee ADD dept_id int AFTER email;

CREATE UNIQUE INDEX idx_user_username ON user(username);
