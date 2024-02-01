-- create the database named inventory
create database inventory;

-- use the inventory database
use inventory;

-- create the product table with product id, name, and in stock quantity
create table product
(
    product_id        int AUTO_INCREMENT primary key,
    product_name      varchar(50) unique not null,
    in_stock_quantity int                not null
);

-- create the buyer table with buyer id and name
create table buyer
(
    buyer_id int AUTO_INCREMENT primary key,
    name     varchar(50) not null
);

-- create the order table with order id, product id, buyer id, and quantity
create table `order`
(
    order_id   int AUTO_INCREMENT primary key,
    product_id int not null,
    buyer_id   int not null,
    quantity   int not null,
    foreign key (product_id) references product (product_id),
    foreign key (buyer_id) references buyer (buyer_id)
);