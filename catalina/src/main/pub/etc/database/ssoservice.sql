use ssoservicedb;

drop table if exists TBL_USER;
create table TBL_USER
(
    id bigint primary key auto_increment,
    userName char(32) unique not null,
    password char(128) not null,
    role enum('guest', 'operator', 'admin') not null,
    isLogIn bool default false,
    isAdminFirstLogin bool default true
)engine=InnoDB, charset=utf8;

insert into TBL_USER(userName, password, role, isLogIn, isAdminFirstLogin)
values ('admin', '123456', 'admin', false, true);
