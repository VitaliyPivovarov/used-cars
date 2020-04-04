# --- Create car_mark table

# --- !Ups
create table car_mark
(
    id      bigserial primary key,
    name    varchar(128) not null,
    country varchar(128) not null
);

# --- !Downs
drop table car_mark;