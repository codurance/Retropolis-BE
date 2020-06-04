CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email varchar UNIQUE
);

CREATE TABLE IF NOT EXISTS boards (
  id SERIAL PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS columns (
  id SERIAL PRIMARY KEY,
  title varchar,
  board_id int,
  FOREIGN KEY (board_id) REFERENCES boards (id)
);

CREATE TABLE IF NOT EXISTS cards (
  id SERIAL PRIMARY KEY,
  text varchar,
  username varchar,
  column_id int,
  voters ARRAY,
  FOREIGN KEY (column_id) REFERENCES columns (id)
);