INSERT INTO MEMBER (USERID, NICKNAME, EMAIL, PASSWORD, CREATED_AT)
VALUES ('JAVAJIGI', '자바지기', 'JAVAJIGI@SLIPP.NET', '1234', NOW()),
       ('SANJIGI', '산지기', 'SANJIGI@SLIPP.NET', '1234', NOW());

INSERT INTO ARTICLE (TITLE, CONTENTS, USER_ID, CREATED_AT)
VALUES ('TEST1', 'TEST CONTENT1', 2, NOW()),
       ('TEST2', 'TEST CONTENT2', 1, NOW()),
       ('TEST3', 'TEST CONTENT3', 1, NOW());
