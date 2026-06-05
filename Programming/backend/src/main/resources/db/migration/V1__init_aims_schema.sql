CREATE TABLE IF NOT EXISTS product(
	id 				UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	title			VARCHAR(255) NOT NULL,
	category		VARCHAR(50) NOT NULL,
	description		TEXT,
	height			NUMERIC(10, 2) NOT NULL, --cm
	width			NUMERIC(10, 2) NOT NULL, --cm
	length			NUMERIC(10, 2) NOT NULL, --cm
	weight			NUMERIC(10, 3) NOT NULL, --kg
	barcode			VARCHAR(32) NOT NULL UNIQUE,
	original_value	BIGINT NOT NULL,
	current_price 	BIGINT NOT NULL,
	stock_quantity	INT NOT NULL,
	status			VARCHAR(20) NOT NULL,
	image_url		VARCHAR(2048),
    version         BIGINT NOT NULL DEFAULT 0,
	created_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book(
	id					UUID PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
	publisher			VARCHAR(255) NOT NULL,
	publication_date	DATE NOT NULL,
	language			VARCHAR(50),
	--authors
	cover_type			VARCHAR(9) NOT NULL,
	number_of_pages		INT,
	genre				VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS book_authors(
	book_id		UUID REFERENCES book(id) ON DELETE CASCADE,
	author		VARCHAR(255),
	PRIMARY KEY (book_id, author)
);

CREATE TABLE IF NOT EXISTS newspaper(
	id						UUID PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
	publisher				VARCHAR(255) NOT NULL,
	publication_date		DATE NOT NULL,
	language				VARCHAR(50),
	editor_in_chief			VARCHAR(255) NOT NULL,
	issue_number			VARCHAR(25),
	publication_frequency	VARCHAR(25),
	issn					VARCHAR(9)
	--sections
);

CREATE TABLE IF NOT EXISTS newspaper_sections(
	newspaper_id	UUID REFERENCES newspaper(id) ON DELETE CASCADE,
	section			VARCHAR(50),
	PRIMARY KEY (newspaper_id, section)
);

CREATE TABLE IF NOT EXISTS cd(
	id 				UUID PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
	release_date	DATE,
	genre			VARCHAR(50) NOT NULL,
	--artists
	record_label	VARCHAR(255) NOT NULL
	--tracks
);

CREATE TABLE IF NOT EXISTS cd_artists(
	cd_id		UUID REFERENCES cd(id) ON DELETE CASCADE,
	artist		VARCHAR(255),
	PRIMARY KEY (cd_id, artist)
);

CREATE TABLE IF NOT EXISTS track(
	id			UUID PRIMARY KEY,
	cd_id		UUID NOT NULL REFERENCES cd(id) ON DELETE CASCADE,
	title		VARCHAR(255) NOT NULL,
	length		INT NOT NULL --seconds
);

CREATE TABLE IF NOT EXISTS dvd(
	id				UUID PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
	release_date	DATE,
	genre			VARCHAR(50),
	disc_type		VARCHAR(7) NOT NULL,
	director		VARCHAR(255) NOT NULL,
	runtime			INT NOT NULL, --minutes
	studio			VARCHAR(255) NOT NULL,
	language		VARCHAR(50) NOT NULL
	--subtitles		
);

CREATE TABLE IF NOT EXISTS dvd_subtitles(
	dvd_id		UUID REFERENCES dvd(id) ON DELETE CASCADE,
	subtitle	TEXT,
	PRIMARY KEY (dvd_id, subtitle)
);

CREATE TABLE IF NOT EXISTS "order"(
    id				UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status			VARCHAR(10) NOT NULL,
    created_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id		UUID REFERENCES "order"(id) ON DELETE CASCADE,
    product_id		UUID REFERENCES product(id) ON DELETE SET NULL,
    product_name	VARCHAR(255) NOT NULL,
    quantity		INT NOT NULL,
    unit_price		BIGINT NOT NULL,
    unit_weight		NUMERIC(10, 3) NOT NULL,
    UNIQUE (order_id, product_id)
);

CREATE TABLE IF NOT EXISTS delivery_information(
    order_id        UUID PRIMARY KEY REFERENCES "order"(id) ON DELETE CASCADE,
    customer_name	VARCHAR(255),
    customer_email	VARCHAR(255),
    phone_number	VARCHAR(10),
    province		VARCHAR(50),
    commune			VARCHAR(50),
    address			TEXT,
    delivery_method	VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS invoice(
    order_id				UUID PRIMARY KEY UNIQUE REFERENCES "order"(id) ON DELETE CASCADE,
    issued_at				TIMESTAMPTZ NOT NULL,
    total_price_without_vat	BIGINT NOT NULL,
    total_price_with_vat	BIGINT NOT NULL,
    delivery_fee			BIGINT NOT NULL,
    total_amount			BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_transaction(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_content TEXT,
    transaction_timestamp TIMESTAMPTZ NOT NULL,
    transaction_method  VARCHAR(10) NOT NULL,
    amount_paid         BIGINT NOT NULL,
    order_id            UUID NOT NULL REFERENCES "order"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "user"(
    id				UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username		VARCHAR(255) NOT NULL,
    email			VARCHAR(255) NOT NULL UNIQUE,
    hashed_password	TEXT NOT NULL,
    created_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at		TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles(
    user_id		UUID REFERENCES "user"(id) ON DELETE CASCADE,
    role		VARCHAR(20),
    PRIMARY KEY (user_id, role)
);

CREATE TABLE IF NOT EXISTS product_log(
    id			        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    action		        VARCHAR(20) NOT NULL,
    manager_id	        UUID REFERENCES "user"(id) ON DELETE SET NULL,
    manager_username    VARCHAR(255) NOT NULL,
    product_id	        UUID REFERENCES product(id) ON DELETE SET NULL,
    product_title       VARCHAR(255) NOT NULL,
    timestamp	        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stock_adjust_log(
    id			        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    old_stock	        INT NOT NULL,
    new_stock	        INT NOT NULL,
    reason		        TEXT NOT NULL,
    manager_id	        UUID REFERENCES "user"(id) ON DELETE SET NULL,
    manager_username    VARCHAR(255) NOT NULL,
    product_id	        UUID REFERENCES product(id) ON DELETE SET NULL,
    product_title       VARCHAR(255) NOT NULL,
    timestamp	        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admin_log(
    id					UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    action				VARCHAR(20) NOT NULL,
    admin_id			UUID REFERENCES "user"(id) ON DELETE SET NULL,
    admin_username      VARCHAR(255) NOT NULL,
    affected_user_id	UUID REFERENCES "user"(id) ON DELETE SET NULL,
    affected_username   VARCHAR(255) NOT NULL,
    timestamp			TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
