create table sellers (
    id bigint generated by default as identity,
    birthday date not null,
    cpf varchar(15) not null unique,
    cnpj varchar(20) unique,
    status varchar(20) not null check (status in ('IN_ANALYSIS','BLOCKED','ACTIVE','INACTIVE')),
    email varchar(100) not null unique,
    name varchar(100) not null,
    primary key (id)
)
