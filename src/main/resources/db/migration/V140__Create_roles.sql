INSERT INTO role (id, name, description, created_date, modified_date)
  VALUES (1, 'admin', '', now(), now());

INSERT INTO role (id, name, description, created_date, modified_date)
  VALUES (2, 'user', '', now(), now());

INSERT INTO user_role (user_id, role_id)
  VALUES (1, 1);
