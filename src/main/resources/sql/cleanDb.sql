DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS columns;
DROP TABLE IF EXISTS users_boards;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email varchar UNIQUE
);

CREATE TABLE IF NOT EXISTS boards (
  id SERIAL PRIMARY KEY,
  title varchar
);

CREATE TABLE IF NOT EXISTS users_boards (
  user_id int,
  board_id int
);

CREATE TABLE IF NOT EXISTS columns (
  id SERIAL PRIMARY KEY,
  title varchar,
  board_id int
);

CREATE TABLE IF NOT EXISTS cards (
  id SERIAL PRIMARY KEY,
  text varchar,
  username varchar,
  column_id int,
  voters ARRAY
);

ALTER TABLE users_boards ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_boards ADD FOREIGN KEY (board_id) REFERENCES boards (id);

ALTER TABLE columns ADD FOREIGN KEY (board_id) REFERENCES boards (id);

ALTER TABLE cards ADD FOREIGN KEY (column_id) REFERENCES columns (id);

insert into boards(id, title) values(1, 'test board');
insert into columns(title, board_id) values('Start', 1);
insert into columns(title, board_id) values('Stop', 1);
insert into columns(title, board_id) values('Continue', 1);
