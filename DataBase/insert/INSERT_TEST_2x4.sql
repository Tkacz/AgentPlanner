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
DELETE FROM `RejectGroups`;


INSERT INTO `Properties` (`prop_id`, `prop_key`, `val`) VALUES
(1, 'days', '2'),
(2, 'times', '4'),
(3, 'negotiation_level', '20');

INSERT INTO `Equipment` (`equip_id`, `symbol`, `name`) VALUES
(1, 'kompy', 'komputery'),
(2, 'rzutnik', 'rzutnik'),
(3, 'tabInt', 'tablica interaktywna');

INSERT INTO `RoomEquip` (`roomEquip_id`, `room_id`, `equip_id`) VALUES
(1, 1, 2),
(2, 1, 3),
(3, 2, 2),
(4, 2, 3),
(5, 3, 2),
(6, 3, 3),
(7, 11, 2),
(8, 12, 2),
(9, 13, 2),
(10, 14, 2),
(11, 15, 1),
(12, 15, 2),
(13, 16, 1),
(14, 16, 2),
(15, 17, 1),
(16, 17, 2),
(17, 18, 1),
(18, 18, 2),
(19, 19, 1),
(20, 19, 2),
(21, 20, 1),
(22, 20, 2),
(23, 20, 3),
(24, 21, 1),
(25, 21, 2),
(26, 21, 3);


INSERT INTO `Rooms` (`room_id`, `no`, `capacity`) VALUES
(1, 1, 96),
(2, 2, 112),
(3, 3, 178),
(4, 122, 20),
(5, 309, 24),
(6, 310, 22),
(7, 311, 22),
(8, 327, 18),
(9, 328, 22),
(10, 47, 24),
(11, 116, 50),
(12, 209, 50),
(13, 227, 50),
(14, 52, 34),
(15, 109, 25),
(16, 110, 21),
(17, 137, 20),
(18, 210, 21),
(19, 29, 21),
(20, 39, 21),
(21, 51, 20);


INSERT INTO `Teachers` (`teacher_id`, `symbol`, `daysPriority`, `timesPriority`) VALUES
(1, 'T1', '44', '44444444'),
(2, 'T2', '44', '44444444'),
(3, 'T3', '44', '44444444'),
(4, 'T4', '44', '44444444'),
(5, 'T5', '44', '44444444'),
(6, 'T6', '44', '44444444'),
(7, 'T7', '44', '44444444'),
(8, 'T8', '44', '44444444'),
(9, 'T9', '44', '44444444'),
(10, 'T10', '44', '44444444'),
(11, 'T11', '44', '44444444'),
(12, 'T12', '44', '44444444'),
(13, 'T13', '44', '44444444'),
(14, 'T14', '44', '44444444');


INSERT INTO `Subjects` (`sub_id`, `symbol`, `name`, `subTyp_id`) VALUES
(1, 'AAA_W', 'AAA', 1), # 2, 3
(2, 'AAA', 'AAA', 3), # 1
(3, 'BBB_W', 'BBB', 1), # 2
(4, 'BBB', 'BBB', 2),
(5, 'SEM', 'Seminarium', 7), # 2
(6, 'CCC_W', 'CCC', 5), # 2, 3
(7, 'CCC', 'CCC', 6), # 1
(8, 'DDD_W', 'DDD', 1), # 2, 3
(9, 'DDD', 'DDD', 3), # 1
(10, 'EEE_W', 'EEE', 1), # 2
(11, 'EEE', 'EEE', 2),
(12, 'FFF', 'FFF', 4),
(13, 'GGG_W', 'GGG', 1), # 2, 3
(14, 'GGG', 'GGG', 3), # 1
(15, 'HHH_W', 'HHH', 1), # 2, 3
(16, 'HHH', 'HHH', 2),
(17, 'III_W', 'III', 5), # 2
(18, 'III', 'III', 6), # 1
(19, 'JJJ_W', 'JJJ', 5), # 2, 3
(20, 'JJJ', 'JJJ', 6), # 1
(21, 'KKK_W', 'KKK', 1), # 2
(22, 'KKK', 'KKK', 3); # 1


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
(8, 7, 1),
(9, 8, 2),
(10, 8, 3),
(11, 9, 1),
(12, 10, 2),
(13, 13, 2),
(14, 13, 3),
(15, 14, 1),
(16, 15, 2),
(17, 15, 3),
(18, 17, 2),
(19, 18, 1),
(20, 19, 2),
(21, 19, 3),
(22, 20, 1),
(23, 21, 2),
(24, 22, 1);



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
(12, 4, 12),
(13, 5, 13),
(14, 5, 14),
(15, 5, 15),
(16, 5, 16),
(17, 5, 17),
(18, 6, 18),
(19, 6, 19),
(20, 6, 20),
(21, 1, 21),
(22, 1, 22),
(23, 5, 23),
(24, 6, 24),
(25, 6, 25),
(26, 7, 26),
(27, 7, 27),
(28, 8, 28),
(29, 8, 29),
(30, 9, 30),
(31, 10, 31),
(32, 5, 32),
(33, 3, 33),
(34, 6, 34),
(35, 2, 35),
(36, 7, 36),
(37, 8, 37),
(38, 9, 38),
(39, 10, 39),
(40, 10, 40),
(41, 11, 41),
(42, 11, 42),
(43, 12, 43),
(44, 12, 44),
(45, 13, 45),
(46, 13, 46),
(47, 12, 47),
(48, 12, 48),
(49, 12, 49),
(50, 1, 50),
(51, 1, 51),
(52, 11, 52),
(53, 11, 53),
(54, 14, 54),
(55, 14, 55),
(56, 14, 56),
(57, 5, 57),
(58, 2, 58),
(59, 2, 59);



