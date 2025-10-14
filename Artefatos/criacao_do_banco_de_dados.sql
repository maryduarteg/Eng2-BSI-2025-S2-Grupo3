-- lembre de nomear o database para: bga-db para não termos problemas na hora de estabelecer a conexão

CREATE TABLE ALUNO
    (
     alu_id               INTEGER  NOT NULL ,
     alu_dt_entrada       DATE ,
     alu_foto             VARCHAR (100) , -- VARCHAR2 alterado para VARCHAR
     alu_mae              VARCHAR (75) ,
     alu_pai              VARCHAR (75) ,
     alu_responsavel_pais CHAR (1) ,
     alu_conhecimento     CHAR (1) ,
     alu_pais_convivem    CHAR (1) ,
     alu_pensao           CHAR (1) ,
     pes_id               INTEGER  NOT NULL
    )
;

ALTER TABLE ALUNO
    ADD CONSTRAINT ALUNO_PK PRIMARY KEY ( alu_id ) ;

--

CREATE TABLE CATEGORIA_USUARIO
    (
     ctsr_id  INTEGER  NOT NULL ,
     ctsr_desc VARCHAR (20)
    )
;

ALTER TABLE CATEGORIA_USUARIO
    ADD CONSTRAINT CATEGORIA_USUARIO_PK PRIMARY KEY ( ctsr_id ) ;

--

CREATE TABLE CIDADE
    (
     cid_id   INTEGER  NOT NULL ,
     cid_nome VARCHAR (75) ,
     est_id   INTEGER  NOT NULL
    )
;

ALTER TABLE CIDADE
    ADD CONSTRAINT CIDADE_PK PRIMARY KEY ( cid_id ) ;

--

CREATE TABLE DIAS
    (
     dia_id          INTEGER  NOT NULL ,
     dia_descricao VARCHAR (20)
    )
;

ALTER TABLE DIAS
    ADD CONSTRAINT DIAS_PK PRIMARY KEY ( dia_id ) ;

--

CREATE TABLE DIAS_MARCADOS_OFICINAS
    (
     dmf_id  INTEGER  NOT NULL ,
     dmf_dia DATE , -- Mantido como DATE, pois é uma data marcada
     ofc_id  INTEGER  NOT NULL
    )
;

ALTER TABLE DIAS_MARCADOS_OFICINAS
    ADD CONSTRAINT DIAS_MARCADOS_OFICINAS_PK PRIMARY KEY ( dmf_id ) ;

--

CREATE TABLE ENDERECO
    (
     end_id          INTEGER  NOT NULL ,
     end_logradouro  VARCHAR (150) ,
     end_numero      VARCHAR (20) ,
     end_cep         VARCHAR (8) ,
     end_complemento VARCHAR (50) ,
     end_bairro      VARCHAR (75) ,
     cid_id          INTEGER  NOT NULL
    )
;

ALTER TABLE ENDERECO
    ADD CONSTRAINT ENDERECO_PK PRIMARY KEY ( end_id ) ;

--

CREATE TABLE ESPECIALIDADES
    (
     esp_id              INTEGER  NOT NULL ,
     esp_descricao       VARCHAR (100) ,
     PROFESSOR_prof_id INTEGER  NOT NULL
    )
;

ALTER TABLE ESPECIALIDADES
    ADD CONSTRAINT ESPECIALIDADES_PK PRIMARY KEY ( esp_id ) ;

--

CREATE TABLE ESTADO
    (
     est_id   INTEGER  NOT NULL ,
     est_uf_1 VARCHAR (2)  NOT NULL ,
     est_nome VARCHAR (30)
    )
;

ALTER TABLE ESTADO
    ADD CONSTRAINT ESTADO_PK PRIMARY KEY ( est_id ) ;

--

CREATE TABLE OFICINA
    (
     ofc_id           INTEGER  NOT NULL ,
     ofc_nome         VARCHAR (75) ,
     ofc_hora_inicio  TIME , -- Alterado de DATE para TIME
     ofc_hora_termino TIME , -- Alterado de DATE para TIME
     ofc_dt_inicial   DATE ,
     ofc_dt_final     DATE ,
     prof_id          INTEGER  NOT NULL ,
     ofc_ativo        CHAR (1)
    )
;

ALTER TABLE OFICINA
    ADD CONSTRAINT OFICINA_PK PRIMARY KEY ( ofc_id ) ;

--

