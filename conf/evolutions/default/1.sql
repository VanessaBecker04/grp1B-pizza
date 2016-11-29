# Users schema

# --- !Ups
CREATE TABLE Users (
    id serial PRIMARY KEY,
    forename varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    zipcode int NOT NULL,
    city varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);

# --- !Downs
DROP TABLE Users,

# Menu schema

# --- !Ups
CREATE TABLE Menu (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    price int,
    category varchar(255) NOT NULL
);

# --- !Downs

# BillForOrder schema

# --- !Ups
CREATE TABLE Orderbill (
    id serial PRIMARY KEY,
    customerId serial NOT NULL,
    pizzaName varchar(255),
    pizzaNumber int,
    pizzaSize varchar(255),
    beverageName varchar(255),
    beverageNumber int,
    beverageSize varchar(255),
    dessertName varchar(255),
    dessertNumber int,
);

# --- "Downs

DROP TABLE Orderbill;
