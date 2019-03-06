INSERT INTO role (id, name, description)
  VALUES (1, 'admin', '');

INSERT INTO role (id, name, description)
  VALUES (2, 'user', '');

INSERT INTO user_role (user_id, role_id)
  VALUES (1, 1);
