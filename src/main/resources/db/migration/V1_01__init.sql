--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` BOOLEAN NOT NULL DEFAULT FALSE,
  `created` datetime DEFAULT NULL,
  `locked` BOOLEAN NOT NULL DEFAULT 0,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `lockedIdx` (`locked`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `authtoken`
--

DROP TABLE IF EXISTS `authtoken`;
CREATE TABLE `authtoken` (
  `uuid` varchar(255) NOT NULL,
  `created` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `ua` varchar(255) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `updatedIdx` (`updated`),
  KEY `FKdp6ee3iccgl3yl9o1hasrvig2` (`user_id`),
  CONSTRAINT `FKdp6ee3iccgl3yl9o1hasrvig2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `credential`
--

DROP TABLE IF EXISTS `credential`;
CREATE TABLE `credential` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` BOOLEAN NOT NULL DEFAULT 0,
  `auth_data` varchar(255) DEFAULT NULL,
  `auth_service_type` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `opt_data` text,
  `uid` varchar(255) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sdmvfrnom5x9vvw41y65h89l7` (`uid`),
  KEY `activeIdx` (`active`),
  KEY `userIdx` (`user_id`),
  KEY `usernameIdx` (`username`),
  KEY `uidIdx` (`uid`),
  CONSTRAINT `FKpg7bdnqxpyhrt7f8soul9y7ne` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `dataform`
--

DROP TABLE IF EXISTS `dataform`;
CREATE TABLE `dataform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emails` varchar(255) DEFAULT NULL,
  `form_fields` longtext,
  `name` varchar(255) DEFAULT NULL,
  `persist` BOOLEAN NOT NULL DEFAULT 0,
  `site_id` int(11) NOT NULL DEFAULT 0,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgpyysyc23jx6kdqoogpsaljxp` (`name`),
  KEY `SITEID_IDX` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `dataform_record`
--

DROP TABLE IF EXISTS `dataform_record`;
CREATE TABLE `dataform_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` longtext,
  `form_id` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `posted` datetime DEFAULT NULL,
  `ua` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `POSTED_IDX` (`posted`),
  KEY `FKchl8iqwjoxehe02fbfo7cmf3n` (`form_id`),
  CONSTRAINT `FKchl8iqwjoxehe02fbfo7cmf3n` FOREIGN KEY (`form_id`) REFERENCES `dataform` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `docfile`
--

DROP TABLE IF EXISTS `docfile`;
CREATE TABLE `docfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL DEFAULT 0,
  `height` int(11) NOT NULL DEFAULT 0,
  `name` varchar(255) DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  `sort_index` int(11) NOT NULL DEFAULT 0,
  `title` varchar(255) DEFAULT NULL,
  `width` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) DEFAULT NULL,
  `home_page` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f9iil6uu8d9ohutpr2irlpvio` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `site_property`
--

DROP TABLE IF EXISTS `site_property`;
CREATE TABLE `site_property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `name` varchar(255) DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK838281vowtk02lc116g7m06x8` (`site_id`),
  CONSTRAINT `FK838281vowtk02lc116g7m06x8` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `page`
--

DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auto_name` BOOLEAN NOT NULL DEFAULT 1,
  `body` longtext,
  `created` datetime DEFAULT NULL,
  `description` longtext,
  `form_id` int(11) DEFAULT NULL,
  `hide_title` BOOLEAN NOT NULL DEFAULT 0,
  `html_title` varchar(255) DEFAULT NULL,
  `include_form` BOOLEAN NOT NULL DEFAULT 0,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int(11) NOT NULL DEFAULT 0,
  `placeholder` BOOLEAN NOT NULL DEFAULT 0,
  `public_page` BOOLEAN NOT NULL DEFAULT 0,
  `rich_text` BOOLEAN NOT NULL DEFAULT 0,
  `site_id` int(11) DEFAULT NULL,
  `sortindex` int(11) NOT NULL DEFAULT 0,
  `tags` longtext,
  `title` varchar(255) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj2jx0gqa4h7wg8ls0k3y221h2` (`site_id`),
  CONSTRAINT `FKj2jx0gqa4h7wg8ls0k3y221h2` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `page_property`
--

DROP TABLE IF EXISTS `page_property`;
CREATE TABLE `page_property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `name` varchar(255) DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKifff538k2kvc7c9h94uw2ois9` (`page_id`),
  CONSTRAINT `FKifff538k2kvc7c9h94uw2ois9` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `embedimage`
--

DROP TABLE IF EXISTS `embedimage`;
CREATE TABLE `embedimage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL DEFAULT 0,
  `height` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  `width` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgh0ubj8lygn4bcgxjb0gcxjss` (`page_id`),
  CONSTRAINT `FKgh0ubj8lygn4bcgxjb0gcxjss` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `imagefile`
--

DROP TABLE IF EXISTS `imagefile`;
CREATE TABLE `imagefile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL DEFAULT 0,
  `height` int(11) NOT NULL,
  `main_image` BOOLEAN NOT NULL DEFAULT 0,
  `name` varchar(255) DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  `sort_index` int(11) NOT NULL DEFAULT 0,
  `width` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `thumbnail`
--

DROP TABLE IF EXISTS `thumbnail`;
CREATE TABLE `thumbnail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `height` int(11) NOT NULL,
  `image_file_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  `width` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mw1r0pi8uplm830k9fkhe1pke` (`image_file_id`),
  CONSTRAINT `FKq7d3xl85499dslkho0lakaorr` FOREIGN KEY (`image_file_id`) REFERENCES `imagefile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

