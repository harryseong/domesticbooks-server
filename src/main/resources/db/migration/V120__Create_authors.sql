INSERT INTO author (id, first_name, middle_name, last_name)
  VALUES (1, 'Michelle', 'LaVaughn', 'Obama');

INSERT INTO author (id, first_name, middle_name, last_name)
  VALUES (2, 'Barrack', 'H', 'Obama');

INSERT INTO author (id, first_name, middle_name, last_name)
  VALUES (3, 'George', 'W', 'Bush');

INSERT INTO book_author (book_id, author_id)
  VALUES (1, 1);

INSERT INTO book_author (book_id, author_id)
  VALUES (2, 2);

INSERT INTO book_author (book_id, author_id)
  VALUES (3, 2);

INSERT INTO book_author (book_id, author_id)
  VALUES (4, 3);