CREATE TABLE PASSEIO
    (
     pas_id          INTEGER  NOT NULL ,
     pas_data        DATE ,
     pas_hora_inicio TIME , -- Alterado de DATE para TIME
     pas_hora_final  TIME , -- Alterado de DATE para TIME
     pas_chamada_feita CHAR (1) ,
     pde_id          INTEGER  NOT NULL
    )
;

ALTER TABLE PASSEIO
    ADD CONSTRAINT PASSEIO_PK PRIMARY KEY ( pas_id ) ;

--

CREATE TABLE PASSEIO_DESCRICAO
    (
     pde_id          INTEGER  NOT NULL ,
     pde_descricao VARCHAR (75)
    )
;

ALTER TABLE PASSEIO_DESCRICAO
    ADD CONSTRAINT PASSEIO_DESCRICAO_PK PRIMARY KEY ( pde_id ) ;

--

CREATE TABLE PESSOA
    (
     pes_id          INTEGER  NOT NULL ,
     pes_nome        VARCHAR (75) ,
     pes_cpf         VARCHAR (11) ,
     pes_dt_nascimento DATE ,
     pes_rg          VARCHAR (9) ,
     pes_ativo       CHAR (1) ,
     end_id          INTEGER  NOT NULL
    )
;

ALTER TABLE PESSOA
    ADD CONSTRAINT PESSOA_PK PRIMARY KEY ( pes_id ) ;

--

CREATE TABLE PRESENCA_OFICINA
    (
     pso_id INTEGER  NOT NULL ,
     alu_id INTEGER  NOT NULL ,
     ofc_id INTEGER  NOT NULL
    )
;

ALTER TABLE PRESENCA_OFICINA
    ADD CONSTRAINT PRESENCA_OFICINA_PK PRIMARY KEY ( alu_id, ofc_id, pso_id ) ;

--

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

--

CREATE TABLE PROFESSOR
    (
     prof_id        INTEGER  NOT NULL ,
     prof_matricula VARCHAR (10) ,
     pes_id         INTEGER  NOT NULL
    )
;

ALTER TABLE PROFESSOR
    ADD CONSTRAINT PROFESSOR_PK PRIMARY KEY ( prof_id ) ;

--

CREATE TABLE R_ALUNO_OFICINA
    (
     alu_id INTEGER  NOT NULL ,
     ofc_id INTEGER  NOT NULL
    )
;

ALTER TABLE R_ALUNO_OFICINA
    ADD CONSTRAINT R_ALUNO_OFICINA_PK PRIMARY KEY ( alu_id, ofc_id ) ;

--

CREATE TABLE R_ALUNO_PASSEIO
    (
     pas_id INTEGER  NOT NULL ,
     alu_id INTEGER  NOT NULL
    )
;

ALTER TABLE R_ALUNO_PASSEIO
    ADD CONSTRAINT R_ALUNO_PASSEIO_PK PRIMARY KEY ( pas_id, alu_id ) ;

--

CREATE TABLE R_DIAS_OFICINA
    (
     ofc_id INTEGER  NOT NULL ,
     dia_id INTEGER  NOT NULL
    )
;

ALTER TABLE R_DIAS_OFICINA
    ADD CONSTRAINT R_DIAS_OFICINA_PK PRIMARY KEY ( ofc_id, dia_id ) ;

--

CREATE TABLE R_PROFESSOR_PASSEIO
    (
     prof_id INTEGER  NOT NULL ,
     pas_id  INTEGER  NOT NULL
    )
;

ALTER TABLE R_PROFESSOR_PASSEIO
    ADD CONSTRAINT R_PROFESSOR_PASSEIO_PK PRIMARY KEY ( pas_id, prof_id ) ;

--

CREATE TABLE TELEFONE
    (
     tel_id   INTEGER  NOT NULL ,
     tel_numero VARCHAR (11) ,
     pes_id   INTEGER  NOT NULL
    )
;

ALTER TABLE TELEFONE
    ADD CONSTRAINT TELEFONE_PK PRIMARY KEY ( tel_id ) ;

--

CREATE TABLE USUARIO
    (
     usr_id    INTEGER  NOT NULL ,
     usr_login VARCHAR (40) ,
     usr_senha VARCHAR (40) ,
     ctsr_id   INTEGER  NOT NULL ,
     usr_ativo CHAR (1)
    )
;

ALTER TABLE USUARIO
    ADD CONSTRAINT USUARIO_PK PRIMARY KEY ( usr_id ) ;

--
-- Definições de Chaves Estrangeiras

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

