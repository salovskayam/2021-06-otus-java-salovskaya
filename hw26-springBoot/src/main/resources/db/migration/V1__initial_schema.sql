create table client
(
    id bigserial not null primary key,
    name varchar(50) not null
);

create table address
(
    id bigserial not null primary key,
    street varchar(50) not null,
    client_id bigint not null,
    CONSTRAINT addr_client_fk FOREIGN KEY (client_id) REFERENCES client (id)
);

create table phones
(
    id bigserial not null primary key,
    client_phone varchar(50) not null,
    client_id bigint not null,
    CONSTRAINT ph_client_fk FOREIGN KEY (client_id) REFERENCES client (id)
);
