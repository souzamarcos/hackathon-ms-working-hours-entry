CREATE TABLE `order`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `client_id` BIGINT NULL,
    `status` BIGINT NOT NULL,
    `total` DOUBLE NOT NULL,
    `payment_status` BIGINT,
    `created_at` DATETIME NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `deleted_at` DATETIME DEFAULT NULL,
    CONSTRAINT `pk_id` PRIMARY KEY (`id`),
    INDEX ix_deleted_at (deleted_at)
);

CREATE TABLE `order_item`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `comment` VARCHAR(255) NULL,
    CONSTRAINT `pk_id` PRIMARY KEY (id)
);

CREATE TABLE `order_item_additional`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_item_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    CONSTRAINT `pk_id` PRIMARY KEY (id),
    CONSTRAINT `fk_order_item_additional_order_item_id` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`id`)
);