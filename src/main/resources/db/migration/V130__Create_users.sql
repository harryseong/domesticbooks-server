INSERT INTO user (id, email, first_name, last_name, username, password, created_date, modified_date)
  VALUES (1, 'l-james@test.com', 'Lebron', 'James', 'lebronjames', 'password', now(), now());

INSERT INTO user_book (user_id, book_id, have_read, have_read_date)
  VALUES (1, 1, true, '2017-01-01');

INSERT INTO user_book (user_id, book_id, have_read, have_read_date)
  VALUES (1, 2, true, '2018-01-01');

INSERT INTO user_book (user_id, book_id, have_read, have_read_date)
  VALUES (1, 3, true, '2018-01-01');

INSERT INTO user_book (user_id, book_id, have_read, have_read_date)
  VALUES (1, 4, true, '2019-01-01');

