DROP TABLE IF EXISTS ROLE_TABLE;
CREATE TABLE ROLE_TABLE (
	roleId int primary key auto_increment not null,
	roleCode varchar(32) not null ,
);

insert into ROLE_TABLE(roleCode) VALUES ('role1');
insert into ROLE_TABLE(roleCode) VALUES ('role2');
insert into ROLE_TABLE(roleCode) VALUES ('role3');
insert into ROLE_TABLE(roleCode) VALUES ('role4');



DROP TABLE IF EXISTS USER_TABLE;
CREATE TABLE USER_TABLE (
	userId integer primary key auto_increment not null,
	userIdentifier varchar(32) not null ,
	firstName varchar(64) not null ,
	lastName varchar(64) not null ,
	locale varchar(3) not null
);


DELETE FROM USER_TABLE;
INSERT INTO USER_TABLE(userIdentifier,firstName,lastName,locale) VALUES ('ROGFED', 'Roger','Federer','FR');
INSERT INTO USER_TABLE(userIdentifier,firstName,lastName,locale) VALUES ('ANDMUR', 'Andy','Murray','EN');
INSERT INTO USER_TABLE(userIdentifier,firstName,lastName,locale) VALUES ('RAFNAD' , 'Rafael' , 'Nadal' , 'EN');

DROP TABLE IF EXISTS USER_ROLE_TABLE;

CREATE TABLE USER_ROLE_TABLE(
   userId integer not null,
   roleId integer not null
);

INSERT INTO USER_ROLE_TABLE VALUES (1, 1);
INSERT INTO USER_ROLE_TABLE VALUES (1, 3);
INSERT INTO USER_ROLE_TABLE VALUES (1, 4);
INSERT INTO USER_ROLE_TABLE VALUES (2, 1);
INSERT INTO USER_ROLE_TABLE VALUES (2, 2);
INSERT INTO USER_ROLE_TABLE VALUES (2, 3);




