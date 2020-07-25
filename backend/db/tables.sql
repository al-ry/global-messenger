DROP TABLE IF EXISTS "user_has_friend";
DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
	"id_user"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	"telephone"	TEXT NOT NULL UNIQUE,
	"name"	TEXT NOT NULL,
	"crypted_password"	TEXT NOT NULL,
	"salt_password"	TEXT NOT NULL
);

CREATE TABLE "user_has_friend" (
	"id_user_has_friend"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"id_user"	INTEGER NOT NULL,
	"id_friend"	INTEGER NOT NULL,
	FOREIGN KEY("id_user") REFERENCES "user"("id_user"),
	FOREIGN KEY("id_friend") REFERENCES "user"("id_user")
);

CREATE UNIQUE INDEX iu_telephone_user
ON user (telephone);

CREATE INDEX ix_id_friend_user_has_friend
ON user_has_friend (id_friend);

CREATE INDEX ix_id_friend_user_has_friend
ON user_has_friend (id_friend);

CREATE INDEX ix_id_user_id_friend_user_has_friend
ON user_has_friend (id_user, id_friend);


