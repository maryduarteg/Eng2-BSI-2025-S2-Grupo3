-- Gerado por Oracle SQL Developer Data Modeler 24.3.1.351.0831
--   em:        2025-10-03 08:21:40 BRT
--   site:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE ALUNO 
    ( 
     alu_id               INTEGER  NOT NULL , 
     alu_dt_entrada       DATE , 
     alu_foto             VARCHAR2 (100) , 
     alu_mae              VARCHAR2 (75) , 
     alu_pai              VARCHAR2 (75) , 
     alu_responsavel_pais CHAR (1) , 
     alu_conhecimento     CHAR (1) , 
     alu_pais_convivem    CHAR (1) , 
     alu_pensao           CHAR (1) , 
     pes_id               INTEGER  NOT NULL 
    ) 
;

ALTER TABLE ALUNO 
    ADD CONSTRAINT ALUNO_PK PRIMARY KEY ( alu_id ) ;

CREATE TABLE CATEGORIA_USUARIO 
    ( 
     ctsr_id   INTEGER  NOT NULL , 
     ctsr_desc VARCHAR2 (20) 
    ) 
;

ALTER TABLE CATEGORIA_USUARIO 
    ADD CONSTRAINT CATEGORIA_USUARIO_PK PRIMARY KEY ( ctsr_id ) ;

CREATE TABLE CIDADE 
    ( 
     cid_id   INTEGER  NOT NULL , 
     cid_nome VARCHAR2 (75) , 
     est_id   INTEGER  NOT NULL 
    ) 
;

ALTER TABLE CIDADE 
    ADD CONSTRAINT CIDADE_PK PRIMARY KEY ( cid_id ) ;

CREATE TABLE DIAS 
    ( 
     dia_id        INTEGER  NOT NULL , 
     dia_descricao VARCHAR2 (20) 
    ) 
;

ALTER TABLE DIAS 
    ADD CONSTRAINT DIAS_PK PRIMARY KEY ( dia_id ) ;

CREATE TABLE DIAS_MARCADOS_OFICINAS 
    ( 
     dmf_id  INTEGER  NOT NULL , 
     dmf_dia DATE , 
     ofc_id  INTEGER  NOT NULL 
    ) 
;

ALTER TABLE DIAS_MARCADOS_OFICINAS 
    ADD CONSTRAINT DIAS_MARCADOS_OFICINAS_PK PRIMARY KEY ( dmf_id ) ;

CREATE TABLE ENDERECO 
    ( 
     end_id          INTEGER  NOT NULL , 
     end_logradouro  VARCHAR2 (150) , 
     end_numero      VARCHAR2 (20) , 
     end_cep         VARCHAR2 (8) , 
     end_complemento VARCHAR2 (50) , 
     end_bairro      VARCHAR2 (75) , 
     cid_id          INTEGER  NOT NULL 
    ) 
;

ALTER TABLE ENDERECO 
    ADD CONSTRAINT ENDERECO_PK PRIMARY KEY ( end_id ) ;

CREATE TABLE ESPECIALIDADES 
    ( 
     esp_id            INTEGER  NOT NULL , 
     esp_descricao     VARCHAR2 (100) , 
     PROFESSOR_prof_id INTEGER  NOT NULL 
    ) 
;

ALTER TABLE ESPECIALIDADES 
    ADD CONSTRAINT ESPECIALIDADES_PK PRIMARY KEY ( esp_id ) ;

CREATE TABLE ESTADO 
    ( 
     est_id   INTEGER  NOT NULL , 
     est_uf_1 VARCHAR2 (2)  NOT NULL , 
     est_nome VARCHAR2 (30) 
    ) 
;

ALTER TABLE ESTADO 
    ADD CONSTRAINT ESTADO_PK PRIMARY KEY ( est_id ) ;

CREATE TABLE OFICINA 
    ( 
     ofc_id           INTEGER  NOT NULL , 
     ofc_nome         VARCHAR2 (75) , 
     ofc_hora_inicio  DATE , 
     ofc_hora_termino DATE , 
     prof_id          INTEGER  NOT NULL 
    ) 
