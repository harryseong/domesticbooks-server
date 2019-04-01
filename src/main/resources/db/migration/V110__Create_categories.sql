INSERT INTO category (id, name, created_date, modified_date)
  VALUES (1, 'Biography & Autobiography', now(), now());

INSERT INTO book_category (book_id, category_id)
  VALUES (1, 1);

INSERT INTO book_category (book_id, category_id)
  VALUES (2, 1);

INSERT INTO book_category (book_id, category_id)
  VALUES (3, 1);

INSERT INTO book_category (book_id, category_id)
  VALUES (4, 1);