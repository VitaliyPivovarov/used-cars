-- Users schema

-- !Ups

CREATE TABLE car_mark (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    country varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

-- !Downs