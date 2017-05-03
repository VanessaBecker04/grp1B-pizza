# --- !Ups
CREATE TABLE Users(
    id serial PRIMARY KEY,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    forename varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    zipcode int NOT NULL,
    city varchar(255) NOT NULL,
    role varchar(255) NOT NULL,
    inactive boolean
);
INSERT INTO Users(id, email, password, forename, name, address, zipcode, city, role, inactive) VALUES(-10, 'padrone@suez.de', 'Suez82346', 'Herbert', 'Padrone', 'Kientalstr. 10', 82346, 'Andechs', 'Mitarbeiter', false);
INSERT INTO Users(id, email, password, forename, name, address, zipcode, city, role, inactive) VALUES(-20, 'emil@gmx.de', 'Susanne82343', 'Susanne', 'Emil', 'Ulrichstr. 1 ', 82343, 'Pöcking', 'Kunde', false);

# --- !Downs
DROP TABLE Users;

# --- !Ups
CREATE TABLE Menu(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    price double precision NOT NULL,
    unit varchar(255),
    category varchar(255) NOT NULL,
    ordered boolean,
    active boolean
);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(101, 'Pizza Margarita', 0.23, 'cm', 'Pizza', false, true);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(102, 'Pizza Regina', 0.27, 'cm', 'Pizza', false, true);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(201, 'Sprite', 0.3, 'ml', 'Getränk', false, true);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(202, 'Cola', 0.3, 'ml', 'Getränk', false, true);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(301, 'Schokokuchen', 2.0, 'Stk', 'Dessert', false, true);
INSERT INTO Menu(id, name, price, unit, category, ordered, active) VALUES(302, 'Schokoeis', 2.0, 'Stk', 'Dessert', false, true);

# --- !Downs
DROP TABLE Menu;

# --- !Ups
Create Table OrderHistory(
    orderID serial NOT NULL PRIMARY KEY,
    customerID serial NOT NULL,
    customerData varchar(255),
    orderedProducts varchar(255),
    sumOfOrder double precision,
    orderDate varchar(255),
    status varchar(255)
);

# --- !Downs
DROP TABLE OrderHistory;
