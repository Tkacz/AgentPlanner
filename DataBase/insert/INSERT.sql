use schedule;


INSERT INTO `Properties` (`prop_id`, `prop_key`, `val`) VALUES
(1, 'days', '5'),
(2, 'times', '6');


INSERT INTO `Equipment` (`equip_id`, `symbol`, `name`) VALUES
(1, 'kompy', 'komputery'),
(2, 'rzutnik', 'rzutnik'),
(3, 'tabInt', 'tablica interaktywna');


INSERT INTO `Groups` (`group_id`, `symbol`, `stud_no`) VALUES
(1, '3M', 45),
(2, '1I2', 12),
(3, '1I1', 10),
(4, '1I5', 11),
(5, '4I2', 15),
(6, '4Id', 18),
(7, '4Ib', 18),
(8, 'FakKart0', 36),
(9, 'FakKart1', 18),
(10, 'FakKart2', 18),
(11, '2M', 40),
(12, '2MNA1', 10),
(13, '2MNA2', 18),
(14, '2MNN', 10),
(15, 'SemZak4', 3), # 4: numer grupy seminaryjnej od teacher_id
(16, 'SemLic4', 8),
(17, 'SemZak5', 4), # 5: numer grupy seminaryjnej od teacher_id
(18, '1If', 16),
(19, '1Id', 18),
(20, '3IAP2', 18),
(21, '3IAP3', 18),
(22, '3IAP1', 18),
(23, '2I', 80),
(24, '2If', 18),
(25, 'Sem6', 16), # 6: numer grupy seminaryjnej od teacher_id
(26, 'FakWinPhoneProg0', 40),
(27, 'FakWinPhoneProg1', 20),
(28, 'FakWinPhoneProg2', 20);


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


INSERT INTO `SubEquip` (`subEquip_id`, `sub_id`, `equip_id`) VALUES
(1, 4, 1),
(2, 5, 2),
(3, 6, 1),
(4, 9, 2),
(5, 9, 3),
(6, 10, 2),
(7, 11, 1),
(8, 13, 1),
(9, 14, 2),
(10, 15, 1),
(11, 16, 2),
(12, 17, 2),
(13, 18, 1),
(14, 18, 2);


INSERT INTO `SubGroup` (`subGroup_id`, `sub_id`, `group_id`) VALUES
(1, 1, 1), #Fiz. wyk, M3
(2, 2, 1), #Fiz. cw, M3
(3, 3, 2), #JAng. lekt 1I2
(4, 3, 3), #JAng. lekt 1I1
(5, 3, 4), #JAng. lekt 1I5
(6, 3, 5), #JAng. lekt 4I2
(7, 4, 6), #ZaJezProg. lab 4Id
(8, 4, 7), #ZaJezProg. lab 4Ib
(9, 5, 8), #ObNaKartGraf. wyk FakKart0
(10, 6, 9), #ObNaKartGraf. wyk FakKart1
(11, 7, 10), #ObNaKartGraf. wyk FakKart2 (laby prowadzone przez T. Puźniakowskiego) brak powiązania TeachGroup !!!
(12, 9, 15), #SemZak SemZak4
(13, 10, 16), #SemLic SemLic4
(14, 9, 17), #SemZak SemZak5
(15, 11, 18), #WstDoProg 1If
(16, 11, 19), #WstDoProg 1Id
(17, 12, 4), #MatDyskr 1I5
(18, 13, 20), #TechJavEnt 3IAP2
(19, 13, 21), #TechJavEnt 3IAP3
(20, 13, 22), #TechJavEnt 3IAP1
(21, 14, 23), #ProgOb wyk 2I
(22, 15, 24), #ProgOb lab 2If
(23, 16, 25), #Sem Sem6
(24, 17, 26), #WinPhoneProg FakWinPhoneProg0
(25, 18, 27), #WinPhoneProg FakWinPhoneProg1
(26, 18, 28); #WinPhoneProg FakWinPhoneProg2


