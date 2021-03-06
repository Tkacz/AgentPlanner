use schedule;

DELETE FROM `Equipment`;
DELETE FROM `RoomEquip`;
DELETE FROM `Rooms`;

INSERT INTO `Equipment` (`equip_id`, `symbol`, `name`) VALUES
(1, 'kompy', 'komputery'),
(2, 'rzutnik', 'rzutnik'),
(3, 'tabInt', 'tablica interaktywna'),
(4, 'xxx', 'xxx');

INSERT INTO `RoomEquip` (`roomEquip_id`, `room_id`, `equip_id`) VALUES
(1, 1, 2), # rzutnik dla sali 1
(2, 1, 3), # tabl. interaktywna dla sali 1
(3, 2, 2), # rzutnik dla sali 2
(4, 3, 1), # komutery dla sali 3
(5, 4, 1),
(6, 4, 2),
(7, 4, 3),
(8, 4, 4),
(9, 5, 1),
(10, 5, 2);

INSERT INTO `Rooms` (`room_id`, `no`, `capacity`) VALUES
(1, 1, 50), # sala wykładowa, ma rzutnik i tablicę interaktywną
(2, 2, 25), # zwykła sala, ma rzutnik 
(3, 3, 18), # sala laboratoryjna, ma komputery
(4, 4, 18), # zwykła sala ćwiczeniowa, nic nie ma
(5, 5, 20);
