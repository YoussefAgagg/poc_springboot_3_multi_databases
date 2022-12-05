
CREATE TABLE IF NOT EXISTS userh2 (
    id             BIGINT auto_increment PRIMARY KEY ,
    username      VARCHAR(100),
    firstName     VARCHAR(50),
    lastName      VARCHAR(50),
    email         VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    createdDT     DATETIME NOT NULL DEFAULT now(),
    modifiedDT    DATETIME NOT NULL DEFAULT now()
);


CREATE UNIQUE INDEX idx_user_username ON userh2(username);
