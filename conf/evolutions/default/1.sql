# Users schema
 
# --- !Ups
 
CREATE TABLE Users (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL
);
 
# --- !Downs
 
DROP TABLE Users;

# Menu Schema

# --- !Ups

CREATE TABLE Menu (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    price int,
    category varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE Menu;
