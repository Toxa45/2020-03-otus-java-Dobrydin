create table users
(
    id       bigint not null constraint users_pkey primary key,
    login    varchar(255),
    name     varchar(255),
    password varchar(255),
    role     varchar(255)
);

alter table users owner to usr;

create sequence seq_user;
alter sequence seq_user owner to usr;



insert into users VALUES (1, 'admin', 'admin', 'admin','ROLE_ADMIN');
insert into users VALUES (2, 'user2', 'login2', 'pass2','ROLE_USER');
insert into users VALUES (3, 'user3', 'login3', 'pass3','ROLE_USER');
insert into users VALUES (4, 'user4', 'login4', 'pass4','ROLE_USER');
insert into users VALUES (5, 'user5', 'login5', 'pass5','ROLE_USER');