INSERT INTO `Subjects` (`sub_id`, `symbol`, `name`, `subTyp_id`) VALUES
(1, 'Fiz', 'Fizyka', 1), #wyk
(2, 'Fiz', 'Fizyka', 2), #cw
(3, 'JAng', 'Język angielski', 4), #lekt
(4, 'ZaJezProg', 'Zaawansowane języki programowania', 3), #lab
(5, 'ObNaKartGraf', 'Obliczenia na kartach graficznych', 5), #wyk
(6, 'ObNakartGraf', 'Obliczenie na kartach graficznych', 6), #lab
(7, 'AnalMat', 'Analiza Matematyczna', 1), #wyk
(8, 'AnalMat', 'Analiza Matematyczna', 2), #cw
(9, 'SemZak', 'Seminarium zakładowe', 7), #sem
(10, 'SemLic', 'Seminarium licencjackie', 7), #sem
(11, 'WstDoProg', 'Wstęp do programowania', 3), #lab
(12, 'MatDyskr', 'Matematyka dyskretna', 2), #cw
(13, 'TechJavEnt', 'Technologie Java Enterprise', 3), #lab
(14, 'ProgOb', 'Programowanie obiektowe', 1), #wyk
(15, 'ProgOb', 'Programowanie obiektowe', 3), #lab
(16, 'Sem', 'Seminarium', 7), #sem
(17, 'WinPhoneProg', 'Windows Phone  - programowanie', 5), #wyk
(18, 'WinPhoneProg', 'Windows Phone  - programowanie', 6); #lab


INSERT INTO `SubjectTypes` (`subTyp_id`, `type_symbol`, `type_name`, `sort_symbol`, `sort_name`, `priority`) VALUES
(1, 'stand', 'standard', 'wyk', 'wyklad', 10),
(2, 'stand', 'standard', 'cw', 'ćwiczenia', 9),
(3, 'stand', 'standard', 'lab', 'laboratorium', 9),
(4, 'lekt', 'lektorat', 'lekt', 'lektorat', 6),
(5, 'fak', 'fakultet', 'wyk', 'wyklad', 8),
(6, 'fak', 'fakultet', 'lab', 'wyklad', 7),
(7, 'sem', 'seminarium', 'sem', 'seminarium', 6);


