
CREATE TABLE IF NOT EXISTS user(
    id             INTEGER  primary key AUTOINCREMENT,
    username      VARCHAR(100),
    firstName     VARCHAR(50),
    lastName      VARCHAR(50),
    email         VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    createdDT     DATETIME NOT NULL DEFAULT current_timestamp,
    modifiedDT    DATETIME NOT NULL DEFAULT current_timestamp
);


CREATE UNIQUE INDEX idx_user_username ON user(username);
