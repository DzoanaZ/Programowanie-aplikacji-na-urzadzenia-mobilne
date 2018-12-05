-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 05 Gru 2018, 23:15
-- Wersja serwera: 10.1.36-MariaDB
-- Wersja PHP: 5.6.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `phpandroid`
--
CREATE DATABASE IF NOT EXISTS `phpandroid` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `phpandroid`;

-- --------------------------------------------------------

--
-- Zastąpiona struktura widoku `all_orders`
-- (Zobacz poniżej rzeczywisty widok)
--
DROP VIEW IF EXISTS `all_orders`;
CREATE TABLE `all_orders` (
`order_id` int(8)
,`user_id` int(6)
,`user_name` varchar(50)
,`user_email` varchar(50)
,`car_id` int(6)
,`car_name` varchar(50)
,`car_price` decimal(7,2)
,`start_date` datetime
,`end_date` datetime
);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `cars`
--

DROP TABLE IF EXISTS `cars`;
CREATE TABLE `cars` (
  `car_id` int(6) NOT NULL,
  `car_name` varchar(50) NOT NULL,
  `car_price` decimal(7,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Tabela Truncate przed wstawieniem `cars`
--

TRUNCATE TABLE `cars`;
--
-- Zrzut danych tabeli `cars`
--

INSERT INTO `cars` (`car_id`, `car_name`, `car_price`) VALUES
(1, 'Opel Corsa C 2003', '115.99'),
(2, 'Audi A4 B6 2004', '150.50'),
(3, 'Renault Megane III 2010', '160.00');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` int(8) NOT NULL,
  `user_id` int(6) NOT NULL,
  `car_id` int(6) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Tabela Truncate przed wstawieniem `orders`
--

TRUNCATE TABLE `orders`;
--
-- Zrzut danych tabeli `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `car_id`, `start_date`, `end_date`) VALUES
(1, 2, 1, '2018-12-18 13:17:17', '2018-12-24 15:00:00'),
(2, 5, 3, '2018-12-06 13:00:00', '2018-12-12 15:00:00');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Tabela Truncate przed wstawieniem `users`
--

TRUNCATE TABLE `users`;
--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_email`, `user_password`) VALUES
(2, 'pskocz', 'skoczp@gmail.com', 'd8578edf8458ce06fbc5bb76a58c5ca4'),
(3, 'kowalskijan', 'jkowalski@o2.pl', 'd8578edf8458ce06fbc5bb76a58c5ca4'),
(4, 'test', 'test@test.pl', 'd8578edf8458ce06fbc5bb76a58c5ca4'),
(5, 'nowak@nowak.com', 'nowak@nowak.com', 'd8578edf8458ce06fbc5bb76a58c5ca4');

-- --------------------------------------------------------

--
-- Struktura widoku `all_orders`
--
DROP TABLE IF EXISTS `all_orders`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `all_orders`  AS  select `orders`.`order_id` AS `order_id`,`orders`.`user_id` AS `user_id`,`users`.`user_name` AS `user_name`,`users`.`user_email` AS `user_email`,`cars`.`car_id` AS `car_id`,`cars`.`car_name` AS `car_name`,`cars`.`car_price` AS `car_price`,`orders`.`start_date` AS `start_date`,`orders`.`end_date` AS `end_date` from ((`orders` join `users` on((`users`.`user_id` = `orders`.`user_id`))) join `cars` on((`cars`.`car_id` = `orders`.`car_id`))) ;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`car_id`);

--
-- Indeksy dla tabeli `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `orders_users` (`user_id`),
  ADD KEY `orders_cars` (`car_id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_name` (`user_name`),
  ADD UNIQUE KEY `user_email` (`user_email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `cars`
--
ALTER TABLE `cars`
  MODIFY `car_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_cars` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `orders_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
