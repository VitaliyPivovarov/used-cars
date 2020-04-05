# --- Create car_store table

# --- !Ups
create table car_store
(
    id            bigserial primary key,
    car_mark_id   bigint    not null,
    car_model_id  bigint    not null,
    year_of_issue timestamp not null,
    mileage       int       not null,
    price         numeric   not null
);

create index if not exists car_store__mileage__ix on car_store (mileage);
create index if not exists car_store__price__ix on car_store (price);

comment on table car_store is 'Сar store table';
comment on column car_store.id is 'Record id';
comment on column car_store.car_mark_id is 'Id car mark';
comment on column car_store.car_model_id is 'Id car model';
comment on column car_store.year_of_issue is 'Year of issue of a car';
comment on column car_store.mileage is 'Mileage of a car';
comment on column car_store.price is 'Price of a car';

# --- !Downs
drop table car_store;