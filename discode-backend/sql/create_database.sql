CREATE TABLE IF NOT EXISTS user_credentials
(
    user_id       INTEGER      NOT NULL AUTO_INCREMENT,
    username      VARCHAR(64)  NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    CONSTRAINT pk_user_credentials PRIMARY KEY (user_id),
    CONSTRAINT uk_user_credentials UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS admins
(
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_admins_user_id FOREIGN KEY (user_id) REFERENCES user_credentials (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_accounts
(
    user_id     INTEGER      NOT NULL,
    first_name  VARCHAR(64)  NOT NULL,
    last_name   VARCHAR(64)  NOT NULL,
    email       VARCHAR(128) NOT NULL,
    description TEXT,
    image_path  VARCHAR(256),
    CONSTRAINT pk_user_accounts PRIMARY KEY (user_id),
    CONSTRAINT fk_user_accounts_user_id FOREIGN KEY (user_id) REFERENCES user_credentials (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_bans
(
    ban_id     INT  NOT NULL AUTO_INCREMENT,
    user_id    INT  NOT NULL,
    start_date DATE NOT NULL,
    end_date   DATE NOT NULL,
    ban_reason TEXT NOT NULL,
    CONSTRAINT pk_user_bans PRIMARY KEY (ban_id),
    CONSTRAINT fk_user_bans_user_id FOREIGN KEY (user_id) REFERENCES user_credentials (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chats
(
    chat_id   INT          NOT NULL AUTO_INCREMENT,
    chat_name VARCHAR(256) NOT NULL,
    CONSTRAINT pk_chats PRIMARY KEY (chat_id)
);

CREATE TABLE IF NOT EXISTS chat_members
(
    chat_member_id INT  NOT NULL AUTO_INCREMENT,
    chat_id        INT  NOT NULL,
    user_id        INT  NOT NULL,
    status         CHAR NOT NULL,
    CONSTRAINT pk_chat_members PRIMARY KEY (chat_member_id),
    CONSTRAINT fk_chat_members_chat_id FOREIGN KEY (chat_id) REFERENCES chats (chat_id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_members_user_id FOREIGN KEY (user_id) REFERENCES user_credentials (user_id) ON DELETE CASCADE,
    CONSTRAINT uk_chat_members_id UNIQUE (chat_id, user_id)
);

CREATE TABLE IF NOT EXISTS messages
(
    message_id     INT  NOT NULL AUTO_INCREMENT,
    chat_member_id INT  NOT NULL,
    creation_date  DATE NOT NULL,
    content        TEXT NOT NULL,
    code_output    TEXT,
    CONSTRAINT pk_messages PRIMARY KEY (message_id),
    CONSTRAINT fk_messages_chat_member_id FOREIGN KEY (chat_member_id) REFERENCES chat_members (chat_member_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS message_reports
(
    message_id    INT  NOT NULL,
    reporter_id   INT  NOT NULL,
    report_date   DATE NOT NULL,
    report_reason TEXT,
    status        CHAR NOT NULL,
    CONSTRAINT pk_message_reports PRIMARY KEY (message_id, reporter_id),
    CONSTRAINT fk_message_reports_message_id FOREIGN KEY (message_id) REFERENCES messages (message_id) ON DELETE CASCADE,
    CONSTRAINT fk_message_reports_reporter_id FOREIGN KEY (reporter_id) REFERENCES user_credentials (user_id) ON DELETE CASCADE
);

CREATE INDEX idx_user_first_name ON user_accounts (first_name);
CREATE INDEX idx_user_last_name ON user_accounts (last_name);
CREATE INDEX idx_user_email ON user_accounts (email);
CREATE INDEX idx_user_image_path ON user_accounts (image_path);
CREATE INDEX idx_chats_name ON chats (chat_name);

-- Check constraints

ALTER TABLE user_credentials
    ADD CONSTRAINT ck_user_credentials_username CHECK (username REGEXP '^[_a-zA-Z]\\w{2,}$');
ALTER TABLE user_accounts
    ADD CONSTRAINT ck_user_accounts_first_name CHECK (first_name REGEXP '^[A-Z](?>[- ]?[a-zA-Z]+)+$');
ALTER TABLE user_accounts
    ADD CONSTRAINT ck_user_accounts_last_name CHECK (last_name REGEXP '^[A-Z](?>[- ]?[a-zA-Z]+)+$');
ALTER TABLE user_accounts
    ADD CONSTRAINT ck_user_accounts_email CHECK (email REGEXP '^\\w+(?:[-.]\\w+)*@\\w+(?:[-.]\\w+)+$');
ALTER TABLE chats
    ADD CONSTRAINT ck_chats_chat_name CHECK (LENGTH(chat_name) > 0);
ALTER TABLE chat_members
    ADD CONSTRAINT ck_chat_members_status CHECK (status REGEXP '^a|l$'); -- active|left
ALTER TABLE message_reports
    ADD CONSTRAINT ck_message_reports_status CHECK (status REGEXP '^p|r$'); -- pending|reviewed