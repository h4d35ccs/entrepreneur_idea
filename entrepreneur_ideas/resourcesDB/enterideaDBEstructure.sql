-- phpMyAdmin SQL Dump
-- version 3.5.7
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generaci—n: 19-11-2013 a las 12:34:35
-- Versi—n del servidor: 5.5.29
-- Versi—n de PHP: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


--
-- Base de datos: `enteridea`
--
DROP DATABASE IF EXISTS `enteridea`;
CREATE DATABASE `enteridea` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `enteridea`;
--
-- Base de datos: `enteridea`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Comment`
--

DROP TABLE IF EXISTS `Comment`;
CREATE TABLE `Comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `IDEA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FK_6vo7ql7d90t42g411neag5vvm` (`USER_ID`),
  KEY `FK_m072psw53630bo3ofjcme8371` (`IDEA_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Idea`
--

DROP TABLE IF EXISTS `Idea`;
CREATE TABLE `Idea` (
  `idea_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation_date` timestamp NOT NULL,
  `modification_date` timestamp NULL DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `USER_ID` bigint(20) NOT NULL,
  `TOPIC_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`idea_id`),
  KEY `FK_9qy4xiwsnfir4cvx0849tevxm` (`USER_ID`),
  KEY `FK_4c1u1qgd821fwj0cytg4ocuf3` (`TOPIC_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;



-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Topic`
--

DROP TABLE IF EXISTS `Topic`;
CREATE TABLE `Topic` (
  `topic_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `topicDescription` varchar(255) NOT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `moderator` tinyint(1) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

--
-- Estructura de tabla para la tabla `votes`
--

DROP TABLE IF EXISTS `votes`;
CREATE TABLE `votes` (
  `vote_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idea_id` bigint(20) NOT NULL,
  `vote_type` enum('positive','negative','neutral') NOT NULL,
  PRIMARY KEY (`vote_id`),
  KEY `fk_idea_vote` (`idea_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `votes`
--
ALTER TABLE `votes`
  ADD CONSTRAINT `fk_idea_vote` FOREIGN KEY (`idea_id`) REFERENCES `Idea` (`idea_id`);
--
--

--
-- Filtros para la tabla `Comment`
--
ALTER TABLE `Comment`
  ADD CONSTRAINT `FK_6vo7ql7d90t42g411neag5vvm` FOREIGN KEY (`USER_ID`) REFERENCES `User` (`user_id`),
  ADD CONSTRAINT `FK_m072psw53630bo3ofjcme8371` FOREIGN KEY (`IDEA_ID`) REFERENCES `Idea` (`idea_id`);

--
-- Filtros para la tabla `Idea`
--
ALTER TABLE `Idea`
  ADD CONSTRAINT `FK_4c1u1qgd821fwj0cytg4ocuf3` FOREIGN KEY (`TOPIC_ID`) REFERENCES `Topic` (`topic_id`),
  ADD CONSTRAINT `FK_9qy4xiwsnfir4cvx0849tevxm` FOREIGN KEY (`USER_ID`) REFERENCES `User` (`user_id`);
