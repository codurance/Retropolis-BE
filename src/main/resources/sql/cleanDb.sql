DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS columns;
DROP TABLE IF EXISTS users_boards;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email    varchar UNIQUE,
    username varchar
);

CREATE TABLE IF NOT EXISTS boards
(
    id    SERIAL PRIMARY KEY,
    title varchar
);

CREATE TABLE IF NOT EXISTS users_boards
(
    user_id  int,
    board_id int,
    PRIMARY KEY (user_id, board_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (board_id) REFERENCES boards (id)
);

CREATE TABLE IF NOT EXISTS columns
(
    id       SERIAL PRIMARY KEY,
    title    varchar,
    board_id int,
    FOREIGN KEY (board_id) REFERENCES boards (id)
);

CREATE TABLE IF NOT EXISTS cards
(
    id        SERIAL PRIMARY KEY,
    text      varchar,
    user_id   int,
    column_id int,
    voters    int[],
    FOREIGN KEY (column_id) REFERENCES columns (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

insert into boards(title)
values ('test board');
insert into columns(title, board_id)
values ('Start', 1);
insert into columns(title, board_id)
values ('Stop', 1);
insert into columns(title, board_id)
values ('Continue', 1);
insert into users(email, username)
values ('john.doe@codurance.com', 'john doe');