-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 05 Gru 2018, 00:42
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
-- Struktura tabeli dla tabeli `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_password` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`car_id`);

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
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
