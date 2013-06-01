use schedule;


DELETE FROM `Properties`;
DELETE FROM `Equipment`;
DELETE FROM `RoomEquip`;
DELETE FROM `Rooms`;
DELETE FROM `Teachers`;
DELETE FROM `TeachGroup`;
DELETE FROM `Groups`;
DELETE FROM `Subjects`;
DELETE FROM `SubjectTypes`;
DELETE FROM `SubEquip`;
DELETE FROM `Students`;
DELETE FROM `StudGroup`;
DELETE FROM `Plan`;


INSERT INTO `Properties` (`prop_id`, `prop_key`, `val`) VALUES
(1, 'days', '2'),
(2, 'times', '4'),
(3, 'negotiation_level', '20');


INSERT INTO `Equipment` (`equip_id`, `symbol`, `name`) VALUES
(1, 'kompy', 'komputery'),
(2, 'rzutnik', 'rzutnik'),
(3, 'tabInt', 'tablica interaktywna');


INSERT INTO `RoomEquip` (`roomEquip_id`, `room_id`, `equip_id`) VALUES
(1, 1, 2), # rzutnik dla sali 1
(2, 1, 3), # tabl. interaktywna dla sali 1
(3, 2, 2), # rzutnik dla sali 2
(4, 3, 1); # komutery dla sali 3


INSERT INTO `Rooms` (`room_id`, `no`, `capacity`) VALUES
(1, 1, 50), # sala wykładowa, ma rzutnik i tablicę interaktywną
(2, 2, 25), # zwykła sala, ma rzutnik 
(3, 3, 18), # sala laboratoryjna, ma komputery
(4, 4, 18); # zwykła sala ćwiczeniowa, nic nie ma


INSERT INTO `Teachers` (`teacher_id`, `degrees`, `symbol`, `name`, `surname`, `daysPriority`, `timesPriority`) VALUES
(1, 'prof. dr hab.', 'jkowalski', 'Jan', 'Kowalski', '05', '00005441'),
(2, 'mgr', 'znowak', 'Zbigniew', 'Nowak', '35', '01230555'),
(3, 'dr',  'pnauczyciel', 'Pan', 'Nauczyciel', '25', '00155555'),
(4, 'dr', 'siksinski', 'Stefan', 'Ikisiński', '52', '01550055');


