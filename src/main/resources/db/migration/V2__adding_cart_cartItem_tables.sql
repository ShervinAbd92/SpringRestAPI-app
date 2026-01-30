CREATE TABLE carts (
      id BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
      dateCreated DATE NOT NULL DEFAULT (CURRENT_DATE),
      PRIMARY KEY (id)
);

create table cart_items
(
    id         BIGINT auto_increment
        primary key,
    cart_id    BINARY(16)    not null,
    product_id BIGINT        not null,
    quantity   int default 1 not null,
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id),
    constraint cart_items_Cart_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_items_Product_id_fk
        foreign key (product_id) references products (id)
            on delete cascade
);