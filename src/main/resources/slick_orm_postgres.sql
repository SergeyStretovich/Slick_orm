CREATE TABLE users
(
  id SERIAL,
  email character varying(34),
  nickname character varying(34),
  password character varying(34),
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE vehicles (
  id_user INT,
  vtype VARCHAR(34),
  brand VARCHAR(34)
  );

  INSERT INTO users (email,nickname, password)   VALUES ('mike@yahoo.com', 'Mike','pass');
  INSERT INTO users (email,nickname, password)   VALUES ('john@aol.com', 'John','trustno1');
  INSERT INTO users (email,nickname, password)   VALUES ('billy@gmail.com', 'Billy','12345');
  INSERT INTO users (email,nickname, password)   VALUES ('donald@zoho.com', 'Donald','11111');
  INSERT INTO users (email,nickname, password)   VALUES ('admin@icloud.com', 'Admin','999');

  INSERT INTO vehicles (id_user,vtype, brand)   VALUES (1, 'car','Toyota');
  INSERT INTO vehicles (id_user,vtype, brand)   VALUES (1, 'bicycle','S-Works');
   INSERT INTO vehicles (id_user,vtype, brand)   VALUES (2, 'mountain bike','Scott');
  INSERT INTO vehicles (id_user,vtype, brand)   VALUES (3, 'motorcycle','Yamaha');
  INSERT INTO vehicles (id_user,vtype, brand)   VALUES (3, 'car','Mitsubishi');
   INSERT INTO vehicles (id_user,vtype, brand)   VALUES (3, 'truck','Kenworth');
  INSERT INTO vehicles (id_user,vtype, brand)   VALUES (4, 'truck','Mack');
   INSERT INTO vehicles (id_user,vtype, brand)   VALUES (4, 'truck','Peterbilt');

select users.id,users.nickname, vehicles.vtype ,vehicles.brand from users
left join vehicles on users.id=vehicles.id_user