INSERT INTO `Teachers` (`teacher_id`, `degrees`, `symbol`, `name`, `surname`, `daysPriority`, `timesPriority`) VALUES
(1, 'prof. dr hab.', 'ralicki', 'Robert', 'Alicki', '55555', '555555'),
(2, 'mgr', 'pandrzejewski', 'Piotr', 'Andrzejewski', '55555', '555555'),
(3, 'dr',  'parlukowicz','Piotr', 'Arłukowicz', '55555', '555555'),
(4, 'dr hab. prof. UG', 'aaugustynowicz', 'Antoni', 'Augustynowicz', '55555', '555555'),
(5, 'dr', 'aborzyszkowski', 'Andrzej', 'Borzyszkowski', '55555', '555555'),
(6, 'dr', 'tborzyszkowski', 'Tomasz', 'Borzyszkowski', '55555', '555555'),
(7, 'dr', 'wbzyl', 'Włodzimierz', 'Bzyl', '55555', '555555'),
(8, 'dr', 'jczarnowksa', 'Joanna', 'Czarnowska', '55555', '555555'),
(9, 'dr', 'pczekanowski', 'Piotr', 'Czekanowski', '55555', '555555'),
(10, 'dr', 'wczernous', 'Wojciech', 'Czernous', '55555', '555555'),
(11, 'dr', 'kczub', 'Krzysztof', 'Czub', '55555', '555555'),
(12, 'dr hab. prof. UG', 'tczlapinski', 'Tomasz', 'Człapiński', '55555', '555555'),
(13, 'dr', 'ademby', 'Agnieszka', 'Demby', '55555', '555555'),
(14, 'dr', 'pdudzinski', 'Piotr', 'Dudziński', '55555', '555555'),
(15, 'mgr', 'jdybizbanski', 'Janusz', 'Dybizbański', '55555', '555555'),
(16, 'dr', 'tdzido', 'Tomasz', 'Dzido', '55555', '555555'),
(17, 'mgr', 'mdziemanczuk', 'Maciej', 'Dziemiańczuk', '55555', '555555'),
(18, 'dr', 'rfidytek', 'Robert', 'Fidytek', '55555', '555555'),
(19, 'dr', 'rfilipow', 'Rafał', 'Filipów', '55555', '555555'),
(20, 'dr', 'hfurmanczyk', 'Hanna', 'Furmańczyk', '55555', '555555'),
(21, 'dr', 'mganzha', 'Maria', 'Ganzha', '55555', '555555'),
(22, 'prof. dr hab.', 'ggromadzki', 'Grzegorz', 'Gromadzki', '55555', '555555'),
(23, 'prof. zw. dr hab.', 'egrzegorek', 'Edward', 'Grzegorek', '55555', '555555'),
(24, 'dr inż.', 'jgulgowski', 'Jacek', 'Gulgowski', '55555', '555555'),
(25, 'dr', 'mhalenda', 'Marek', 'Hałenda', '55555', '555555'),
(26, 'dr', 'khorodecki', 'Karol', 'Horodecki', '55555', '555555'),
(27, 'mgr', 'ijanicka', 'Irena', 'Janicka', '55555', '555555'),
(28, 'dr', 'djaruszewska', 'Danuta', 'Jaruszewska-Walczak', '55555', '555555'),
(29, 'dr', 'jjastrzebski', 'Jan', 'Jastrzębski', '55555', '555555'),
(30, 'dr hab. prof. UG', 'jjedrzejowicz', 'Joanna', 'Jędrzejowicz', '55555', '555555'),
(31, 'dr hab. prof. UG', 'tkaminska', 'Teresa', 'Kamińska', '55555', '555555'),
(32, 'mgr', 'pkaminski', 'Paweł', 'Kamiński', '55555', '555555'),
(33, 'dr', 'pkarwasz', 'Piotr', 'Karwasz', '55555', '555555'),
(34, 'dr',  'jklimaszewska','Joanna', 'Klimaszewska', '55555', '555555'),
(35, 'mgr', 'pklinga', 'Paweł', 'Klinga', '55555', '555555'),
(36, 'mgr', 'jknitter', 'Jakub', 'Knitter', '55555', '555555'),
(37, 'dr', 'jkowalik', 'Janusz', 'Kowalik', '55555', '555555'),
(38, 'dr', 'ekozlowska', 'Ewa', 'Kozłowska-Walania', '55555', '555555'),
(39, 'dr', 'gkrzykowski', 'Grzegorz', 'Krzykowski', '55555', '555555'),
(40, 'mgr', 'ekrzywanska', 'Emilia', 'Krzywańska-Frankowska', '55555', '555555'),
(41, 'mgr', 'akubica', 'Alicja', 'Kubica', '55555', '555555'),
(42, 'mgr', 'mkwiatek', 'Małgorzata', 'Kwiatek', '55555', '555555'),
(43, 'dr hab. prof. UG', 'hleszczynski', 'Henryk', 'Leszczyński', '55555', '555555'),
(44, 'dr', 'rlutowski', 'Rafał', 'Lutowski', '55555', '555555'),
(45, 'mgr', 'gmadejski', 'Grzegorz', 'Madejski', '55555', '555555'),
(46, 'mgr', 'gmatusik', 'Grzegorz', 'Matusik', '55555', '555555'),
(47, 'dr', 'mmatusik', 'Milena', 'Matusik', '55555', '555555'),
(48, 'dr', 'mmroczkowski', 'Maciej', 'Mroczkowski', '55555', '555555'),
(49, 'dr',  'nmrozek','Nikodem', 'Mrożek', '55555', '555555'),
(50, 'mgr', 'emrozek', 'Elżbieta', 'Mrożek', '55555', '555555'),
(51, 'dr hab. prof. UG', 'jmrozek', 'Jarosław', 'Mrozek', '55555', '555555'),
(52, 'dr inż.', 'jmlodzianowski', 'Janusz', 'Młodzianowski', '55555', '555555'),
(53, 'prof. dr hab.', 'tnatkaniec', 'Tomasz', 'Natkaniec', '55555', '555555'),
(54, 'mgr', 'anenca', 'Anna', 'Nenca', '55555', '555555'),
(55, 'dr', 'jneumann', 'Jakub', 'Neumann', '55555', '555555'),
(56, 'mgr', 'kniklas', 'Katarzyna', 'Niklas', '55555', '555555'),
(57, 'dr', 'anowel', 'Aleksandra', 'Nowel', '55555', '555555'),
(58, 'dr', 'ppaczkowski', 'Paweł', 'Pączkowski', '55555', '555555'),
(59, 'dr', 'wpawlowski', 'Wiesław', 'Pawłowski', '55555', '555555'),
(60, 'dr', 'jprzybylski', 'Jacek', 'Przybylski', '55555', '555555'),
(61, 'dr', 'bputrycz', 'Bartosz', 'Putrycz', '55555', '555555'),
(62, 'mgr', 'tpuzniakowski', 'Tadeusz', 'Puźniakowski', '55555', '555555'),
(63, 'dr hab. prof. UG', 'jpykacz', 'Jarosław', 'Pykacz', '55555', '555555'),
(64, 'dr', 'wplotka', 'Witold', 'Płotka', '55555', '555555'),
(65, 'dr hab. prof. UG', 'cschwarzweller', 'Christoph', 'Schwarzweller', '55555', '555555'),
(66, 'dr inż.', 'jskurczynski', 'Jerzy', 'Skurczyński', '55555', '555555'),
(67, 'dr', 'ssokolowski', 'Stefan', 'Sokołowski', '55555', '555555'),
(68, 'mgr', 'pspica', 'Paweł', 'Śpica', '55555', '555555'),
(69, 'mgr', 'mstaniszewski', 'Marcin', 'Staniszewski', '55555', '555555'),
(70, 'dr', 'mstukow', 'Michal', 'Stukow', '55555', '555555'),
(71, 'mgr', 'aszabala', 'Andrzej', 'Szabała', '55555', '555555'),
(72, 'prof. dr hab.', 'zszafraniec', 'Zbigniew', 'Szafraniec', '55555', '555555'),
(73, 'prof. dr hab.', 'tszarek', 'Tomasz', 'Szarek', '55555', '555555'),
(74, 'prof. dr hab.', 'aszczepanski', 'Andrzej', 'Szczepański', '55555', '555555'),
(75, 'dr hab. prof. UG', 'aszepietowski', 'Andrzej', 'Szepietowski', '55555', '555555'),
(76, 'dr', 'bszepietowski', 'Błażej', 'Szepietowski', '55555', '555555'),
(77, 'dr', 'pszuca', 'Piotr', 'Szuca', '55555', '555555'),
(78, 'dr', 'ktopolski', 'Krzysztof', 'Topolski', '55555', '555555'),
(79, 'dr hab. prof. UG', 'jtopp', 'Jerzy', 'Topp', '55555', '555555'),
(80, 'dr', 'jwesolowska', 'Jolanta', 'Wesołowska', '55555', '555555'),
(81, 'prof. zw. dr hab. inż.', 'swierzchon', 'Sławomir', 'Wierzchoń', '55555', '555555'),
(82, 'mgr', 'hwojewodka', 'Hanna', 'Wojewódka', '55555', '555555'),
(83, 'dr', 'bwolnik', 'Barbara', 'Wolnik', '55555', '555555'),
(84, 'mgr', 'mwrzosek', 'Monika', 'Wrzosek', '55555', '555555'),
(85, 'mgr', 'mwyszomirska', 'Magdalena', 'Wyszomirska-Góra', '55555', '555555'),
(86, 'dr', 'pzarzycki', 'Piotr', 'Zarzycki', '55555', '555555'),
(87, 'dr hab. prof. UG', 'azastrow', 'Andreas', 'Zastrow', '55555', '555555'),
(88, 'mgr', 'izdunowska', 'Izabela', 'Zdunowska', '55555', '555555'),
(89, 'dr', 'mzemojtel', 'Magdalena', 'Żemojtel-Piotrowska', '55555', '555555'),
(90, 'mgr', 'mziemlanska', 'Maria', 'Ziemlańska', '55555', '555555'),
(91, 'dr', 'pzwierkowski', 'Piotr', 'Zwierkowski', '55555', '555555'),
(92, 'dr', 'pzylinski', 'Paweł', 'Żyliński', '55555', '555555');


