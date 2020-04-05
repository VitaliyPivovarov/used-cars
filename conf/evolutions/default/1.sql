# --- Create car_mark table

# --- !Ups
create table car_mark
(
    id      bigserial primary key,
    name    varchar(128) not null,
    country varchar(128) not null
);

create index if not exists car_mark__name__ix on car_mark (name);

comment on table car_mark is 'Сar mark table';
comment on column car_mark.id is 'Record id';
comment on column car_mark.name is 'Car mark name';
comment on column car_mark.country is 'Car mark country';

# --- !Downs
drop table car_mark;