;

ALTER TABLE OFICINA 
    ADD CONSTRAINT OFICINA_PK PRIMARY KEY ( ofc_id ) ;

CREATE TABLE PASSEIO 
    ( 
     pas_id            INTEGER  NOT NULL , 
     pas_data          DATE , 
     pas_hora_inicio   DATE , 
     pas_hora_final    DATE , 
     pas_chamada_feita CHAR (1) , 
     pde_id            INTEGER  NOT NULL 
    ) 
;

ALTER TABLE PASSEIO 
    ADD CONSTRAINT PASSEIO_PK PRIMARY KEY ( pas_id ) ;

CREATE TABLE PASSEIO_DESCRICAO 
    ( 
     pde_id        INTEGER  NOT NULL , 
     pde_descricao VARCHAR2 (75) 
    ) 
;

ALTER TABLE PASSEIO_DESCRICAO 
    ADD CONSTRAINT PASSEIO_DESCRICAO_PK PRIMARY KEY ( pde_id ) ;

CREATE TABLE PESSOA 
    ( 
     pes_id            INTEGER  NOT NULL , 
     pes_nome          VARCHAR2 (75) , 
     pes_cpf           VARCHAR2 (11) , 
     pes_dt_nascimento DATE , 
     pes_rg            VARCHAR2 (9) , 
     end_id            INTEGER  NOT NULL 
    ) 
;

ALTER TABLE PESSOA 
    ADD CONSTRAINT PESSOA_PK PRIMARY KEY ( pes_id ) ;

CREATE TABLE PRESENCA_OFICINA 
    ( 
     pso_id INTEGER  NOT NULL , 
     alu_id INTEGER  NOT NULL , 
     ofc_id INTEGER  NOT NULL 
    ) 
;

ALTER TABLE PRESENCA_OFICINA 
    ADD CONSTRAINT PRESENCA_OFICINA_PK PRIMARY KEY ( alu_id, ofc_id, pso_id ) ;

CREATE TABLE PRESENCA_PASSEIO 
    ( 
     psp_id         INTEGER  NOT NULL , 
     ALUNO_alu_id   INTEGER  NOT NULL , 
     PASSEIO_pas_id INTEGER  NOT NULL , 
     pp_id          INTEGER 
    ) 
;

ALTER TABLE PRESENCA_PASSEIO 
    ADD CONSTRAINT PRESENCA_PASSEIO_PK PRIMARY KEY ( ALUNO_alu_id, PASSEIO_pas_id, psp_id ) ;

CREATE TABLE PROFESSOR 
    ( 
     prof_id        INTEGER  NOT NULL , 
     prof_matricula VARCHAR2 (10) , 
     pes_id         INTEGER  NOT NULL 
    ) 
;

ALTER TABLE PROFESSOR 
    ADD CONSTRAINT PROFESSOR_PK PRIMARY KEY ( prof_id ) ;

CREATE TABLE R_ALUNO_OFICINA 
    ( 
     alu_id INTEGER  NOT NULL , 
     ofc_id INTEGER  NOT NULL 
    ) 
;

ALTER TABLE R_ALUNO_OFICINA 
    ADD CONSTRAINT R_ALUNO_OFICINA_PK PRIMARY KEY ( alu_id, ofc_id ) ;

CREATE TABLE R_ALUNO_PASSEIO 
    ( 
     pas_id INTEGER  NOT NULL , 
     alu_id INTEGER  NOT NULL 
    ) 
;

ALTER TABLE R_ALUNO_PASSEIO 
    ADD CONSTRAINT R_ALUNO_PASSEIO_PK PRIMARY KEY ( pas_id, alu_id ) ;

CREATE TABLE R_DIAS_OFICINA 
    ( 
     ofc_id INTEGER  NOT NULL , 
     dia_id INTEGER  NOT NULL 
    ) 
;

