CREATE TABLE employe
(
    id_employe BIGINT       NOT NULL,
    nom        VARCHAR(255) NULL,
    prenom     VARCHAR(255) NULL,
    `role`     VARCHAR(255) NULL,
    CONSTRAINT pk_employe PRIMARY KEY (id_employe)
);