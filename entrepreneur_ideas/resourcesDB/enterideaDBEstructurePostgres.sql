-- phpMyAdmin SQL Dump
-- version 3.5.7
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generaci�n: 19-11-2013 a las 12:34:35
-- Versi�n del servidor: 5.5.29
-- Versi�n de PHP: 5.4.10




--
-- Base de datos: enteridea
--
DROP DATABASE IF EXISTS enteridea;
CREATE DATABASE enteridea;
--
-- Base de datos: enteridea
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla Comment
--

DROP TABLE IF EXISTS Comment;
CREATE TABLE Comment (
  comment_id serial PRIMARY KEY ,
  comment varchar(255) NOT NULL,
  USER_ID bigint NOT NULL,
  IDEA_ID bigint NOT NULL
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla Idea
--

DROP TABLE IF EXISTS Idea;
CREATE TABLE Idea (
  idea_id SERIAL PRIMARY KEY,
  creation_date timestamp NOT NULL,
  modification_date timestamp NULL DEFAULT NULL,
  title varchar(255) NOT NULL,
  description text,
  USER_ID bigint NOT NULL,
  TOPIC_ID bigint NOT NULL
);



-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla Topic
--

DROP TABLE IF EXISTS Topic;
CREATE TABLE Topic (
  topic_id SERIAL PRIMARY KEY,
  topicDescription varchar(255) NOT NULL
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla User
--

DROP TABLE IF EXISTS UserEnteridea;
create table UserEnteridea (
   user_id serial PRIMARY KEY,
  email varchar(255) DEFAULT NULL,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  moderator boolean NOT NULL,
  nickname varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL
 );
 

--
-- Estructura de tabla para la tabla votes
--
DROP TYPE IF EXISTS vote_type;
--CREATE TYPE vote_type AS ENUM('positive','negative','neutral');
DROP TABLE IF EXISTS votes;
CREATE TABLE votes (
  vote_id serial primary Key,
  idea_id bigint NOT NULL,
  vote_type varchar
);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla votes
--
ALTER TABLE votes
  ADD CONSTRAINT fk_idea_vote FOREIGN KEY (idea_id) REFERENCES Idea (idea_id);
--
--

--
-- Filtros para la tabla Comment
--
ALTER TABLE Comment
  ADD CONSTRAINT FK_6vo7ql7d90t42g411neag5vvm FOREIGN KEY (USER_ID) REFERENCES UserEnteridea (user_id),
  ADD CONSTRAINT FK_m072psw53630bo3ofjcme8371 FOREIGN KEY (IDEA_ID) REFERENCES Idea (idea_id);

--
-- Filtros para la tabla Idea
--
ALTER TABLE Idea
  ADD CONSTRAINT FK_4c1u1qgd821fwj0cytg4ocuf3 FOREIGN KEY (TOPIC_ID) REFERENCES Topic (topic_id),
  ADD CONSTRAINT FK_9qy4xiwsnfir4cvx0849tevxm FOREIGN KEY (USER_ID) REFERENCES UserEnteridea (user_id);
  
  --
-- Volcado de datos para la tabla 'UserEnteridea'
--

INSERT INTO UserEnteridea (user_id, email, first_name, last_name, moderator, nickname, `password`) VALUES
(1, 'shiro@apache.com', 'Security name', 'Security lastname', 0, 'shiro', '1234');
INSERT INTO `enteridea`.`Topic` (`topic_id`, `topicDescription`) VALUES (NULL, 'firstTopic');
