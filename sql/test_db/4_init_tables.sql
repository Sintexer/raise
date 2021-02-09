INSERT INTO role(id,
                 name)
values (1, 'ADMIN'),
       (2, 'USER');

SELECT setval('role_id_seq', (SELECT MAX(id) from role));

INSERT INTO role_permissions(id,
                             role_id,
                             permission)
values (1, 1, '*'),
       (2, 2, 'user:*'),
       (3, 2, 'test:*');

SELECT setval('role_permissions_id_seq', (SELECT MAX(id) from role_permissions));

INSERT INTO test_category(id, category, parent_id) VALUES (1, 'Programming', NULL),
                                                          (2, 'Math', NULL),
                                                          (3, 'Nature', NULL),
                                                          (4, 'Java', 1),
                                                          (5, 'C++', 1),
                                                          (6, 'Algebra', 2),
                                                          (7, 'Geometry', 2),
                                                          (8, 'Wildlife', 3),
                                                          (9, 'FOR_TESTS', NULL);
SELECT setval('test_category_id_seq', (SELECT MAX(id) from test_category));

INSERT INTO usr(id, email, name, surname, password, status, registration_date)
VALUES (1, 'ilboogl@gmail.com', 'Ilya', 'Buglakov', '3ea87a56da3844b420ec2925ae922bc731ec16a4fc44dcbeafdad49b0e61d39c', 'ACTIVE', TO_DATE('17/12/2021', 'DD/MM/YYYY')),
       (2, 'st@gmail.com', 'Jack', 'Nickles', '3ea87a56da3844b420ec2925ae922bc731ec16a4fc44dcbeafdad49b0e61d39c', 'ACTIVE', TO_DATE('17/12/2021', 'DD/MM/YYYY'));

SELECT setval('user_id_seq', (SELECT MAX(id) from usr));

INSERT INTO usr_roles(id, user_id, role_id)
VALUES (1, 1, 1),
       (2, 2, 2);

SELECT setval('user_roles_id_seq', (SELECT MAX(id) from user_roles));

INSERT INTO test(id, author_id, status, test_name,difficulty,category_id)
VALUES (8, 1, 'CONFIRMED', 'Test test', 1, 9),
       (1, 1, 'CONFIRMED', 'Math test 1', 1, 6),
       (2, 1, 'CONFIRMED', 'Math test 2', 1, 6),
       (3, 1, 'CONFIRMED', 'Math test 3', 1, 6),
       (4, 1, 'CONFIRMED', 'Animals 1', 1, 8),
       (5, 1, 'CONFIRMED', 'Java 1', 1, 4),
       (7, 1, 'NEW', 'Java 2', 1, 4),
       (9, 1, 'CONFIRMED', 'For delete test', 1, 9),
       (10, 1, 'CONFIRMED', 'For delete test', 1, 9),
       (11, 1, 'CONFIRMED', 'For delete test', 1, 9),
       (12, 1, 'CONFIRMED', 'For delete test', 1, 9),
       (13, 1, 'CONFIRMED', 'For update test', 1, 9),
       (14, 1, 'CONFIRMED', 'For update test', 1, 9)
       ;


SELECT setval('test_id_seq', (SELECT MAX(id) from test));

