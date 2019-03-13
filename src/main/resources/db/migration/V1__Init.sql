create table author (
  id integer not null auto_increment,
  first_name varchar(255) not null,
  last_name varchar(255),
  middle_name varchar(255),
  primary key (id)
) engine=InnoDB;

create table book (
  id integer not null auto_increment,
  cover_image_url varchar(255),
  description varchar(5000),
  isbn10 varchar(10),
  isbn13 varchar(13),
  other_id_type varchar(255),
  title varchar(400) not null,
  page_count integer,
  print_type varchar(255),
  published_date int,
  publisher varchar(255),
  primary key (id)
) engine=InnoDB;

create table category (
  id integer not null auto_increment,
  name varchar(255) not null,
  primary key (id)
) engine=InnoDB;

create table library (
  book_id integer not null,
  user_id integer not null
) engine=InnoDB;

create table book_author (
  book_id integer not null,
  author_id integer not null
) engine=InnoDB;

create table book_category (
  book_id integer not null,
  category_id integer not null
) engine=InnoDB;

create table user (
  id integer not null auto_increment,
  email varchar(255) not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  middle_name varchar(255),
  password varchar(255),
  primary key (id)
) engine=InnoDB;

create table role (
  id integer not null auto_increment,
  name varchar(255) not null,
  description varchar(255) not null,
  primary key (id)
) engine=InnoDB;

create table user_role (
  user_id integer not null,
  role_id integer not null
) engine=InnoDB;

alter table book_author
  add constraint FKbjqhp85wjv8vpr0beygh6jsgo
  foreign key (author_id)
  references author (id);

alter table book_author
  add constraint FKhwgu59n9o80xv75plf9ggj7xn
  foreign key (book_id)
  references book (id);

alter table book_category
  add constraint FKam8llderp40mvbbwceqpu6l2s
  foreign key (category_id)
  references category (id);

alter table book_category
  add constraint FKnyegcbpvce2mnmg26h0i856fd
  foreign key (book_id)
  references book (id);

alter table library
  add constraint FKbc0bwdnndnxhct38sinbem0n0
  foreign key (user_id)
  references user (id);

alter table library
  add constraint FK85pwltn867pjxog1gk5smtqcw
  foreign key (book_id)
  references book (id);

alter table user_role
  add constraint FKa68196081fvovjhkek5m97n3y
  foreign key (role_id)
  references role (id);

alter table user_role
  add constraint FK859n2jvi8ivhui0rl0esws6o
  foreign key (user_id)
  references user (id);