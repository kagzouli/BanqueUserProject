DROP TABLE IF EXISTS OPERATION_USER_TABLE;
CREATE TABLE OPERATION_USER_TABLE(

    operationType char(4) not null,
    identifierUser varchar(32) not null,
    labelOperation varchar(64) not null,
    operationDate timestamp not null,
    amount decimal not null
);

DELETE FROM OPERATION_USER_TABLE;
INSERT INTO OPERATION_USER_TABLE(operationType,identifierUser,labelOperation,operationDate,amount) values ('CRED','ROGFED' ,'Credit1','2016-12-29 14:32:00.12' , 400);
INSERT INTO OPERATION_USER_TABLE(operationType,identifierUser,labelOperation,operationDate,amount) values ('DEBI','ROGFED' ,'Debit1','2016-12-29 19:45:00.29' , 150);
INSERT INTO OPERATION_USER_TABLE(operationType,identifierUser,labelOperation,operationDate,amount) values ('CRED','ROGFED' ,'Credit2','2016-12-29 21:20:15.21' , 250);
INSERT INTO OPERATION_USER_TABLE(operationType,identifierUser,labelOperation,operationDate,amount) values ('DEBI','ROGFED' ,'Debit2','2016-12-30 12:11:13.11' , 200);


