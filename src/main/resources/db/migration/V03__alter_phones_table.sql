alter table if exists phones
add constraint fk_phones_sellers
foreign key (seller_id)
references sellers
