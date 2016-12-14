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
INSERT INTO Users(id, forename, name, address, zipcode, city, role) VALUES(1, 'Herbert', 'Padrone', 'Werdenfelsstr. 10', 81377, 'München', 'Mitarbeiter');
INSERT INTO Users(id, forename, name, address, zipcode, city, role) VALUES(2, 'Susanne', 'Emil', 'Rodensteinstr. 3 ', 81375, 'München', 'Kunde');


# --- !Downs
DROP TABLE Users,

# Menu schema

# --- !Ups
CREATE TABLE Menu (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    price double NOT NULL,
    category varchar(255) NOT NULL
);
INSERT INTO Menu(id, name, price, category) VALUES(101, 'Pizza Margarita', 0.5, 'Pizza');
INSERT INTO Menu(id, name, price, category) VALUES(102, 'Pizza Regina', 0.5, 'Pizza');
INSERT INTO Menu(id, name, price, category) VALUES(201, 'Sprite', 0.3, 'Getränk');
INSERT INTO Menu(id, name, price, category) VALUES(202, 'Cola', 0.3, 'Getränk');
INSERT INTO Menu(id, name, price, category) VALUES(301, 'Schokokuchen', 2.0, 'Dessert');
INSERT INTO Menu(id, name, price, category) VALUES(302, 'Schokoeis', 2.0, 'Dessert');

# --- !Downs

# Orderbill schema

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

# --- !Downs
DROP TABLE Orderbill;

# Customerorderhistory schema

# --- !Ups
Create Table Customerorderhistory (
    customerId serial NOT NULL PRIMARY KEY,
    customerData varchar(255),
    orderedProducts varchar(255),
    sumOfOrder double,
    orderDate DATE,
);

# --- !Downs
DROP TABLE Customerorderhistory;
