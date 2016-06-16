-- phpMyAdmin SQL Dump
-- version 4.6.1
-- http://www.phpmyadmin.net
--
-- Vært: mysql13.gigahost.dk
-- Genereringstid: 16. 06 2016 kl. 13:54:48
-- Serverversion: 5.6.25-1~dotdeb+7.1
-- PHP-version: 5.6.20-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ahma0942_dtu_livechat`
--

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `banned_words`
--

CREATE TABLE `banned_words` (
  `word_id` int(11) NOT NULL,
  `word` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `blocked_contact`
--

CREATE TABLE `blocked_contact` (
  `user_id` int(11) NOT NULL,
  `blocked_id` int(11) NOT NULL,
  `blocked_time` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `contacts`
--

CREATE TABLE `contacts` (
  `user_ID` int(11) NOT NULL,
  `contact_ID` int(11) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0',
  `friends_since` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `groups`
--

CREATE TABLE `groups` (
  `group_id` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `group_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_created` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `group_members`
--

CREATE TABLE `group_members` (
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `group_joined` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `group_messages`
--

CREATE TABLE `group_messages` (
  `group_message_id` int(11) NOT NULL,
  `group_message` varchar(900) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `user_ID` int(11) NOT NULL,
  `group_message_sent` int(10) NOT NULL,
  `group_message_deleted` int(1) NOT NULL DEFAULT '0',
  `group_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Stand-in-struktur for visning `hyhy`
-- (See below for the actual view)
--
CREATE TABLE `hyhy` (
`username` varchar(45)
,`contact_ID` int(11)
);

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `messages`
--

