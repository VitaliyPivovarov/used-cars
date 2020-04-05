# --- Create car_model table

# --- !Ups
create table car_model
(
    id         bigserial primary key,
    name       varchar(128) not null,
    start_year timestamp    not null,
    end_year   timestamp
);

create index if not exists car_model__name__ix on car_model (name);

comment on table car_model is 'Сar model table';
comment on column car_model.id is 'Record id';
comment on column car_model.name is 'Car model name';
comment on column car_model.start_year is 'Production start year';
comment on column car_model.end_year is 'Production end year';

# --- !Downs
drop table car_model;