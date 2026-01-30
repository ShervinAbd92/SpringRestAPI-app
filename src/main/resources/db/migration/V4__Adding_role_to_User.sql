alter table users
    add role VARCHAR(20) default 'USER' not null;