INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(1, 'Seller 1', 'seller1@example.com', '77135947010', '1980-01-01', 'IN_ANALYSIS', '49781729000131') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(2, 'Seller 2', 'seller2@example.com', '55608789016', '1980-01-02', 'IN_ANALYSIS', '99532029000181') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(3, 'Seller 3', 'seller3@example.com', '95882555035', '1980-01-03', 'IN_ANALYSIS', '12191314000106') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(4, 'Seller 4', 'seller4@example.com', '42055635077', '1980-01-04', 'IN_ANALYSIS', '19446299000130') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(5, 'Seller 5', 'seller5@example.com', '52005573051', '1980-01-05', 'IN_ANALYSIS', '24780463000181') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(6, 'Seller 6', 'seller6@example.com', '44545008003', '1980-01-06', 'IN_ANALYSIS', '03506261000176') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(7, 'Seller 7', 'seller7@example.com', '51844614000', '1980-01-07', 'BLOCKED', '55341746000150') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(8, 'Seller 8', 'seller8@example.com', '49800471065', '1980-01-08', 'ACTIVE', '22065727000190') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(9, 'Seller 9', 'seller9@example.com', '03274512036', '1980-01-01', 'IN_ANALYSIS', '34536110000194') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(10, 'Seller 10', 'seller10@example.com', '64949174029', '1980-01-10', 'INACTIVE', '31451785000142') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(11, 'Seller 11', 'seller11@example.com', '25306186076', '1980-01-11', 'ACTIVE', '63865367000198') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(12, 'Seller 12', 'seller12@example.com', '82100641000', '1980-01-12', 'IN_ANALYSIS', '74832335000102') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(13, 'Seller 13', 'seller13@example.com', '75586419009', '1980-01-01', 'IN_ANALYSIS', '46529501000160') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(14, 'Seller 14', 'seller14@example.com', '81266832017', '1980-01-14', 'IN_ANALYSIS', '72283530000103') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(15, 'Seller 15', 'seller15@example.com', '82970325071', '1980-01-15', 'BLOCKED', '28845268000143') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(16, 'Seller 16', 'seller16@example.com', '11832989003', '1980-01-16', 'IN_ANALYSIS', '76986382000136') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(17, 'Seller 17', 'seller17@example.com', '82798735033', '1980-01-17', 'IN_ANALYSIS', '74070164000121') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(18, 'Seller 18', 'seller18@example.com', '22242307029', '1980-01-18', 'ACTIVE', '80376859000139') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(19, 'Seller 19', 'seller19@example.com', '15981466022', '1980-01-19', 'IN_ANALYSIS', '30643167000131') ON CONFLICT (id) DO NOTHING;
INSERT INTO sellers (id, name, email, cpf, birthday, status, cnpj)
VALUES(20, 'Seller 20', 'seller20@example.com', '83799084061', '1980-01-01', 'INACTIVE', '78347959000195') ON CONFLICT (id) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(23,997303069, 'CELL_PHONE',1) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(89,996086205, 'CELL_PHONE',2) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(71,993954817, 'CELL_PHONE',3) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(62,994910501, 'CELL_PHONE',4) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(42,992023174, 'CELL_PHONE',5) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(63,992198850, 'CELL_PHONE',6) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(62,997329926, 'CELL_PHONE',7) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(56,998385418, 'CELL_PHONE',8) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(54,993630399, 'CELL_PHONE',9) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(76,995998465, 'CELL_PHONE',10) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(43,997678603, 'CELL_PHONE',11) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(52,992844080, 'CELL_PHONE',12) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(43,991720235, 'CELL_PHONE',13) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(66,996106423, 'CELL_PHONE',14) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(78,997624183, 'CELL_PHONE',15) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(82,996893104, 'CELL_PHONE',16) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(50,998904561, 'CELL_PHONE',17) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(68,994030082, 'CELL_PHONE',18) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(69,996155997, 'CELL_PHONE',19) ON CONFLICT(area_code, number) DO NOTHING;
INSERT INTO phones(area_code, number, type, seller_id) VALUES(85,998352894, 'CELL_PHONE',20) ON CONFLICT(area_code, number) DO NOTHING;