INSERT INTO `Groups` (`group_id`, `symbol`, `sub_id`, `stud_no`) VALUES
(1, 'AW', 1, 50), # T1
(2, 'A1', 2, 18), # T1
(3, 'A2', 2, 18), # T2
(4, 'A3', 2, 14), # T2
(5, 'BW', 3, 20), # T3
(6, 'B1', 4, 10), # T4
(7, 'B2', 4, 10), # T2
(8, 'SEM1', 5, 8), # T1
(9, 'SEM2', 5, 10), # T3
(10, 'CW', 6, 32), # T1
(11, 'C1', 7, 16), # T4 
(12, 'C2', 7, 16), # T4
(13, 'DW', 8, 120), # T5
(14, 'D1', 9, 18), # T5
(15, 'D2', 9, 18), # T5
(16, 'D3', 9, 18), # T5
(17, 'D4', 9, 18), # T5
(18, 'D5', 9, 16), # T6
(19, 'D6', 9, 16), # T6
(20, 'D7', 9, 16), # T6
(21, 'EW', 10, 120), # T1
(22, 'E1', 11, 30), # T1
(23, 'E2', 11, 30), # T5
(24, 'E3', 11, 30), # T6
(25, 'E4', 11, 30), # T6
(26, 'L1', 12, 20), # T7
(27, 'L2', 12, 20), # T7
(28, 'L3', 12, 20), # T8
(29, 'L4', 12, 20), # T8
(30, 'L5', 12, 20), # T9
(31, 'L6', 12, 20), # T10
(32, 'SEM3', 5, 12), # T5
(33, 'SEM4', 5, 18), # T3
(34, 'SEM5', 5, 12), # T6
(35, 'SEM6', 5, 18), # T2
(36, 'L7', 12, 20), # T7
(37, 'L8', 12, 20), # T8
(38, 'L9', 12, 20), # T9
(39, 'L10', 12, 20), # T10
(40, 'L11', 12, 20), # T10
(41, 'GW', 13, 100), # T11
(42, 'G1', 14, 20), # T11
(43, 'G2', 14, 20), # T12
(44, 'G3', 14, 20), # T12
(45, 'G4', 14, 20), # T13
(46, 'G5', 14, 20), # T13
(47, 'HW', 15, 60), # T12 
(48, 'H1', 16, 30), # T12
(49, 'H2', 16, 30), # T12
(50, 'IW', 17, 60), # T1
(51, 'I1', 18, 20), # T1
(52, 'I2', 18, 20), # T11
(53, 'I3', 18, 20), # T11
(54, 'JW', 19, 40), # T14
(55, 'J1', 20, 20), # T14
(56, 'J2', 20, 20), # T14
(57, 'KW', 21, 40), # T5
(58, 'K1', 22, 20), # T2
(59, 'K2', 22, 20); # T2




