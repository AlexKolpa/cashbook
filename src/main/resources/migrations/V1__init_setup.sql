CREATE TABLE categories (
  id   BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL CONSTRAINT non_empty_name CHECK (length(name) > 0)
);

CREATE TABLE sub_categories (
  id          BIGSERIAL PRIMARY KEY,
  name        TEXT   NOT NULL CONSTRAINT non_empty_name CHECK (length(name) > 0),
  category_id BIGINT NOT NULL CONSTRAINT sub_category_has_category REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE flows (
  id              BIGSERIAL PRIMARY KEY,
  date            DATE   NOT NULL DEFAULT now(),
  category_id     BIGINT NOT NULL CONSTRAINT flow_has_category REFERENCES categories (id),
  sub_category_id BIGINT CONSTRAINT flow_has_sub_category REFERENCES sub_categories (id),
  description     TEXT,
  cost            BIGINT NOT NULL
);

CREATE TYPE FLOW_INTERVAL AS ENUM ('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY');

CREATE TABLE recurring_flows (
  id              BIGSERIAL PRIMARY KEY,
  date            DATE          NOT NULL DEFAULT now(),
  category_id     BIGINT        NOT NULL CONSTRAINT flow_has_category REFERENCES categories (id),
  sub_category_id BIGINT CONSTRAINT flow_has_sub_category REFERENCES sub_categories (id),
  name            TEXT          NOT NULL,
  cost            BIGINT        NOT NULL,
  interval        FLOW_INTERVAL NOT NULL
);