ALTER TABLE R_DIAS_OFICINA 
    ADD CONSTRAINT R_DIAS_OFICINA_PK PRIMARY KEY ( ofc_id, dia_id ) ;

CREATE TABLE R_PROFESSOR_PASSEIO 
    ( 
     prof_id INTEGER  NOT NULL , 
     pas_id  INTEGER  NOT NULL 
    ) 
;

ALTER TABLE R_PROFESSOR_PASSEIO 
    ADD CONSTRAINT R_PROFESSOR_PASSEIO_PK PRIMARY KEY ( pas_id, prof_id ) ;

CREATE TABLE TELEFONE 
    ( 
     tel_id     INTEGER  NOT NULL , 
     tel_numero VARCHAR2 (11) , 
     pes_id     INTEGER  NOT NULL 
    ) 
;

ALTER TABLE TELEFONE 
    ADD CONSTRAINT TELEFONE_PK PRIMARY KEY ( tel_id ) ;

CREATE TABLE USUARIO 
    ( 
     usr_id    INTEGER  NOT NULL , 
     usr_login VARCHAR2 (40) , 
     usr_senha VARCHAR2 (40) , 
     ctsr_id   INTEGER  NOT NULL 
    ) 
;

ALTER TABLE USUARIO 
    ADD CONSTRAINT USUARIO_PK PRIMARY KEY ( usr_id ) ;

ALTER TABLE ALUNO 
    ADD CONSTRAINT ALUNO_PESSOA_FK FOREIGN KEY 
    ( 
     pes_id
    ) 
    REFERENCES PESSOA 
    ( 
     pes_id
    ) 
;

ALTER TABLE CIDADE 
    ADD CONSTRAINT CIDADE_ESTADO_FK FOREIGN KEY 
    ( 
     est_id
    ) 
    REFERENCES ESTADO 
    ( 
     est_id
    ) 
;

ALTER TABLE DIAS_MARCADOS_OFICINAS 
    ADD CONSTRAINT DIAS_MARC_OFC_FK FOREIGN KEY 
    ( 
     ofc_id
    ) 
    REFERENCES OFICINA 
    ( 
     ofc_id
    ) 
;

ALTER TABLE ENDERECO 
    ADD CONSTRAINT ENDERECO_CIDADE_FK FOREIGN KEY 
    ( 
     cid_id
    ) 
    REFERENCES CIDADE 
    ( 
     cid_id
    ) 
;

ALTER TABLE ESPECIALIDADES 
    ADD CONSTRAINT ESPECIALI_PROF_FK FOREIGN KEY 
    ( 
     PROFESSOR_prof_id
    ) 
    REFERENCES PROFESSOR 
    ( 
     prof_id
    ) 
;

ALTER TABLE OFICINA 
    ADD CONSTRAINT OFICINA_PROFESSOR_FK FOREIGN KEY 
    ( 
     prof_id
    ) 
    REFERENCES PROFESSOR 
    ( 
     prof_id
    ) 
;

ALTER TABLE PASSEIO 
    ADD CONSTRAINT PASSEIO_PASSEIO_DESCRICAO_FK FOREIGN KEY 
    ( 
     pde_id
    ) 
    REFERENCES PASSEIO_DESCRICAO 
    ( 
     pde_id
    ) 
;

ALTER TABLE PESSOA 
    ADD CONSTRAINT PESSOA_ENDERECO_FK FOREIGN KEY 
    ( 
     end_id
    ) 
    REFERENCES ENDERECO 
    ( 
     end_id
    ) 
;

ALTER TABLE PRESENCA_OFICINA 
    ADD CONSTRAINT PRESENCA_OFICINA_ALUNO_FK FOREIGN KEY 
    ( 
     alu_id
    ) 
    REFERENCES ALUNO 
    ( 
     alu_id
    ) 
;

ALTER TABLE PRESENCA_OFICINA 
    ADD CONSTRAINT PRESENCA_OFICINA_OFICINA_FK FOREIGN KEY 
    ( 
     ofc_id
    ) 
    REFERENCES OFICINA 
    ( 
     ofc_id
    ) 
