CREATE TABLE "product" (
  "id" uuid PRIMARY KEY,
  "title" varchar(255) NOT NULL,
  "category" varchar(50) NOT NULL,
  "description" text,
  "height" numeric(10,2) NOT NULL,
  "width" numeric(10,2) NOT NULL,
  "length" numeric(10,2) NOT NULL,
  "weight" numeric(10,3) NOT NULL,
  "barcode" varchar(32) UNIQUE NOT NULL,
  "original_value" bigint NOT NULL,
  "current_price" bigint NOT NULL,
  "stock_quantity" int NOT NULL,
  "status" varchar(20) NOT NULL,
  "image_url" varchar(2048),
  "version" bigint NOT NULL DEFAULT 0,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "book" (
  "id" uuid PRIMARY KEY,
  "publisher" varchar(255) NOT NULL,
  "publication_date" date NOT NULL,
  "language" varchar(50),
  "cover_type" varchar(9) NOT NULL,
  "number_of_pages" int,
  "genre" varchar(50)
);

CREATE TABLE "book_authors" (
  "book_id" uuid,
  "author" varchar(255),
  PRIMARY KEY ("book_id", "author")
);

CREATE TABLE "newspaper" (
  "id" uuid PRIMARY KEY,
  "publisher" varchar(255) NOT NULL,
  "publication_date" date NOT NULL,
  "language" varchar(50),
  "editor_in_chief" varchar(255) NOT NULL,
  "issue_number" varchar(25),
  "publication_frequency" varchar(25),
  "issn" varchar(9)
);

CREATE TABLE "newspaper_sections" (
  "newspaper_id" uuid,
  "section" varchar(50),
  PRIMARY KEY ("newspaper_id", "section")
);

CREATE TABLE "cd" (
  "id" uuid PRIMARY KEY,
  "release_date" date,
  "genre" varchar(50) NOT NULL,
  "record_label" varchar(255) NOT NULL
);

CREATE TABLE "cd_artists" (
  "cd_id" uuid,
  "artist" varchar(255),
  PRIMARY KEY ("cd_id", "artist")
);

CREATE TABLE "cd_tracks" (
  "cd_id" uuid,
  "track_number" int,
  "title" varchar(255) NOT NULL,
  "length" int NOT NULL,
  PRIMARY KEY ("cd_id", "track_number")
);

CREATE TABLE "dvd" (
  "id" uuid PRIMARY KEY,
  "release_date" date,
  "genre" varchar(50),
  "disc_type" varchar(7) NOT NULL,
  "director" varchar(255) NOT NULL,
  "runtime" int NOT NULL,
  "studio" varchar(255) NOT NULL,
  "language" varchar(50) NOT NULL
);

CREATE TABLE "dvd_subtitles" (
  "dvd_id" uuid,
  "subtitle" text,
  PRIMARY KEY ("dvd_id", "subtitle")
);

CREATE TABLE "order" (
  "id" uuid PRIMARY KEY,
  "status" varchar(10) NOT NULL,
  "version" bigint NOT NULL DEFAULT 0,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "order_item" (
  "id" uuid PRIMARY KEY,
  "order_id" uuid,
  "product_id" uuid,
  "product_name" varchar(255) NOT NULL,
  "quantity" int NOT NULL,
  "unit_price" bigint NOT NULL,
  "unit_weight" numeric(10,3) NOT NULL
);

CREATE TABLE "delivery_information" (
  "order_id" uuid PRIMARY KEY,
  "customer_name" varchar(255),
  "customer_email" varchar(255),
  "phone_number" varchar(10),
  "province" varchar(50),
  "commune" varchar(50),
  "address" text,
  "delivery_method" varchar(50)
);

CREATE TABLE "invoice" (
  "order_id" uuid PRIMARY KEY,
  "issued_at" timestamptz NOT NULL,
  "total_price_without_vat" bigint NOT NULL,
  "total_price_with_vat" bigint NOT NULL,
  "delivery_fee" bigint NOT NULL,
  "total_amount" bigint NOT NULL
);

CREATE TABLE "payment_transaction" (
  "id" uuid PRIMARY KEY,
  "transaction_content" text,
  "transaction_timestamp" timestamptz NOT NULL,
  "transaction_method" varchar(10) NOT NULL,
  "amount_paid" bigint NOT NULL,
  "order_id" uuid NOT NULL
);

CREATE TABLE "users" (
  "id" uuid PRIMARY KEY,
  "username" varchar(255) NOT NULL,
  "email" varchar(255) UNIQUE NOT NULL,
  "hashed_password" text NOT NULL,
  "active" boolean NOT NULL DEFAULT true,
  "blocked" boolean NOT NULL DEFAULT false,
  "must_change_password" boolean NOT NULL,
  "version" bigint NOT NULL DEFAULT 0,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "user_roles" (
  "user_id" uuid,
  "role" varchar(20),
  PRIMARY KEY ("user_id", "role")
);

CREATE TABLE "product_log" (
  "id" uuid PRIMARY KEY,
  "action" varchar(20) NOT NULL,
  "manager_id" uuid,
  "manager_username" varchar(255) NOT NULL,
  "product_id" uuid,
  "product_title" varchar(255) NOT NULL,
  "timestamp" timestamptz NOT NULL
);

CREATE TABLE "product_update_log" (
  "log_id" uuid,
  "field_number" int,
  "field_name" varchar(255),
  "old_value" text,
  "new_value" text,
  PRIMARY KEY ("log_id", "field_number")
);

CREATE TABLE "stock_adjust_log" (
  "id" uuid PRIMARY KEY,
  "old_stock" int NOT NULL,
  "new_stock" int NOT NULL,
  "reason" text NOT NULL,
  "manager_id" uuid,
  "manager_username" varchar(255) NOT NULL,
  "product_id" uuid,
  "product_title" varchar(255) NOT NULL,
  "timestamp" timestamptz NOT NULL
);

CREATE TABLE "admin_log" (
  "id" uuid PRIMARY KEY,
  "action" varchar(20) NOT NULL,
  "admin_id" uuid,
  "admin_username" varchar(255) NOT NULL,
  "affected_user_id" uuid,
  "affected_username" varchar(255) NOT NULL,
  "timestamp" timestamptz NOT NULL
);

CREATE UNIQUE INDEX ON "order_item" ("order_id", "product_id");

COMMENT ON COLUMN "product"."height" IS 'cm';

COMMENT ON COLUMN "product"."width" IS 'cm';

COMMENT ON COLUMN "product"."length" IS 'cm';

COMMENT ON COLUMN "product"."weight" IS 'kg';

COMMENT ON COLUMN "cd_tracks"."length" IS 'seconds';

COMMENT ON COLUMN "dvd"."runtime" IS 'minutes';

ALTER TABLE "book" ADD FOREIGN KEY ("id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newspaper" ADD FOREIGN KEY ("id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cd" ADD FOREIGN KEY ("id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dvd" ADD FOREIGN KEY ("id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "book_authors" ADD FOREIGN KEY ("book_id") REFERENCES "book" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newspaper_sections" ADD FOREIGN KEY ("newspaper_id") REFERENCES "newspaper" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cd_artists" ADD FOREIGN KEY ("cd_id") REFERENCES "cd" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cd_tracks" ADD FOREIGN KEY ("cd_id") REFERENCES "cd" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "dvd_subtitles" ADD FOREIGN KEY ("dvd_id") REFERENCES "dvd" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "delivery_information" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "invoice" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_transaction" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_roles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product_log" ADD FOREIGN KEY ("manager_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product_log" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product_update_log" ADD FOREIGN KEY ("log_id") REFERENCES "product_log" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock_adjust_log" ADD FOREIGN KEY ("manager_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock_adjust_log" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "admin_log" ADD FOREIGN KEY ("admin_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "admin_log" ADD FOREIGN KEY ("affected_user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;
