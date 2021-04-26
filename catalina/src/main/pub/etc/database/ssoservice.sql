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

drop table if exists TBL_SINGLE_PARTITION;
create table TBL_SINGLE_PARTITION
(
    id int primary key,
    name char(32) not null,
    age int not null,
    createTime char(32) not null
)engine=InnoDB, charset=utf8;

drop table if exists TBL_TWO_PARTITION;
create table TBL_TWO_PARTITION
(
    id int primary key,
    name char(32) not null,
    age int not null,
    createTime char(32) not null
)engine=InnoDB, charset=utf8;

drop table if exists TBL_THREE_PARTITION;
create table TBL_THREE_PARTITION
(
    id int primary key,
    name char(32) not null,
    age int not null,
    createTime char(32) not null
)engine=InnoDB, charset=utf8;
