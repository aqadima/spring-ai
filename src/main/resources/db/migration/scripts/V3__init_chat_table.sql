create table if not exists app.chats
(
    id           UUID         not null unique,
    user_id      UUID         not null,
    is_active    bool         not null,
    title        varchar(50),
    max_messages serial       not null,
    created_at   timestamp(3) not null,
    updated_at   timestamp(3) not null,
    primary key (id)
);

ALTER TABLE app.chats
    OWNER TO postgres;