SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE article_tag;
ALTER TABLE article_tag AUTO_INCREMENT = 1;

TRUNCATE TABLE article;
ALTER TABLE article AUTO_INCREMENT = 1;

TRUNCATE TABLE comments;
ALTER TABLE comments AUTO_INCREMENT = 1;

TRUNCATE TABLE follow_relation;
ALTER TABLE follow_relation AUTO_INCREMENT = 1;

TRUNCATE TABLE tag;
ALTER TABLE tag AUTO_INCREMENT = 1;

TRUNCATE TABLE users;
ALTER TABLE users AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;