;

ALTER TABLE PRESENCA_PASSEIO 
    ADD CONSTRAINT PRESENCA_PAS_ALUNO_FK FOREIGN KEY 
    ( 
     ALUNO_alu_id
    ) 
    REFERENCES ALUNO 
    ( 
     alu_id
    ) 
;

ALTER TABLE PRESENCA_PASSEIO 
    ADD CONSTRAINT PRESENCA_PAS_FK FOREIGN KEY 
    ( 
     PASSEIO_pas_id
    ) 
    REFERENCES PASSEIO 
    ( 
     pas_id
    ) 
;

ALTER TABLE PROFESSOR 
    ADD CONSTRAINT PROFESSOR_PESSOA_FK FOREIGN KEY 
    ( 
     pes_id
    ) 
    REFERENCES PESSOA 
    ( 
     pes_id
    ) 
;

ALTER TABLE R_ALUNO_OFICINA 
    ADD CONSTRAINT R_ALUNO_OFICINA_ALUNO_FK FOREIGN KEY 
    ( 
     alu_id
    ) 
    REFERENCES ALUNO 
    ( 
     alu_id
    ) 
;

ALTER TABLE R_ALUNO_OFICINA 
    ADD CONSTRAINT R_ALUNO_OFICINA_OFICINA_FK FOREIGN KEY 
    ( 
     ofc_id
    ) 
    REFERENCES OFICINA 
    ( 
     ofc_id
    ) 
;

ALTER TABLE R_ALUNO_PASSEIO 
    ADD CONSTRAINT R_ALUNO_PASSEIO_FK FOREIGN KEY 
    ( 
     pas_id
    ) 
    REFERENCES PASSEIO 
    ( 
     pas_id
    ) 
;

ALTER TABLE R_ALUNO_PASSEIO 
    ADD CONSTRAINT R_ALUNO_PASSEIO_FKv1 FOREIGN KEY 
    ( 
     alu_id
    ) 
    REFERENCES ALUNO 
    ( 
     alu_id
    ) 
;

ALTER TABLE R_DIAS_OFICINA 
    ADD CONSTRAINT R_DIAS_OFICINA_DIAS_FK FOREIGN KEY 
    ( 
     dia_id
    ) 
    REFERENCES DIAS 
    ( 
     dia_id
    ) 
;

ALTER TABLE R_DIAS_OFICINA 
    ADD CONSTRAINT R_DIAS_OFICINA_OFICINA_FK FOREIGN KEY 
    ( 
     ofc_id
    ) 
    REFERENCES OFICINA 
    ( 
     ofc_id
    ) 
;

ALTER TABLE R_PROFESSOR_PASSEIO 
    ADD CONSTRAINT R_PROFESSOR_PASSEIO_FK FOREIGN KEY 
    ( 
     pas_id
    ) 
    REFERENCES PASSEIO 
    ( 
     pas_id
    ) 
;

ALTER TABLE R_PROFESSOR_PASSEIO 
    ADD CONSTRAINT R_PROFESSOR_PASSEIO_FKv1 FOREIGN KEY 
    ( 
     prof_id
    ) 
    REFERENCES PROFESSOR 
    ( 
     prof_id
    ) 
;

ALTER TABLE TELEFONE 
    ADD CONSTRAINT TELEFONE_PESSOA_FK FOREIGN KEY 
    ( 
     pes_id
    ) 
    REFERENCES PESSOA 
    ( 
     pes_id
    ) 
;

ALTER TABLE USUARIO 
    ADD CONSTRAINT USUARIO_CATEGORIA_FK FOREIGN KEY 
    ( 
     ctsr_id
    ) 
    REFERENCES CATEGORIA_USUARIO 
    ( 
     ctsr_id
    ) 
;



-- Relatório do Resumo do Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            21
-- CREATE INDEX                             0
-- ALTER TABLE                             44
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