CREATE TABLE `messages` (
  `message_id` int(11) NOT NULL,
  `message` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_ID` int(11) NOT NULL,
  `message_sent` int(10) NOT NULL,
  `message_deleted` int(1) NOT NULL DEFAULT '0',
  `receiver_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Stand-in-struktur for visning `showAllFriends`
-- (See below for the actual view)
--
CREATE TABLE `showAllFriends` (
`user_ID` int(11)
,`username` varchar(45)
,`nickname` varchar(50)
);

-- --------------------------------------------------------

--
-- Stand-in-struktur for visning `showMessages`
-- (See below for the actual view)
--
CREATE TABLE `showMessages` (
`user_id` int(11)
,`message` varchar(900)
);

-- --------------------------------------------------------

--
-- Stand-in-struktur for visning `showOffline`
-- (See below for the actual view)
--
CREATE TABLE `showOffline` (
`user_ID` int(11)
,`username` varchar(45)
,`nickname` varchar(50)
);

-- --------------------------------------------------------

--
-- Stand-in-struktur for visning `showOnline`
-- (See below for the actual view)
--
CREATE TABLE `showOnline` (
`user_ID` int(11)
,`username` varchar(45)
,`nickname` varchar(50)
);

-- --------------------------------------------------------

--
-- Struktur-dump for tabellen `users`
--

CREATE TABLE `users` (
  `user_ID` int(11) NOT NULL,
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `age` int(3) NOT NULL DEFAULT '0',
  `last_on` int(10) NOT NULL,
  `user_created` int(10) NOT NULL,
  `user_deleted` int(1) NOT NULL DEFAULT '0',
  `online` tinyint(1) NOT NULL DEFAULT '0',
  `activated` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktur for visning `hyhy`
--
DROP TABLE IF EXISTS `hyhy`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ahma0942`@`%` SQL SECURITY DEFINER VIEW `hyhy`  AS  select `users`.`username` AS `username`,`contacts`.`contact_ID` AS `contact_ID` from (`users` join `contacts` on((`users`.`user_ID` = `contacts`.`user_ID`))) ;

-- --------------------------------------------------------

--
-- Struktur for visning `showAllFriends`
--
DROP TABLE IF EXISTS `showAllFriends`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ahma0942`@`%` SQL SECURITY DEFINER VIEW `showAllFriends`  AS  select `users`.`user_ID` AS `user_ID`,`users`.`username` AS `username`,`users`.`nickname` AS `nickname` from `users` where (`users`.`user_ID` in (select `contacts`.`user_ID` from `contacts` where ((`contacts`.`contact_ID` = '1') and (`contacts`.`status` = 1))) or `users`.`user_ID` in (select `contacts`.`contact_ID` from `contacts` where ((`contacts`.`user_ID` = '1') and (`contacts`.`status` = 1)))) ;

-- --------------------------------------------------------

--
-- Struktur for visning `showMessages`
--
DROP TABLE IF EXISTS `showMessages`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ahma0942`@`%` SQL SECURITY DEFINER VIEW `showMessages`  AS  select `users`.`user_ID` AS `user_id`,`messages`.`message` AS `message` from (`users` join `messages` on((`users`.`user_ID` = `messages`.`user_ID`))) order by `users`.`user_ID` ;

-- --------------------------------------------------------

--
-- Struktur for visning `showOffline`
--
DROP TABLE IF EXISTS `showOffline`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ahma0942`@`%` SQL SECURITY DEFINER VIEW `showOffline`  AS  select `users`.`user_ID` AS `user_ID`,`users`.`username` AS `username`,`users`.`nickname` AS `nickname` from `users` where ((`users`.`user_ID` in (select `contacts`.`user_ID` from `contacts` where ((`contacts`.`contact_ID` = '1') and (`contacts`.`status` = 1))) or `users`.`user_ID` in (select `contacts`.`contact_ID` from `contacts` where ((`contacts`.`user_ID` = '1') and (`contacts`.`status` = 1)))) and (`users`.`online` = 0)) ;

-- --------------------------------------------------------

--
-- Struktur for visning `showOnline`
--
DROP TABLE IF EXISTS `showOnline`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ahma0942`@`%` SQL SECURITY DEFINER VIEW `showOnline`  AS  select `users`.`user_ID` AS `user_ID`,`users`.`username` AS `username`,`users`.`nickname` AS `nickname` from `users` where ((`users`.`user_ID` in (select `contacts`.`user_ID` from `contacts` where ((`contacts`.`contact_ID` = '1') and (`contacts`.`status` = 1))) or `users`.`user_ID` in (select `contacts`.`contact_ID` from `contacts` where ((`contacts`.`user_ID` = '1') and (`contacts`.`status` = 1)))) and (`users`.`online` = 1)) ;

--
-- Begrænsninger for dumpede tabeller
--

--
-- Indeks for tabel `banned_words`
--
ALTER TABLE `banned_words`
  ADD PRIMARY KEY (`word_id`),
  ADD UNIQUE KEY `word` (`word`);
ALTER TABLE `banned_words` ADD FULLTEXT KEY `word_2` (`word`);

--
-- Indeks for tabel `blocked_contact`
--
ALTER TABLE `blocked_contact`
  ADD KEY `blocked_contact_ibfk_1` (`user_id`),
  ADD KEY `blocked_contact_ibfk_2` (`blocked_id`);

--
-- Indeks for tabel `contacts`
--
ALTER TABLE `contacts`
  ADD UNIQUE KEY `user_ID` (`user_ID`,`contact_ID`),
  ADD KEY `contact_ID` (`contact_ID`);

--
-- Indeks for tabel `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`group_id`),
  ADD KEY `samilesmaersej` (`owner_id`);

--
-- Indeks for tabel `group_members`
--
ALTER TABLE `group_members`
  ADD UNIQUE KEY `group_id` (`group_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks for tabel `group_messages`
--
ALTER TABLE `group_messages`
  ADD PRIMARY KEY (`group_message_id`),
  ADD KEY `user_ID` (`user_ID`),
  ADD KEY `group_id` (`group_id`);

--
-- Indeks for tabel `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `messages_ibfk_2` (`receiver_id`),
  ADD KEY `messages_ibfk_1_idx` (`user_ID`);

--
-- Indeks for tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_ID`),
  ADD UNIQUE KEY `username` (`username`,`email`);

--
-- Brug ikke AUTO_INCREMENT for slettede tabeller
--

--
-- Tilføj AUTO_INCREMENT i tabel `banned_words`
--
ALTER TABLE `banned_words`
  MODIFY `word_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- Tilføj AUTO_INCREMENT i tabel `groups`
--
ALTER TABLE `groups`
  MODIFY `group_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- Tilføj AUTO_INCREMENT i tabel `group_messages`
--
ALTER TABLE `group_messages`
  MODIFY `group_message_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;
--
-- Tilføj AUTO_INCREMENT i tabel `messages`
--
ALTER TABLE `messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=116;
--
-- Tilføj AUTO_INCREMENT i tabel `users`
--
ALTER TABLE `users`
  MODIFY `user_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
--
-- Begrænsninger for dumpede tabeller
--

--
-- Begrænsninger for tabel `blocked_contact`
--
ALTER TABLE `blocked_contact`
  ADD CONSTRAINT `blocked_contact_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `blocked_contact_ibfk_2` FOREIGN KEY (`blocked_id`) REFERENCES `users` (`user_ID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Begrænsninger for tabel `contacts`
--
ALTER TABLE `contacts`
  ADD CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `users` (`user_ID`),
  ADD CONSTRAINT `contacts_ibfk_2` FOREIGN KEY (`contact_ID`) REFERENCES `users` (`user_ID`);

--
-- Begrænsninger for tabel `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `samilesmaersej` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_ID`);

--
-- Begrænsninger for tabel `group_members`
--
ALTER TABLE `group_members`
  ADD CONSTRAINT `group_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_ID`),
  ADD CONSTRAINT `group_members_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`),
  ADD CONSTRAINT `group_members_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_ID`);

--
-- Begrænsninger for tabel `group_messages`
--
ALTER TABLE `group_messages`
  ADD CONSTRAINT `group_messages_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `users` (`user_ID`),
  ADD CONSTRAINT `group_messages_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`);

--
-- Begrænsninger for tabel `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`user_ID`) REFERENCES `users` (`user_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