-- DDL para ATUALIZAR as tabelas existentes no PostgreSQL,
-- adicionando a funcionalidade de auto-incremento (IDENTITY)
-- para as chaves primárias simples.

-- 1. Tabela ALUNO
ALTER TABLE ALUNO
    ALTER COLUMN alu_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 2. Tabela CATEGORIA_USUARIO
ALTER TABLE CATEGORIA_USUARIO
    ALTER COLUMN ctsr_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 3. Tabela CIDADE
ALTER TABLE CIDADE
    ALTER COLUMN cid_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 4. Tabela DIAS
ALTER TABLE DIAS
    ALTER COLUMN dia_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 5. Tabela DIAS_MARCADOS_OFICINAS
ALTER TABLE DIAS_MARCADOS_OFICINAS
    ALTER COLUMN dmf_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 6. Tabela ENDERECO
ALTER TABLE ENDERECO
    ALTER COLUMN end_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 7. Tabela ESPECIALIDADES
ALTER TABLE ESPECIALIDADES
    ALTER COLUMN esp_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 8. Tabela ESTADO
ALTER TABLE ESTADO
    ALTER COLUMN est_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 9. Tabela OFICINA
ALTER TABLE OFICINA
    ALTER COLUMN ofc_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 10. Tabela PASSEIO
ALTER TABLE PASSEIO
    ALTER COLUMN pas_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 11. Tabela PASSEIO_DESCRICAO
ALTER TABLE PASSEIO_DESCRICAO
    ALTER COLUMN pde_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 12. Tabela PESSOA
ALTER TABLE PESSOA
    ALTER COLUMN pes_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 13. Tabela PROFESSOR
ALTER TABLE PROFESSOR
    ALTER COLUMN prof_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 14. Tabela TELEFONE
ALTER TABLE TELEFONE
    ALTER COLUMN tel_id ADD GENERATED BY DEFAULT AS IDENTITY;

-- 15. Tabela USUARIO
ALTER TABLE USUARIO
    ALTER COLUMN usr_id ADD GENERATED BY DEFAULT AS IDENTITY;


-- AVISO: Se você inseriu dados na tabela *antes* de executar estes comandos,
-- você deve reajustar o contador (sequence) para garantir que não haja
-- duplicidade ao tentar inserir novos registros.

-- COMANDOS OPCIONAIS PARA AJUSTAR O CONTADOR (RECOMENDADO SE JÁ HOUVER DADOS):

-- Função auxiliar que faz o ajuste (só precisa ser criada uma vez)
CREATE OR REPLACE FUNCTION reset_identity_sequence(table_name text, column_name text)
RETURNS void AS $$
DECLARE
    seq_name text;
    max_id bigint;
BEGIN
    -- Determina o nome da sequence criada pelo comando IDENTITY
    SELECT pg_get_serial_sequence(table_name, column_name) INTO seq_name;

    -- Encontra o maior valor atual na coluna
    EXECUTE format('SELECT COALESCE(MAX(%I), 1) FROM %I', column_name, table_name) INTO max_id;

    -- Define o próximo valor da sequence para o maior ID + 1
    EXECUTE format('SELECT setval(%L, %s, true)', seq_name, max_id);
END
$$ LANGUAGE plpgsql;

-- Executando a função de ajuste para cada tabela (se necessário)
SELECT reset_identity_sequence('aluno', 'alu_id');
SELECT reset_identity_sequence('categoria_usuario', 'ctsr_id');
SELECT reset_identity_sequence('cidade', 'cid_id');
SELECT reset_identity_sequence('dias', 'dia_id');
SELECT reset_identity_sequence('dias_marcados_oficinas', 'dmf_id');
SELECT reset_identity_sequence('endereco', 'end_id');
SELECT reset_identity_sequence('especialidades', 'esp_id');
SELECT reset_identity_sequence('estado', 'est_id');
SELECT reset_identity_sequence('oficina', 'ofc_id');
SELECT reset_identity_sequence('passeio', 'pas_id');
SELECT reset_identity_sequence('passeio_descricao', 'pde_id');
SELECT reset_identity_sequence('pessoa', 'pes_id');
SELECT reset_identity_sequence('professor', 'prof_id');
SELECT reset_identity_sequence('telefone', 'tel_id');
SELECT reset_identity_sequence('usuario', 'usr_id');

-- DROP FUNCTION reset_identity_sequence(text, text); -- Opcional: remover a função após o uso