INSERT INTO `TeachGroup` (`teachGroup_id`, `teacher_id`, `group_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 8),
(4, 1, 10),
(5, 2, 3),
(6, 2, 4),
(7, 2, 7),
(8, 3, 5),
(9, 3, 6),
(10, 3, 9),
(11, 4, 11),
(12, 4, 12);


INSERT INTO `Subjects` (`sub_id`, `symbol`, `name`, `subTyp_id`) VALUES
(1, 'AAA_W', 'AAA', 1),
(2, 'AAA', 'AAA', 3),
(3, 'BBB_W', 'BBB', 1),
(4, 'BBB', 'BBB', 2),
(5, 'SEM', 'Seminarium', 7),
(6, 'CCC_W', 'CCC', 5),
(7, 'CCC', 'CCC', 6);


INSERT INTO `SubjectTypes` (`subTyp_id`, `type_symbol`, `type_name`, `sort_symbol`, `sort_name`, `priority`) VALUES
(1, 'stand', 'standard', 'wyk', 'wyklad', 10),
(2, 'stand', 'standard', 'cw', 'ćwiczenia', 9),
(3, 'stand', 'standard', 'lab', 'laboratorium', 9),
(4, 'lekt', 'lektorat', 'lekt', 'lektorat', 6),
(5, 'fak', 'fakultet', 'wyk', 'wyklad', 8),
(6, 'fak', 'fakultet', 'lab', 'wyklad', 7),
(7, 'sem', 'seminarium', 'sem', 'seminarium', 6);


INSERT INTO `SubEquip` (`subEquip_id`, `sub_id`, `equip_id`) VALUES
(1, 1, 2),
(2, 1, 3),
(3, 2, 1),
(4, 3, 2),
(5, 5, 2),
(6, 6, 2),
(7, 6, 3),
(8, 7, 1);


INSERT INTO `Groups` (`group_id`, `symbol`, `sub_id`, `stud_no`) VALUES
(1, 'A_WYK', 1, 50), # jkowalski
(2, 'A_LAB1', 2, 18), # jkowalski
(3, 'A_LAB2', 2, 18), # znowak
(4, 'A_LAB3', 2, 14), # znowak
(5, 'B_WYK', 3, 20), # pnauczyciel
(6, 'B_CW1', 4, 10), # pnauczyciel
(7, 'B_CW2', 4, 10), # znowak
(8, 'SEM_1', 5, 8), # jkowalski
(9, 'SEM_2', 5, 10), # pnauczyciel
(10, 'C_WYK', 6, 32), # jkowalski
(11, 'C_LAB1', 7, 16), # siksinski 
(12, 'C_LAB2', 7, 16); # siksinski


INSERT INTO Students (`stud_id`, `index_no`) VALUES
(1, '000001'),
(2, '000002'),
(3, '000003'),
(4, '000004'),
(5, '000005'),
(6, '000006'),
(7, '000007'),
(8, '000008'),
(9, '000009'),
(10, '000010'),
(11, '000011'),
(12, '000012'),
(13, '000013'),
(14, '000014'),
(15, '000015'),
(16, '000016'),
(17, '000017'),
(18, '000018'),
(19, '000019'),
(20, '000020'),
(21, '000021'),
(22, '000022'),
(23, '000023'),
(24, '000024'),
(25, '000025'),
(26, '000026'),
(27, '000027'),
(28, '000028'),
(29, '000029'),
(30, '000030'),
(31, '000031'),
(32, '000032'),
(33, '000033'),
(34, '000034'),
(35, '000035'),
(36, '000036'),
(37, '000037'),
(38, '000038'),
(39, '000039'),
(40, '000040'),
(41, '000041'),
(42, '000042'),
(43, '000043'),
(44, '000044'),
(45, '000045'),
(46, '000046'),
(47, '000047'),
(48, '000048'),
(49, '000049'),
(50, '000050');


INSERT INTO StudGroup (`stud_id`, `group_id`) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1), (11, 1), (12, 1), (13, 1), (14, 1), (15, 1), (16, 1), (17, 1), (18, 1), (19, 1), (20, 1), (21, 1), (22, 1), (23, 1), (24, 1), (25, 1), (26, 1), (27, 1), (28, 1), (29, 1), (30, 1), (31, 1), (32, 1), (33, 1), (34, 1), (35, 1), (36, 1), (37, 1), (38, 1), (39, 1), (40, 1), (41, 1), (42, 1), (43, 1), (44, 1), (45, 1), (46, 1), (47, 1), (48, 1), (49, 1), (50, 1),
(1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2), (11, 2), (12, 2), (13, 2), (14, 2), (15, 2), (16, 2), (17, 2), (18, 2),
(19, 3), (20, 3), (21, 3), (22, 3), (23, 3), (24, 3), (25, 3), (26, 3), (27, 3), (28, 3), (29, 3), (30, 3), (31, 3), (32, 3), (33, 3), (34, 3), (35, 3), (36, 3),
(37, 4), (38, 4), (39, 4), (40, 4), (41, 4), (42, 4), (43, 4), (44, 4), (45, 4), (46, 4), (47, 4), (48, 4), (49, 4), (50, 4),
(1, 5), (2, 5), (3, 5), (4, 5), (5, 5), (6, 5), (7, 5), (8, 5), (9, 5), (10, 5), (11, 5), (12, 5), (13, 5), (14, 5), (15, 5), (16, 5), (17, 5), (18, 5), (19, 5), (20, 5),
(1, 6), (2, 6), (3, 6), (4, 6), (5, 6), (6, 6), (7, 6), (8, 6), (9, 6), (10, 6),
(11, 7), (12, 7), (13, 7), (14, 7), (15, 7), (16, 7), (17, 7), (18, 7), (19, 7), (20, 7),
(19, 10), (20, 10), (21, 10), (22, 10), (23, 10), (24, 10), (25, 10), (26, 10), (27, 10), (28, 10), (29, 10), (30, 10), (31, 10), (32, 10), (33, 10), (34, 10), (35, 10), (36, 10), (37, 10), (38, 10), (39, 10), (40, 10), (41, 10), (42, 10), (43, 10), (44, 10), (45, 10), (46, 10), (47, 10), (48, 10), (49, 10), (50, 10),
(19, 11), (20, 11), (21, 11), (22, 11), (23, 11), (24, 11), (25, 11), (26, 11), (27, 11), (28, 11), (29, 11), (30, 11), (31, 11), (32, 11), (33, 11), (34, 11),
(35, 12), (36, 12), (37, 12), (38, 12), (39, 12), (40, 12), (41, 12), (42, 12), (43, 12), (44, 12), (45, 12), (46, 12), (47, 12), (48, 12), (49, 12), (50, 12),
(1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (7, 8), (8, 8),
(41, 9), (42, 9), (43, 9), (44, 9), (45, 9), (46, 9), (47, 9), (48, 9), (49, 9), (50, 9);
