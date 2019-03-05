INSERT INTO user (id, email, first_name, middle_name, last_name, password)
  VALUES (1, 'harryseong@gmail.com', 'Harry', 'Hyunsoo', 'Seong', '123456');

INSERT INTO library (user_id, book_id)
  VALUES (1, 1);

INSERT INTO library (user_id, book_id)
  VALUES (1, 2);

INSERT INTO library (user_id, book_id)
  VALUES (1, 3);

INSERT INTO library (user_id, book_id)
  VALUES (1, 4);