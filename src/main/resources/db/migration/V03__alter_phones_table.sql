ALTER TABLE IF EXISTS phones
ADD CONSTRAINT fk_phones_sellers
FOREIGN KEY (seller_id)
REFERENCES sellers(id);