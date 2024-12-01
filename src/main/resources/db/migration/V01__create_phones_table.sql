create table phones (
    area_code smallint not null check ((area_code>=10) and (area_code<=99)),
    number integer not null check ((number<=999999999) and (number>=19999999)),
    seller_id bigint not null,
    type varchar(15) not null check (type in ('HOME','CELL_PHONE','WORK')),
    primary key (area_code, number)
)
