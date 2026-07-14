create table if not exists app.chats
(
    id         UUID         not null unique,
    user_id    UUID         not null,
    title      varchar(50),
    created_at timestamp(3) not null,
    updated_at timestamp(3) not null,
    primary key (id)
);

ALTER TABLE app.chats
    OWNER TO postgres;