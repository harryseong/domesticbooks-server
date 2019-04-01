INSERT INTO author (id, first_name, middle_name, last_name, created_date, modified_date)
  VALUES (1, 'Michelle', 'LaVaughn', 'Obama', now(), now());

INSERT INTO author (id, first_name, middle_name, last_name, created_date, modified_date)
  VALUES (2, 'Barrack', 'H', 'Obama', now(), now());

INSERT INTO author (id, first_name, middle_name, last_name, created_date, modified_date)
  VALUES (3, 'George', 'W', 'Bush', now(), now());

INSERT INTO book_author (book_id, author_id)
  VALUES (1, 1);

INSERT INTO book_author (book_id, author_id)
  VALUES (2, 2);

INSERT INTO book_author (book_id, author_id)
  VALUES (3, 2);

INSERT INTO book_author (book_id, author_id)
  VALUES (4, 3);