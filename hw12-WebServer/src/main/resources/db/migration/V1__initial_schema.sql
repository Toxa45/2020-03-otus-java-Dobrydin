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

