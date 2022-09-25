CREATE TABLE users  (
  id int AUTO_INCREMENT,
  user_name varchar(40) DEFAULT NULL,
  password varchar(100) DEFAULT NULL,
  last_logged date,
  active boolean DEFAULT true,
  PRIMARY KEY (id)
);

ALTER TABLE users AUTO_INCREMENT = 1000;

CREATE TABLE user_account (
  account_no int AUTO_INCREMENT,
  cust_id int NOT NULL,
  customer_name varchar(40) DEFAULT NULL,
  clear_balance double(40,2),
  unclear_balance double(40,2),
  last_transaction_date date,
  status varchar(40) DEFAULT NULL,
  PRIMARY KEY (account_no),
  FOREIGN KEY (cust_id) REFERENCES users (id)
);

ALTER TABLE user_account AUTO_INCREMENT = 10000;

CREATE TABLE transaction (
  id int NOT NULL AUTO_INCREMENT,
  account_no int NOT NULL,
  transaction_type varchar(100) NOT NULL,
  transaction_id varchar(100) NOT NULL,
  current_balance double(40,2),
  transaction_amount double(40,2),
  clear_balance double(40,2),
  status varchar(40) NOT NULL,
  transaction_date date,
  remarks varchar(200),
  PRIMARY KEY (id),
  FOREIGN KEY (account_no) REFERENCES user_account (account_no)
);

ALTER TABLE transaction AUTO_INCREMENT = 0;

CREATE TABLE denominations (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  value int NOT NULL,
  count int NOT NULL,
  deposit_limit int NOT NULL,
  disburse_limit int NOT NULL,
  active boolean default true,
  PRIMARY KEY (id)
);

ALTER TABLE denominations AUTO_INCREMENT = 0;