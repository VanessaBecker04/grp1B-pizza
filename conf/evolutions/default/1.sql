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
INSERT INTO Users(id, forename, name, address, zipcode, city, role) VALUES(1, 'Herbert', 'Padrone', 'Zoxelstr. 34', 81479, 'München', 'Mitarbeiter');


# --- !Downs
DROP TABLE Users,

# Menu schema

# --- !Ups
CREATE TABLE Menu (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    price int NOT NULL,
    category varchar(255) NOT NULL
);
INSERT INTO Menu(id, name, price, category) VALUES(101, 'Pizza Margarita', 5, 'Pizza');
INSERT INTO Menu(id, name, price, category) VALUES(102, 'Pizza Regina', 5, 'Pizza');
INSERT INTO Menu(id, name, price, category) VALUES(201, 'Sprite', 3, 'Getränk');
INSERT INTO Menu(id, name, price, category) VALUES(202, 'Cola', 3, 'Getränk');
INSERT INTO Menu(id, name, price, category) VALUES(301, 'Schokokuchen', 2, 'Dessert');
INSERT INTO Menu(id, name, price, category) VALUES(302, 'Schokoeis', 2, 'Dessert');

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
