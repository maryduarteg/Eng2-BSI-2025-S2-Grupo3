DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM categoria_usuario WHERE ctsr_id = 1) THEN
        INSERT INTO categoria_usuario (ctsr_id, ctsr_desc)
        VALUES (1, 'Diretor');
    END IF;

	IF NOT EXISTS (SELECT 1 FROM categoria_usuario WHERE ctsr_id = 2) THEN
        INSERT INTO categoria_usuario (ctsr_id, ctsr_desc)
        VALUES (2, 'Gestor');
    END IF;

	IF NOT EXISTS (SELECT 1 FROM categoria_usuario WHERE ctsr_id = 3) THEN
        INSERT INTO categoria_usuario (ctsr_id, ctsr_desc)
        VALUES (3, 'Professor');
    END IF;

	IF NOT EXISTS (SELECT 1 FROM usuario WHERE usr_id = 1) THEN
        INSERT INTO usuario (usr_id, usr_login, usr_senha, ctsr_id, usr_ativo)
        VALUES (1, 'Suporte', 'Mudar@123', 1, 1);
    END IF;
END $$;