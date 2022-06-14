CREATE DATABASE forum_db_2;

CREATE TABLE USERS (
id bigserial primary key not null,
username varchar(255) UNIQUE not null,
email varchar(255) UNIQUE not null,
role varchar(255) default 'USER');

create table TOPICS (
id bigserial primary key not null,
title varchar(255),
pinned boolean not null DEFAULT false,
email_user boolean not null DEFAULT false,
create_date timestamp,
delete_date timestamp,
edit_date timestamp,
message text,
user_id bigint not null references USERS(id)
);

CREATE TABLE POSTS (
id bigserial primary key not null,
message text,
create_date timestamp,
delete_date timestamp,
edit_date timestamp,
user_id bigint not null references USERS(id),
topic_id bigint not null references TOPICS(id)
);


CREATE OR REPLACE VIEW V_TOPICS AS
select t.id, t.title, t.message, t.pinned, t.email_user,
       t.create_date, t.delete_date, t.edit_date, u.username as author_username, u.email as author_email,
       (select count(*) from posts p, topics t2 where t2.id=t.id) as posts_number
from TOPICS t, USERS u
where t.user_id = u.id;