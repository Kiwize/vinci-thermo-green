drop table if exists Mesure;
drop table if exists Stadium;
drop table if exists AppUser;

CREATE TABLE AppUser(
   id_user INT AUTO_INCREMENT,
   Username VARCHAR(64) ,
   Password VARCHAR(64) ,
   CONSTRAINT PK_APPUSER PRIMARY KEY(id_user)
);

CREATE TABLE Stadium(
   ID_Stadium CHAR(5) ,
   nom_stade VARCHAR(64) ,
   id_user INT NOT NULL,
   CONSTRAINT PK_STADIUM PRIMARY KEY(ID_Stadium),
   CONSTRAINT FK_STADIUM_USER FOREIGN KEY(id_user) REFERENCES AppUser(id_user)
);

CREATE TABLE Mesure(
   ID_Mesure INT AUTO_INCREMENT,
   num_zone INT,
   Date_mesure DATETIME,
   Temp DECIMAL(4,2)  ,
   ID_Stadium CHAR(5)  NOT NULL,
   CONSTRAINT PK_MESURE PRIMARY KEY(ID_Mesure),
   CONSTRAINT FK_MESURE_STADIUM FOREIGN KEY(ID_Stadium) REFERENCES Stadium(ID_Stadium)
);


INSERT INTO AppUser (Username, Password)
VALUES
    ('Toto', '$2a$10$AQL40Y7ZI3YNypvKP9ASyOtF2w81lxD8ik18gC7bVbU6aGbyIhwwW'),
    ('user1', '$2a$10$TeXqHNyFbVrVun6MLIejOe1HjqQ7DPO2etd446pT4wRI//0kwIymS'),
    ('user2', '$2a$10$pNHicnvNiIGrU/Ukw4knk.pj06DVj9ct4M8vw.Ul84SJvW5Zatiru');


INSERT INTO Stadium (ID_Stadium, nom_stade, id_user)
VALUES
    ('STD01', 'Stade A', 1),
    ('STD02', 'Stade B', 1),
    ('STD03', 'Stade C', 2),
    ('STD04', 'Stade D', 3);

INSERT INTO Mesure (num_zone, Date_mesure, Temp, ID_Stadium)
VALUES
    (1, '2023-09-25 10:00:00', 55.5, 'STD01'),
    (2, '2023-09-25 11:00:00', 57.0, 'STD01'),
    (1, '2023-09-25 10:00:00', 56.5, 'STD01'),
    (2, '2023-09-25 11:00:00', 54.0, 'STD01'),
    (1, '2023-09-25 10:00:00', 59.5, 'STD01'),
    (2, '2023-09-25 11:00:00', 55.0, 'STD01'),

    (1, '2023-09-25 10:30:00', 54.5, 'STD02'),
    (2, '2023-09-25 11:30:00', 55.0, 'STD02'),
    (1, '2023-09-25 10:30:00', 55.5, 'STD02'),
    (2, '2023-09-25 11:30:00', 60.0, 'STD02'),
    (1, '2023-09-25 10:30:00', 58.5, 'STD02'),
    (2, '2023-09-25 11:30:00', 55.0, 'STD02'),

    (1, '2023-09-25 10:15:00', 54.5, 'STD03'),
    (2, '2023-09-25 11:15:00', 53.0, 'STD03'),
    (1, '2023-09-25 10:15:00', 52.5, 'STD03'),
    (2, '2023-09-25 11:15:00', 53.0, 'STD03'),
    (1, '2023-09-25 10:15:00', 56.5, 'STD03'),
    (2, '2023-09-25 11:15:00', 55.0, 'STD03'),

    (1, '2023-09-25 10:15:00', 54.5, 'STD04'),
    (2, '2023-09-25 11:15:00', 58.0, 'STD04'),
    (1, '2023-09-25 10:15:00', 60.5, 'STD04'),
    (2, '2023-09-25 11:15:00', 62.0, 'STD04'),
    (1, '2023-09-25 10:15:00', 60.5, 'STD04'),
    (2, '2023-09-25 11:15:00', 59.0, 'STD04');


