use schedule;

DROP TABLE IF EXISTS Teachers;
CREATE TABLE Teachers (
	teacher_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	degrees varchar(64) NOT NULL default '', #stopnie naukowe
	symbol varchar(32) NOT NULL UNIQUE, #symbol, musi być unikalny, brany jako nazwa agenta
	name varchar(32) NOT NULL default '', #imie
	surname varchar(64) NOT NULL default '', #nawisko
	daysPriority varchar(255) NOT NULL default '', #priorytet dni
	timesPriority TEXT(65535) NOT NULL default '', #priorytet czasowy wg powyzszych godzin
	maxHours int unsigned NOT NULL default 10, #max. ilosc godzin dziennie
	minHours int unsigned NOT NULL default 0, #min ilosc godzin dziennie
	maxContHours int unsigned NOT NULL default 10, #max ilosc godzin pod rzad
	maxDayGaps int unsigned NOT NULL default 0, #max ilosc okienek w ciagu dnia
	maxWeekGaps int unsigned NOT NULL default 0 #max ilosc okienek w ciagu tygodnia
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Absences;
CREATE TABLE Absences (
	abs_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	teacher_id int unsigned NOT NULL, #id wykladowcy
	date_from datetime NOT NULL, #data nieobecnosci od
	date_to datetime NOT NULL, #data nieobecnosci do
	comm varchar(255) NOT NULL default '' #komentarz
) ENGINE=InnoDB;

DROP TABLE IF EXISTS SubjectTypes;
CREATE TABLE SubjectTypes (
	subTyp_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	type_symbol varchar(16) NOT NULL, #stand, mon, fak, lek, ect
	type_name varchar(24) NOT NULL default '', #standard, monograf, fakultet, lektorat, etc.
	sort_symbol varchar(16) NOT NULL, #wyk, cw, lab, etc.
	sort_name varchar(24) NOT NULL default '', #wyklad, cwiczenia, laboratorium, etc.
	priority int unsigned NOT NULL default 0, #priorytet zajęć 0 najmniej
	comm varchar(255) NOT NULL default '' #komentarz
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Subjects;
CREATE TABLE Subjects (
	sub_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	symbol varchar(32) NOT NULL UNIQUE, # symbol, musi być unikalny, brany jako nazwa agenta
	name varchar(64) NOT NULL default '', #nazwa, może się powtarzać, lecz jest rozróżnialny poprzez typ
	subTyp_id int unsigned NOT NULL, #typ przedmiotu
	time int unsigned NOT NULL default 1 #czas trwania liczony w jednostkach planowych
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Equipment;
CREATE TABLE Equipment (
	equip_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	symbol varchar(32) NOT NULL UNIQUE, #symbol, musi być unikalny
	name varchar(64) NOT NULL default '', #nazwa
	type varchar(32) NOT NULL default '', #rodzaj, np. elektronika, pomoc naukowa, sprzęt lab., mebel
	comm varchar(255) NOT NULL default '' #komentarz
) ENGINE=InnoDB;

DROP TABLE IF EXISTS SubEquip;
CREATE TABLE SubEquip ( #relacja wiele do wielu dla tabel: Subjects i Equipment
	subEquip_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	sub_id int unsigned NOT NULL, #id z tabeli Subjects
	equip_id int unsigned NOT NULL #id z tabeli Equipment
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Rooms;
CREATE TABLE Rooms (
	room_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	no int unsigned NOT NULL UNIQUE, #numer sali, jest brany jako nazwa agenta
	capacity int unsigned NOT NULL default 0 #pojemność sali (na ile osób)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS RoomEquip;
CREATE TABLE RoomEquip ( #relacja wiele do wielu dla tabel: Rooms i Equipment
	roomEquip_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	room_id int unsigned NOT NULL, #id z tabeli Rooms
	equip_id int unsigned NOT NULL #id z tabeli Equipments
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
	group_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	symbol varchar(32) NOT NULL UNIQUE, #symbol, musi być unikalny
	sub_id int unsigned NOT NULL, #id przedmiotu, którego dana grupa jest częścią
	stud_no int unsigned NOT NULL default 0 #ilość studentów
) ENGINE=InnoDB;

DROP TABLE IF EXISTS TeachGroup;
CREATE TABLE TeachGroup ( #relacja wiele do wielu dla tabel: Teachers i Groups
	teachGroup_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	teacher_id int unsigned NOT NULL, #id z tabeli Teachers
	group_id int unsigned NOT NULL #id z tabeli Groups
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Students;
CREATE TABLE Students (
	stud_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	index_no varchar(16) NOT NULL UNIQUE, #nr indeksu
	pesel varchar(11) NOT NULL default '00000000000', #nr pesel (same zera jak nie ma)
	name varchar(32) NOT NULL default '', #imię
	surname varchar(64) NOT NULL default '', #nazwisko
	major varchar(32) NOT NULL default '', #kierunek studiów
	speciality varchar(32) NOT NULL default '', #specjalność
	year int unsigned NOT NULL default 1, #rok studiów
	semester int unsigned NOT NULL default 1 #numer semestru
) ENGINE=InnoDB;

DROP TABLE IF EXISTS StudGroup;
CREATE TABLE StudGroup ( #relacja wiele do wielu tla tabel: Students i Groups
	studGroup_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	stud_id int unsigned NOT NULL, #id z tabeli Students
	group_id int unsigned NOT NULL #id z tabeli Groups
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Plan;
CREATE TABLE Plan (
	plan_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	group_id int unsigned NOT NULL, #id z tabeli Groups
	day int unsigned NOT NULL,
	time int unsigned NOT NULL,
	room_no int unsigned NOT NULL
) ENGINE=InnoDB;

DROP TABLE IF EXISTS RejectGroups;
CREATE TABLE RejectGroups (
	reject_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	group_id int unsigned NOT NULL, #id z tabeli Groups
	teacher_id int unsigned NOT NULL #id z tabeli Teachers
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Properties;
CREATE TABLE Properties (
	prop_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prop_key varchar(32) NOT NULL UNIQUE, #klucz
	val varchar(256) NOT NULL, #wartość
	comm varchar(255) NOT NULL default '' #komentarz
) ENGINE=InnoDB;

# DROP TABLE IF EXISTS Buildings;
# CREATE TABLE Buildings();