INSERT INTO Students (`stud_id`, `index_no`) VALUES
(1, '000001'), (2, '000002'), (3, '000003'), (4, '000004'), (5, '000005'), (6, '000006'), (7, '000007'), (8, '000008'), (9, '000009'), (10, '000010'), (11, '000011'), (12, '000012'), (13, '000013'), (14, '000014'), (15, '000015'), (16, '000016'), (17, '000017'), (18, '000018'), (19, '000019'), (20, '000020'), (21, '000021'), (22, '000022'), (23, '000023'), (24, '000024'), (25, '000025'), (26, '000026'), (27, '000027'), (28, '000028'), (29, '000029'), (30, '000030'), (31, '000031'), (32, '000032'), (33, '000033'), (34, '000034'), (35, '000035'), (36, '000036'), (37, '000037'), (38, '000038'), (39, '000039'), (40, '000040'), (41, '000041'), (42, '000042'), (43, '000043'), (44, '000044'), (45, '000045'), (46, '000046'), (47, '000047'), (48, '000048'), (49, '000049'), (50, '000050'), (51, '000051'), (52, '000052'), (53, '000053'), (54, '000054'), (55, '000055'), (56, '000056'), (57, '000057'), (58, '000058'), (59, '000059'), (60, '000060'), (61, '000061'), (62, '000062'), (63, '000063'), (64, '000064'), (65, '000065'), (66, '000066'), (67, '000067'), (68, '000068'), (69, '000069'), (70, '000070'), (71, '000071'), (72, '000072'), (73, '000073'), (74, '000074'), (75, '000075'), (76, '000076'), (77, '000077'), (78, '000078'), (79, '000079'), (80, '000080'), (81, '000081'), (82, '000082'), (83, '000083'), (84, '000084'), (85, '000085'), (86, '000086'), (87, '000087'), (88, '000088'), (89, '000089'), (90, '000090'), (91, '000091'), (92, '000092'), (93, '000093'), (94, '000094'), (95, '000095'), (96, '000096'), (97, '000097'), (98, '000098'), (99, '000099'), (100, '000100'), (101, '000101'), (102, '000102'), (103, '000103'), (104, '000104'), (105, '000105'), (106, '000106'), (107, '000107'), (108, '000108'), (109, '000109'), (110, '000110'), (111, '000111'), (112, '000112'), (113, '000113'), (114, '000114'), (115, '000115'), (116, '000116'), (117, '000117'), (118, '000118'), (119, '000119'), (120, '000120'),
(121, '000121'), (122, '000122'), (123, '000123'), (124, '000124'), (125, '000125'), (126, '000126'), (127, '000127'), (128, '000128'), (129, '000129'), (130, '000130'), (131, '000131'), (132, '000132'), (133, '000133'), (134, '000134'), (135, '000135'), (136, '000136'), (137, '000137'), (138, '000138'), (139, '000139'), (140, '000140'), (141, '000141'), (142, '000142'), (143, '000143'), (144, '000144'), (145, '000145'), (146, '000146'), (147, '000147'), (148, '000148'), (149, '000149'), (150, '000150'), (151, '000151'), (152, '000152'), (153, '000153'), (154, '000154'), (155, '000155'), (156, '000156'), (157, '000157'), (158, '000158'), (159, '000159'), (160, '000160'), (161, '000161'), (162, '000162'), (163, '000163'), (164, '000164'), (165, '000165'), (166, '000166'), (167, '000167'), (168, '000168'), (169, '000169'), (170, '000170'), (171, '000171'), (172, '000172'), (173, '000173'), (174, '000174'), (175, '000175'), (176, '000176'), (177, '000177'), (178, '000178'), (179, '000179'), (180, '000180'), (181, '000181'), (182, '000182'), (183, '000183'), (184, '000184'), (185, '000185'), (186, '000186'), (187, '000187'), (188, '000188'), (189, '000189'), (190, '000190'), (191, '000191'), (192, '000192'), (193, '000193'), (194, '000194'), (195, '000195'), (196, '000196'), (197, '000197'), (198, '000198'), (199, '000199'), (200, '000200');


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
(41, 9), (42, 9), (43, 9), (44, 9), (45, 9), (46, 9), (47, 9), (48, 9), (49, 9), (50, 9),
(1, 13), (2, 13), (3, 13), (4, 13), (5, 13), (6, 13), (7, 13), (8, 13), (9, 13), (10, 13), (11, 13), (12, 13), (13, 13), (14, 13), (15, 13), (16, 13), (17, 13), (18, 13), (19, 13), (20, 13), (21, 13), (22, 13), (23, 13), (24, 13), (25, 13), (26, 13), (27, 13), (28, 13), (29, 13), (30, 13), (31, 13), (32, 13), (33, 13), (34, 13), (35, 13), (36, 13), (37, 13), (38, 13), (39, 13), (40, 13), (41, 13), (42, 13), (43, 13), (44, 13), (45, 13), (46, 13), (47, 13), (48, 13), (49, 13), (50, 13), (51, 13), (52, 13), (53, 13), (54, 13), (55, 13), (56, 13), (57, 13), (58, 13), (59, 13), (60, 13), (61, 13), (62, 13), (63, 13), (64, 13), (65, 13), (66, 13), (67, 13), (68, 13), (69, 13), (70, 13), (71, 13), (72, 13), (73, 13), (74, 13), (75, 13), (76, 13), (77, 13), (78, 13), (79, 13), (80, 13), (81, 13), (82, 13), (83, 13), (84, 13), (85, 13), (86, 13), (87, 13), (88, 13), (89, 13), (90, 13), (91, 13), (92, 13), (93, 13), (94, 13), (95, 13), (96, 13), (97, 13), (98, 13), (99, 13), (100, 13), (101, 13), (102, 13), (103, 13), (104, 13), (105, 13), (106, 13), (107, 13), (108, 13), (109, 13), (110, 13), (111, 13), (112, 13), (113, 13), (114, 13), (115, 13), (116, 13), (117, 13), (118, 13), (119, 13), (120, 13),
(1, 14), (2, 14), (3, 14), (4, 14), (5, 14), (6, 14), (7, 14), (8, 14), (9, 14), (10, 14), (11, 14), (12, 14), (13, 14), (14, 14), (15, 14), (16, 14), (17, 14), (18, 14),
(19, 15), (20, 15), (21, 15), (22, 15), (23, 15), (24, 15), (25, 15), (26, 15), (27, 15), (28, 15), (29, 15), (30, 15), (31, 15), (32, 15), (33, 15), (34, 15), (35, 15), (36, 15),
(37, 16), (38, 16), (39, 16), (40, 16), (41, 16), (42, 16), (43, 16), (44, 16), (45, 16), (46, 16), (47, 16), (48, 16), (49, 16), (50, 16), (51, 16), (52, 16), (53, 16), (54, 16),
(55, 17), (56, 17), (57, 17), (58, 17), (59, 17), (60, 17), (61, 17), (62, 17), (63, 17), (64, 17), (65, 17), (66, 17), (67, 17), (68, 17), (69, 17), (70, 17), (71, 17), (72, 17),
(73, 18), (74, 18), (75, 18), (76, 18), (77, 18), (78, 18), (79, 18), (80, 18), (81, 18), (82, 18), (83, 18), (84, 18), (85, 18), (86, 18), (87, 18), (88, 18),
(89, 19), (90, 19), (91, 19), (92, 19), (93, 19), (94, 19), (95, 19), (96, 19), (97, 19), (98, 19), (99, 19), (100, 19), (101, 19), (102, 19), (103, 19), (104, 19),
(105, 20), (106, 20), (107, 20), (108, 20), (109, 20), (110, 20), (111, 20), (112, 20), (113, 20), (114, 20), (115, 20), (116, 20), (117, 20), (118, 20), (119, 20), (120, 20),
(1, 21), (2, 21), (3, 21), (4, 21), (5, 21), (6, 21), (7, 21), (8, 21), (9, 21), (10, 21), (11, 21), (12, 21), (13, 21), (14, 21), (15, 21), (16, 21), (17, 21), (18, 21), (19, 21), (20, 21), (21, 21), (22, 21), (23, 21), (24, 21), (25, 21), (26, 21), (27, 21), (28, 21), (29, 21), (30, 21), (31, 21), (32, 21), (33, 21), (34, 21), (35, 21), (36, 21), (37, 21), (38, 21), (39, 21), (40, 21), (41, 21), (42, 21), (43, 21), (44, 21), (45, 21), (46, 21), (47, 21), (48, 21), (49, 21), (50, 21), (51, 21), (52, 21), (53, 21), (54, 21), (55, 21), (56, 21), (57, 21), (58, 21), (59, 21), (60, 21), (61, 21), (62, 21), (63, 21), (64, 21), (65, 21), (66, 21), (67, 21), (68, 21), (69, 21), (70, 21), (71, 21), (72, 21), (73, 21), (74, 21), (75, 21), (76, 21), (77, 21), (78, 21), (79, 21), (80, 21), (81, 21), (82, 21), (83, 21), (84, 21), (85, 21), (86, 21), (87, 21), (88, 21), (89, 21), (90, 21), (91, 21), (92, 21), (93, 21), (94, 21), (95, 21), (96, 21), (97, 21), (98, 21), (99, 21), (100, 21), (101, 21), (102, 21), (103, 21), (104, 21), (105, 21), (106, 21), (107, 21), (108, 21), (109, 21), (110, 21), (111, 21), (112, 21), (113, 21), (114, 21), (115, 21), (116, 21), (117, 21), (118, 21), (119, 21), (120, 21),
(1, 22), (2, 22), (3, 22), (4, 22), (5, 22), (6, 22), (7, 22), (8, 22), (9, 22), (10, 22), (11, 22), (12, 22), (13, 22), (14, 22), (15, 22), (16, 22), (17, 22), (18, 22), (19, 22), (20, 22), (21, 22), (22, 22), (23, 22), (24, 22), (25, 22), (26, 22), (27, 22), (28, 22), (29, 22), (30, 22),
(31, 23), (32, 23), (33, 23), (34, 23), (35, 23), (36, 23), (37, 23), (38, 23), (39, 23), (40, 23), (41, 23), (42, 23), (43, 23), (44, 23), (45, 23), (46, 23), (47, 23), (48, 23), (49, 23), (50, 23), (51, 23), (52, 23), (53, 23), (54, 23), (55, 23), (56, 23), (57, 23), (58, 23), (59, 23), (60, 23),
(61, 24), (62, 24), (63, 24), (64, 24), (65, 24), (66, 24), (67, 24), (68, 24), (69, 24), (70, 24), (71, 24), (72, 24), (73, 24), (74, 24), (75, 24), (76, 24), (77, 24), (78, 24), (79, 24), (80, 24), (81, 24), (82, 24), (83, 24), (84, 24), (85, 24), (86, 24), (87, 24), (88, 24), (89, 24), (90, 24),
(91, 25), (92, 25), (93, 25), (94, 25), (95, 25), (96, 25), (97, 25), (98, 25), (99, 25), (100, 25), (101, 25), (102, 25), (103, 25), (104, 25), (105, 25), (106, 25), (107, 25), (108, 25), (109, 25), (110, 25), (111, 25), (112, 25), (113, 25), (114, 25), (115, 25), (116, 25), (117, 25), (118, 25), (119, 25), (120, 25),
(1, 26), (2, 26), (3, 26), (4, 26), (5, 26), (6, 26), (7, 26), (8, 26), (9, 26), (10, 26), (11, 26), (12, 26), (13, 26), (14, 26), (15, 26), (16, 26), (17, 26), (18, 26), (19, 26), (20, 26),
(21, 27), (22, 27), (23, 27), (24, 27), (25, 27), (26, 27), (27, 27), (28, 27), (29, 27), (30, 27), (31, 27), (32, 27), (33, 27), (34, 27), (35, 27), (36, 27), (37, 27), (38, 27), (39, 27), (40, 27),
(41, 28), (42, 28), (43, 28), (44, 28), (45, 28), (46, 28), (47, 28), (48, 28), (49, 28), (50, 28), (51, 28), (52, 28), (53, 28), (54, 28), (55, 28), (56, 28), (57, 28), (58, 28), (59, 28), (60, 28),
(61, 29), (62, 29), (63, 29), (64, 29), (65, 29), (66, 29), (67, 29), (68, 29), (69, 29), (70, 29), (71, 29), (72, 29), (73, 29), (74, 29), (75, 29), (76, 29), (77, 29), (78, 29), (79, 29), (80, 29),
(81, 30), (82, 30), (83, 30), (84, 30), (85, 30), (86, 30), (87, 30), (88, 30), (89, 30), (90, 30), (91, 30), (92, 30), (93, 30), (94, 30), (95, 30), (96, 30), (97, 30), (98, 30), (99, 30), (100, 30),
(101, 31), (102, 31), (103, 31), (104, 31), (105, 31), (106, 31), (107, 31), (108, 31), (109, 31), (110, 31), (111, 31), (112, 31), (113, 31), (114, 31), (115, 31), (116, 31), (117, 31), (118, 31), (119, 31), (120, 31),
(51, 32), (52, 32), (53, 32), (54, 32), (55, 32), (56, 32), (57, 32), (58, 32), (59, 32), (60, 32), (61, 32), (62, 32),
(63, 33), (64, 33), (65, 33), (66, 33), (67, 33), (68, 33), (69, 33), (70, 33), (71, 33), (72, 33), (73, 33), (74, 33), (75, 33), (76, 33), (77, 33), (78, 33), (79, 33), (80, 33),
(81, 34), (82, 34), (83, 34), (84, 34), (85, 34), (86, 34), (87, 34), (88, 34), (89, 34), (90, 34), (91, 34), (92, 34),
(93, 35), (94, 35), (95, 35), (96, 35), (97, 35), (98, 35), (99, 35), (100, 35), (101, 35), (102, 35), (103, 35), (104, 35), (105, 35), (106, 35), (107, 35), (108, 35), (109, 35), (110, 35), (111, 35), (112, 35), (113, 35), (114, 35), (115, 35), (116, 35), (117, 35), (118, 35), (119, 35), (120, 35),
(121, 36), (122, 36), (123, 36), (124, 36), (125, 36), (126, 36), (127, 36), (128, 36), (129, 36), (130, 36), (131, 36), (132, 36), (133, 36), (134, 36), (135, 36), (136, 36), (137, 36), (138, 36), (139, 36), (140, 36),
(141, 37), (142, 37), (143, 37), (144, 37), (145, 37), (146, 37), (147, 37), (148, 37), (149, 37), (150, 37), (151, 37), (152, 37), (153, 37), (154, 37), (155, 37), (156, 37), (157, 37), (158, 37), (159, 37), (160, 37),
(161, 38), (162, 38), (163, 38), (164, 38), (165, 38), (166, 38), (167, 38), (168, 38), (169, 38), (170, 38), (171, 38), (172, 38), (173, 38), (174, 38), (175, 38), (176, 38), (177, 38), (178, 38), (179, 38), (180, 38), 
(181, 39), (182, 39), (183, 39), (184, 39), (185, 39), (186, 39), (187, 39), (188, 39), (189, 39), (190, 39), (191, 39), (192, 39), (193, 39), (194, 39), (195, 39), (196, 39), (197, 39), (198, 39), (199, 39), (200, 39),
(201, 40), (202, 40), (203, 40), (204, 40), (205, 40), (206, 40), (207, 40), (208, 40), (209, 40), (210, 40), (211, 40), (212, 40), (213, 40), (214, 40), (215, 40), (216, 40), (217, 40), (218, 40), (219, 40), (220, 40), 
(121, 41), (122, 41), (123, 41), (124, 41), (125, 41), (126, 41), (127, 41), (128, 41), (129, 41), (130, 41), (131, 41), (132, 41), (133, 41), (134, 41), (135, 41), (136, 41), (137, 41), (138, 41), (139, 41), (140, 41), (141, 41), (142, 41), (143, 41), (144, 41), (145, 41), (146, 41), (147, 41), (148, 41), (149, 41), (150, 41), (151, 41), (152, 41), (153, 41), (154, 41), (155, 41), (156, 41), (157, 41), (158, 41), (159, 41), (160, 41), (161, 41), (162, 41), (163, 41), (164, 41), (165, 41), (166, 41), (167, 41), (168, 41), (169, 41), (170, 41), (171, 41), (172, 41), (173, 41), (174, 41), (175, 41), (176, 41), (177, 41), (178, 41), (179, 41), (180, 41), (181, 41), (182, 41), (183, 41), (184, 41), (185, 41), (186, 41), (187, 41), (188, 41), (189, 41), (190, 41), (191, 41), (192, 41), (193, 41), (194, 41), (195, 41), (196, 41), (197, 41), (198, 41), (199, 41), (200, 41), (201, 41), (202, 41), (203, 41), (204, 41), (205, 41), (206, 41), (207, 41), (208, 41), (209, 41), (210, 41), (211, 41), (212, 41), (213, 41), (214, 41), (215, 41), (216, 41), (217, 41), (218, 41), (219, 41), (220, 41),
(121, 42), (122, 42), (123, 42), (124, 42), (125, 42), (126, 42), (127, 42), (128, 42), (129, 42), (130, 42), (131, 42), (132, 42), (133, 42), (134, 42), (135, 42), (136, 42), (137, 42), (138, 42), (139, 42), (140, 42),
(141, 43), (142, 43), (143, 43), (144, 43), (145, 43), (146, 43), (147, 43), (148, 43), (149, 43), (150, 43), (151, 43), (152, 43), (153, 43), (154, 43), (155, 43), (156, 43), (157, 43), (158, 43), (159, 43), (160, 43),
(161, 44), (162, 44), (163, 44), (164, 44), (165, 44), (166, 44), (167, 44), (168, 44), (169, 44), (170, 44), (171, 44), (172, 44), (173, 44), (174, 44), (175, 44), (176, 44), (177, 44), (178, 44), (179, 44), (180, 44),
(181, 45), (182, 45), (183, 45), (184, 45), (185, 45), (186, 45), (187, 45), (188, 45), (189, 45), (190, 45), (191, 45), (192, 45), (193, 45), (194, 45), (195, 45), (196, 45), (197, 45), (198, 45), (199, 45), (200, 45),
(200, 46), (201, 46), (202, 46), (203, 46), (204, 46), (205, 46), (206, 46), (207, 46), (208, 46), (209, 46), (210, 46), (211, 46), (212, 46), (213, 46), (214, 46), (215, 46), (216, 46), (217, 46), (218, 46), (219, 46), (220, 46),
(121, 47), (122, 47), (123, 47), (124, 47), (125, 47), (126, 47), (127, 47), (128, 47), (129, 47), (130, 47), (131, 47), (132, 47), (133, 47), (134, 47), (135, 47), (136, 47), (137, 47), (138, 47), (139, 47), (140, 47), (141, 47), (142, 47), (143, 47), (144, 47), (145, 47), (146, 47), (147, 47), (148, 47), (149, 47), (150, 47), (151, 47), (152, 47), (153, 47), (154, 47), (155, 47), (156, 47), (157, 47), (158, 47), (159, 47), (160, 47), (161, 47), (162, 47), (163, 47), (164, 47), (165, 47), (166, 47), (167, 47), (168, 47), (169, 47), (170, 47), (171, 47), (172, 47), (173, 47), (174, 47), (175, 47), (176, 47), (177, 47), (178, 47), (179, 47), (180, 47),
(121, 48), (122, 48), (123, 48), (124, 48), (125, 48), (126, 48), (127, 48), (128, 48), (129, 48), (130, 48), (131, 48), (132, 48), (133, 48), (134, 48), (135, 48), (136, 48), (137, 48), (138, 48), (139, 48), (140, 48), (141, 48), (142, 48), (143, 48), (144, 48), (145, 48), (146, 48), (147, 48), (148, 48), (149, 48), (150, 48),
(151, 49), (152, 49), (153, 49), (154, 49), (155, 49), (156, 49), (157, 49), (158, 49), (159, 49), (160, 49), (161, 49), (162, 49), (163, 49), (164, 49), (165, 49), (166, 49), (167, 49), (168, 49), (169, 49), (170, 49), (171, 49), (172, 49), (173, 49), (174, 49), (175, 49), (176, 49), (177, 49), (178, 49), (179, 49), (180, 49),
(121, 50), (122, 50), (123, 50), (124, 50), (125, 50), (126, 50), (127, 50), (128, 50), (129, 50), (130, 50), (131, 50), (132, 50), (133, 50), (134, 50), (135, 50), (136, 50), (137, 50), (138, 50), (139, 50), (140, 50), (141, 50), (142, 50), (143, 50), (144, 50), (145, 50), (146, 50), (147, 50), (148, 50), (149, 50), (150, 50), (151, 50), (152, 50), (153, 50), (154, 50), (155, 50), (156, 50), (157, 50), (158, 50), (159, 50), (160, 50), (161, 50), (162, 50), (163, 50), (164, 50), (165, 50), (166, 50), (167, 50), (168, 50), (169, 50), (170, 50), (171, 50), (172, 50), (173, 50), (174, 50), (175, 50), (176, 50), (177, 50), (178, 50), (179, 50), (180, 50),
(121, 51), (122, 51), (123, 51), (124, 51), (125, 51), (126, 51), (127, 51), (128, 51), (129, 51), (130, 51), (131, 51), (132, 51), (133, 51), (134, 51), (135, 51), (136, 51), (137, 51), (138, 51), (139, 51), (140, 51),
(141, 52), (142, 52), (143, 52), (144, 52), (145, 52), (146, 52), (147, 52), (148, 52), (149, 52), (150, 52), (151, 52), (152, 52), (153, 52), (154, 52), (155, 52), (156, 52), (157, 52), (158, 52), (159, 52), (160, 52),
(161, 53), (162, 53), (163, 53), (164, 53), (165, 53), (166, 53), (167, 53), (168, 53), (169, 53), (170, 53), (171, 53), (172, 53), (173, 53), (174, 53), (175, 53), (176, 53), (177, 53), (178, 53), (179, 53), (180, 53),
(181, 54), (182, 54), (183, 54), (184, 54), (185, 54), (186, 54), (187, 54), (188, 54), (189, 54), (190, 54), (191, 54), (192, 54), (193, 54), (194, 54), (195, 54), (196, 54), (197, 54), (198, 54), (199, 54), (200, 54), (201, 54), (202, 54), (203, 54), (204, 54), (205, 54), (206, 54), (207, 54), (208, 54), (209, 54), (210, 54), (211, 54), (212, 54), (213, 54), (214, 54), (215, 54), (216, 54), (217, 54), (218, 54), (219, 54), (220, 54),
(181, 55), (182, 55), (183, 55), (184, 55), (185, 55), (186, 55), (187, 55), (188, 55), (189, 55), (190, 55), (191, 55), (192, 55), (193, 55), (194, 55), (195, 55), (196, 55), (197, 55), (198, 55), (199, 55), (200, 55),
(201, 56), (202, 56), (203, 56), (204, 56), (205, 56), (206, 56), (207, 56), (208, 56), (209, 56), (210, 56), (211, 56), (212, 56), (213, 56), (214, 56), (215, 56), (216, 56), (217, 56), (218, 56), (219, 56), (220, 56),
(161, 57), (162, 57), (163, 57), (164, 57), (165, 57), (166, 57), (167, 57), (168, 57), (169, 57), (170, 57), (171, 57), (172, 57), (173, 57), (174, 57), (175, 57), (176, 57), (177, 57), (178, 57), (179, 57), (180, 57), (181, 57), (182, 57), (183, 57), (184, 57), (185, 57), (186, 57), (187, 57), (188, 57), (189, 57), (190, 57), (191, 57), (192, 57), (193, 57), (194, 57), (195, 57), (196, 57), (197, 57), (198, 57), (199, 57), (200, 57), (201, 57), (202, 57), (203, 57), (204, 57), (205, 57), (206, 57), (207, 57), (208, 57), (209, 57), (210, 57), (211, 57), (212, 57), (213, 57), (214, 57), (215, 57), (216, 57), (217, 57), (218, 57), (219, 57), (220, 57),
(161, 58), (162, 58), (163, 58), (164, 58), (165, 58), (166, 58), (167, 58), (168, 58), (169, 58), (170, 58), (171, 58), (172, 58), (173, 58), (174, 58), (175, 58), (176, 58), (177, 58), (178, 58), (179, 58), (180, 58),
(181, 59), (182, 59), (183, 59), (184, 59), (185, 59), (186, 59), (187, 59), (188, 59), (189, 59), (190, 59), (191, 59), (192, 59), (193, 59), (194, 59), (195, 59), (196, 59), (197, 59), (198, 59), (199, 59), (200, 59), (201, 59), (202, 59), (203, 59), (204, 59), (205, 59), (206, 59), (207, 59), (208, 59), (209, 59), (210, 59), (211, 59), (212, 59), (213, 59), (214, 59), (215, 59), (216, 59), (217, 59), (218, 59), (219, 59), (220, 59);