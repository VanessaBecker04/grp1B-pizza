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
 
DROP TABLE User;
