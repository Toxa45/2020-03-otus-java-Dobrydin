create sequence seq_user;
alter sequence seq_user owner to usr;

create table users
(
    id       bigint not null constraint users_pkey primary key default nextval ('SEQ_USER'),
    login    varchar(255),
    name     varchar(255),
    password varchar(255),
    role     varchar(255)
);

alter table users owner to usr;




insert into users VALUES ( nextval ('SEQ_USER'), 'admin', 'admin', 'admin','ROLE_ADMIN');
insert into users VALUES ( nextval ('SEQ_USER'), 'user2', 'login2', 'pass2','ROLE_USER');
insert into users VALUES ( nextval ('SEQ_USER'), 'user3', 'login3', 'pass3','ROLE_USER');
insert into users VALUES ( nextval ('SEQ_USER'), 'user4', 'login4', 'pass4','ROLE_USER');
insert into users VALUES ( nextval ('SEQ_USER'), 'user5', 'login5', 'pass5','ROLE_USER');