INSERT INTO question(id, name, content, test_id)
VALUES (22, 'QUESTION FOR INSERT ANSWER', '', 5),
       (1, 'Answer question:', 'What type is presented in Java?', 5),
       (2, 'Choose all', 'Choose types, that are presented by only 2 values:', 5),
       (3, 'True or false?', 'Java is a language with dynamic types', 5),
       (4, 'Choose correct answer', '2+2*2=?', 1),
       (5, 'Choose correct answer', '2*2+2*2=?', 1),
       (6, 'Calculate', '621+232/2=?', 1),
       (7, 'Choose correct answer', '621+230/2=?', 1),
       (8, 'Calculate', '1/2=?', 2),
       (9, 'Choose correct answer', '1/(2*2)=?', 2),
       (10, 'Calculate', '1+1+1+1+1+1+1=?', 3),
       (11, 'Choose correct answer', '1*6=?', 3),
       (12, 'Choose correct answer', 'Dog is descendant of ?', 4),
       (13, 'Choose correct answer', 'Cat has ? legs', 4),
       (14, 'Choose correct answer', 'C++ has these types:', 6),
       (15, 'Is the statement true?', 'C++ is a dynamic type language', 6),
       (16, 'Answer question:', 'Do Java have a String pool?', 7),
           (17, 'Java is good', 'This statement is true or false>', 7),
           (18, 'For delete test', 'For delete test', 8),
           (19, 'For delete test', 'For delete test', 8),
           (20, 'For delete test', 'For delete test', 8),
           (21, 'For delete test', 'For delete test', 8);

SELECT setval('question_id_seq', (SELECT MAX(id) from question));

INSERT INTO answer(id, content, correct, question_id)
VALUES (1, 'int', true, 1), (2, 'String', true, 1), (3, 'Tuple', false, 1), (4, 'varchar', false, 1),
       (5, 'boolean', true, 2), (6, 'Boolean', true, 2), (7, 'Integer', true, 2), (8, 'double', true, 2),
       (9, 'True', false, 3), (10, 'False', true, 3),
       (11, '6', true, 4), (12, '7', false, 4), (13, '8', false, 4), (14, '4', false, 4), (15, '12', false, 4),
       (16, '6', false, 5), (17, '7', false, 5), (18, '8', true, 5), (19, '4', false, 5), (20, '12', false, 5),
       (21, '731', true, 6), (22, '688', false, 6), (23, '732', false, 6), (24, '721', false, 6), (25, '722', false, 6),
       (26, '731', false, 7), (27, '688', false, 7), (28, '728', true, 7), (29, '721', false, 7), (30, '722', false, 7),
       (31, '0.2', false, 8), (32, '1', false, 8), (33, '0.5', true, 8), (34, '0', false, 8),
       (35, '0.2', false, 9), (36, '0.25', true, 9), (37, '0.5', false, 9), (38, '1', false, 9),
       (39, '6', false, 10), (40, '7', true, 10), (41, '65', false, 10), (42, '5', false, 10),
       (43, '6', true, 11), (44, '7', false, 11), (45, '1', false, 11), (46, '0', false, 11),
       (49, 'Wolf', true, 12), (47, 'Bear', false, 12), (48, 'Cat', false, 12),
       (50, '2', false, 13), (51, '3', false, 13), (52, '5', false, 13), (53, '4', true, 13),
       (54, 'bool', true, 14), (55, 'int', true, 14), (56, 'string', true, 14), (57, 'char', true, 14), (58, 'tuple', false, 14), (59, 'arraylist', false, 14),
       (60, 'True', false, 15), (61, 'False', true, 15),
       (62, 'Yes', true, 16), (63, 'No', false, 16),
       (64, 'True', true, 17), (65, 'False', false, 17);

SELECT setval('answer_id_seq', (SELECT MAX(id) from usr));

INSERT INTO test_characteristic(id, characteristic, test_id)
VALUES (1, 'MEMORY', 1),
       (2, 'CALCULATIONS', 1),
       (3, 'MEMORY', 2),
       (4, 'CALCULATIONS', 2),
       (5, 'MEMORY', 3),
       (6, 'CALCULATIONS', 3),
       (7, 'MEMORY', 4),
       (8, 'MEMORY', 5),
       (9, 'LOGIC', 5),
       (10, 'MEMORY', 6),
       (11, 'LOGIC', 6),
       (12, 'MEMORY', 7),
       (13, 'LOGIC', 7);

SELECT setval('test_characteristic_id_seq', (SELECT MAX(id) from test_characteristic));


--        731




