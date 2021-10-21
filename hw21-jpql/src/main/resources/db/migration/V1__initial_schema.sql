-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create table address
(
    id     bigint not null primary key,
    street varchar(50),
    CONSTRAINT addr_client_fk FOREIGN KEY (id) REFERENCES client (id)
);

create table phones
(
    id           bigint not null primary key,
    client_phone varchar(50),
    CONSTRAINT ph_client_fk FOREIGN KEY (client_id) REFERENCES client (id)
);
