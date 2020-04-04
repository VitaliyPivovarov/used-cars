# --- Create car_model table

# --- !Ups
create table car_model
(
    id         bigserial primary key,
    name       varchar(128) not null,
    start_year timestamp    not null,
    end_year   timestamp
);

# --- !Downs
drop table car_model;