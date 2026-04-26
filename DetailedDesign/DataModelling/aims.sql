CREATE TABLE "user" (
  "id" uuid PRIMARY KEY,
  "user_name" varchar(255) NOT NULL,
  "email" varchar(255) UNIQUE NOT NULL,
  "hashed_password" text NOT NULL
);

CREATE TABLE "user_roles" (
  "user_id" uuid NOT NULL,
  "role" varchar(20) NOT NULL,
  PRIMARY KEY ("user_id", "role")
);

CREATE TABLE "product" (
  "id" uuid PRIMARY KEY,
  "title" varchar(255) NOT NULL,
  "height" numeric(10,2) NOT NULL,
  "width" numeric(10,2) NOT NULL,
  "length" numeric(10,2) NOT NULL,
  "weight" numeric(10,3) NOT NULL,
  "category" varchar(50) NOT NULL,
  "description" text NOT NULL,
  "barcode" varchar(32) UNIQUE NOT NULL,
  "original_value" bigint NOT NULL,
  "current_price" bigint NOT NULL,
  "stock_quantity" int NOT NULL,
  "status" varchar(20) NOT NULL
);

CREATE TABLE "book" (
  "id" uuid PRIMARY KEY,
  "language" varchar(50),
  "genre" varchar(50),
  "publication_date" date NOT NULL,
  "cover_type" varchar(9) NOT NULL,
  "publisher" varchar(255) NOT NULL,
  "number_of_pages" integer
);

CREATE TABLE "book_authors" (
  "book_id" uuid NOT NULL,
  "author" varchar(255) NOT NULL,
  PRIMARY KEY ("book_id", "author")
);

CREATE TABLE "newspaper" (
  "id" uuid PRIMARY KEY,
  "editor_in_chief" varchar(255) NOT NULL,
  "issue_number" varchar(25),
  "issn" varchar(9),
  "publication_frequency" varchar(25),
  "publisher" varchar(255) NOT NULL,
  "language" varchar(50),
  "publication_date" date NOT NULL
);

CREATE TABLE "newspaper_sections" (
  "newspaper_id" uuid NOT NULL,
  "section" varchar(50) NOT NULL,
  PRIMARY KEY ("newspaper_id", "section")
);

CREATE TABLE "cd" (
  "id" uuid PRIMARY KEY,
  "record_label" varchar(255) NOT NULL,
  "genre" varchar(50) NOT NULL,
  "release_date" date
);

CREATE TABLE "cd_artists" (
  "cd_id" uuid,
  "artist" varchar(255),
  PRIMARY KEY ("cd_id", "artist")
);

CREATE TABLE "track" (
  "track_id" uuid PRIMARY KEY,
  "cd_id" uuid NOT NULL,
  "title" varchar(255) NOT NULL,
  "length" bigint NOT NULL
);

CREATE TABLE "dvd" (
  "id" uuid PRIMARY KEY,
  "studio" varchar(255) NOT NULL,
  "disc_type" varchar(7) NOT NULL,
  "language" varchar(50) NOT NULL,
  "runtime" bigint NOT NULL,
  "director" varchar(255) NOT NULL,
  "release_date" timestamp,
  "genre" varchar(50)
);

CREATE TABLE "dvd_subtitles" (
  "dvd_id" uuid,
  "subtitle" text,
  PRIMARY KEY ("dvd_id", "subtitle")
);

CREATE TABLE "admin_log" (
  "id" uuid PRIMARY KEY,
  "admin_id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "timestamp" timestamp NOT NULL,
  "action_type" varchar(20) NOT NULL
);

CREATE TABLE "product_log" (
  "id" uuid PRIMARY KEY,
  "manager_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "timestamp" timestamp NOT NULL,
  "action_type" varchar(20) NOT NULL
);

CREATE TABLE "stock_adjust_log" (
  "id" uuid PRIMARY KEY,
  "manager_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "old_stock" int NOT NULL,
  "new_stock" int NOT NULL,
  "reason" text NOT NULL,
  "timestamp" timestamp NOT NULL
);

CREATE TABLE "order" (
  "id" uuid PRIMARY KEY,
  "status" varchar(10) NOT NULL,
  "delivery_fee" bigint NOT NULL
);

CREATE TABLE "delivery_information" (
  "order_id" uuid PRIMARY KEY,
  "customer_name" varchar(255) NOT NULL,
  "customer_email" varchar(255) NOT NULL,
  "phone_number" varchar(10) NOT NULL,
  "province" varchar(50) NOT NULL,
  "commune" varchar(50) NOT NULL,
  "address" varchar(255) NOT NULL,
  "delivery_method" varchar(255) NOT NULL
);

CREATE TABLE "invoice" (
  "order_id" uuid PRIMARY KEY,
  "total_price_without_vat" bigint NOT NULL,
  "total_price_with_vat" bigint NOT NULL,
  "delivery_fee" bigint NOT NULL,
  "total_amount" bigint NOT NULL
);

CREATE TABLE "payment_transaction" (
  "id" uuid PRIMARY KEY,
  "order_id" uuid NOT NULL,
  "amount_paid" bigint NOT NULL,
  "transaction_method" varchar(10) NOT NULL,
  "transaction_content" text NOT NULL,
  "transaction_timestamp" timestamp NOT NULL
);

CREATE TABLE "order_item" (
  "order_id" uuid,
  "product_id" uuid,
  "quantity" integer NOT NULL,
  "product_name" varchar(255) NOT NULL,
  "unit_price" bigint NOT NULL,
  "unit_weight" numeric(10,3) NOT NULL,
  PRIMARY KEY ("order_id", "product_id")
);

ALTER TABLE "admin_log" ADD FOREIGN KEY ("admin_id") REFERENCES "user" ("id");

ALTER TABLE "admin_log" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "stock_adjust_log" ADD FOREIGN KEY ("manager_id") REFERENCES "user" ("id");

ALTER TABLE "stock_adjust_log" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "product_log" ADD FOREIGN KEY ("manager_id") REFERENCES "user" ("id");

ALTER TABLE "product_log" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("id") REFERENCES "book" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("id") REFERENCES "newspaper" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("id") REFERENCES "cd" ("id");

ALTER TABLE "track" ADD FOREIGN KEY ("cd_id") REFERENCES "cd" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("id") REFERENCES "dvd" ("id");

ALTER TABLE "order_item" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "order_item" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id");

ALTER TABLE "payment_transaction" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("id");

ALTER TABLE "user_roles" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "book_authors" ADD FOREIGN KEY ("book_id") REFERENCES "book" ("id");

ALTER TABLE "newspaper_sections" ADD FOREIGN KEY ("newspaper_id") REFERENCES "newspaper" ("id");

ALTER TABLE "dvd_subtitles" ADD FOREIGN KEY ("dvd_id") REFERENCES "dvd" ("id");

ALTER TABLE "cd_artists" ADD FOREIGN KEY ("cd_id") REFERENCES "cd" ("id");

ALTER TABLE "order" ADD FOREIGN KEY ("id") REFERENCES "delivery_information" ("order_id");

ALTER TABLE "order" ADD FOREIGN KEY ("id") REFERENCES "invoice" ("order_id");