INSERT INTO `TeachGroup` (`teachGroup_id`, `teacher_id`, `group_id`) VALUES
(1, 1, 1), #ralicki, M3
(2, 2, 2), #pandrzejewski, 1I2
(3, 2, 3), #pandrzejewski, 1I1
(4, 2, 4), #pandrzejewski, 1I5
(5, 2, 5), #pandrzejewski, 4I2
(6, 3, 6), #parlukowicz, 4Id
(7, 3, 7), #parlukowicz, 4Ib
(8, 3, 8), #parlukowicz, FakKart0
(9, 3, 9), #parlukowicz, FakKart1
(10, 4, 11), #aaugustynowicz, 2M
(11, 4, 12), #aaugustynowicz, 2MNA1
(12, 4, 13), #aaugustynowicz, 2MNA2
(13, 4, 14), #aaugustynowicz, 2MNN
(14, 4, 15), #aaugustynowicz, SemZak4
(15, 4, 16), #aaugustynowicz, SemLic4
(16, 5, 17), #aborzyszkowski, SemZak5
(17, 5, 18), #aborzyszkowski, 1If
(18, 5, 4), #aborzyszkowski, 1I5
(19, 5, 19), #aborzyszkowski, 1Id
(20, 6, 20), #tborzyszkowski, 3IAP2
(21, 6, 21), #tborzyszkowski, 3IAP3
(22, 6, 22), #tborzyszkowski, 3IAP1
(23, 6, 23), #tborzyszkowski, 2I
(24, 6, 24), #tborzyszkowski, 2If
(25, 6, 25), #tborzyszkowski, Sem6
(26, 6, 26), #tborzyszkowski, FakWinPhoneProg0
(27, 6, 27), #tborzyszkowski, FakWinPhoneProg1
(28, 6, 28); #tborzyszkowski, FakWinPhoneProg2
