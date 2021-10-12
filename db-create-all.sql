create table users (
  id                            serial not null,
  name                          varchar(255),
  birthdate                     timestamptz,
  lastname                      varchar(255),
  constraint pk_users primary key (id)
);

