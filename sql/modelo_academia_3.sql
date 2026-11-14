-- MODELO DO BANCO "academia" (somente estrutura; os dados estão em dados_demonstracao.sql)
-- Alterações em relação à versão anterior:
--   1) pessoa.senha_hash        -> login no sistema
--   2) matricula.unidade_id     -> unidade onde o aluno se matriculou
--   3) tabela inscricao_aula    -> inscrição do aluno nas aulas
--   4) aula: colunas redundantes removidas (FK composta com professor_modalidade)
--
-- ATENÇÃO: a linha abaixo apaga o banco "academia" se ele já existir.
-- Isso permite rodar o script do zero quantas vezes precisar durante o desenvolvimento.
DROP DATABASE IF EXISTS academia;
CREATE DATABASE academia CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE academia;

-- =========================================================
-- TABELAS
-- =========================================================

CREATE TABLE unidade (
    unidade_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20),
    horario_funcionamento VARCHAR(100),
    data_inauguracao DATE,
    custo_mensal_ocupacao DECIMAL(10,2)
);

CREATE TABLE pessoa (
    pessoa_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    -- ALTERAÇÃO: senha de acesso ao sistema (login por CPF + senha).
    -- Guarda o hash SHA-256 em hexadecimal (64 caracteres), nunca a senha em texto puro.
    -- NULL = pessoa sem acesso ao sistema (ex.: convidado que ainda não é aluno).
    senha_hash CHAR(64) NULL,
    data_cadastro DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE aluno (
    pessoa_id INT PRIMARY KEY,
    endereco VARCHAR(200),
    status VARCHAR(20) DEFAULT 'ativo',
    CONSTRAINT fk_aluno_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(pessoa_id)
);

CREATE TABLE funcionario (
    pessoa_id INT PRIMARY KEY,
    matricula_funcional VARCHAR(20) NOT NULL UNIQUE,
    data_admissao DATE NOT NULL,
    salario DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ativo',
    CONSTRAINT fk_funcionario_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(pessoa_id)
);

CREATE TABLE professor (
    pessoa_id INT PRIMARY KEY,
    registro_profissional VARCHAR(30),
    apto_avaliacao_fisica TINYINT(1) DEFAULT 0,
    CONSTRAINT fk_professor_funcionario FOREIGN KEY (pessoa_id) REFERENCES funcionario(pessoa_id)
);

CREATE TABLE recepcao (
    pessoa_id INT PRIMARY KEY,
    unidade_id INT NOT NULL,
    turno VARCHAR(20),
    CONSTRAINT fk_recepcao_funcionario FOREIGN KEY (pessoa_id) REFERENCES funcionario(pessoa_id),
    CONSTRAINT fk_recepcao_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE gerente (
    pessoa_id INT PRIMARY KEY,
    unidade_id INT NOT NULL UNIQUE,
    data_inicio_funcao DATE,
    email_corporativo VARCHAR(100),
    CONSTRAINT fk_gerente_funcionario FOREIGN KEY (pessoa_id) REFERENCES funcionario(pessoa_id),
    CONSTRAINT fk_gerente_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE convite (
    convite_id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    convidado_id INT NOT NULL,
    unidade_id INT NOT NULL,
    data_convite DATE NOT NULL DEFAULT (CURRENT_DATE),
    data_validade DATE,
    status VARCHAR(20) DEFAULT 'pendente',
    CONSTRAINT fk_convite_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(pessoa_id),
    CONSTRAINT fk_convite_convidado FOREIGN KEY (convidado_id) REFERENCES pessoa(pessoa_id),
    CONSTRAINT fk_convite_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE ambiente (
    ambiente_id INT AUTO_INCREMENT PRIMARY KEY,
    unidade_id INT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    tipo VARCHAR(30),
    capacidade_fisica INT,
    area DECIMAL(6,2),
    status VARCHAR(20) DEFAULT 'disponivel',
    CONSTRAINT fk_ambiente_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE equipamento (
    equipamento_id INT AUTO_INCREMENT PRIMARY KEY,
    unidade_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    quantidade_disponivel INT DEFAULT 0,
    fabricante VARCHAR(100),
    espaco_ocupado DECIMAL(6,2),
    data_aquisicao DATE,
    CONSTRAINT fk_equipamento_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE modalidade (
    modalidade_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    categoria VARCHAR(20),
    ambiente_necessario VARCHAR(30),
    experiencia_minima VARCHAR(20),
    duracao_limite SMALLINT COMMENT 'em minutos'
);

CREATE TABLE professor_modalidade (
    professor_id INT NOT NULL,
    modalidade_id INT NOT NULL,
    nivel VARCHAR(20),
    PRIMARY KEY (professor_id, modalidade_id),
    CONSTRAINT fk_profmod_professor FOREIGN KEY (professor_id) REFERENCES professor(pessoa_id),
    CONSTRAINT fk_profmod_modalidade FOREIGN KEY (modalidade_id) REFERENCES modalidade(modalidade_id)
);

CREATE TABLE plano (
    plano_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    valor DECIMAL(8,2) NOT NULL,
    duracao_meses TINYINT NOT NULL,
    acesso_multiunidade TINYINT(1) DEFAULT 0
);

CREATE TABLE beneficio (
    beneficio_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE plano_beneficio (
    plano_id INT NOT NULL,
    beneficio_id INT NOT NULL,
    PRIMARY KEY (plano_id, beneficio_id),
    CONSTRAINT fk_planobeneficio_plano FOREIGN KEY (plano_id) REFERENCES plano(plano_id),
    CONSTRAINT fk_planobeneficio_beneficio FOREIGN KEY (beneficio_id) REFERENCES beneficio(beneficio_id)
);

CREATE TABLE matricula (
    matricula_id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    plano_id INT NOT NULL,
    -- ALTERAÇÃO: unidade (franquia) onde o aluno fez a matrícula.
    unidade_id INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_termino DATE,
    status VARCHAR(20) DEFAULT 'ativa',
    forma_renovacao VARCHAR(20),
    motivo_cancelamento VARCHAR(50),
    canal_matricula VARCHAR(20),
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(pessoa_id),
    CONSTRAINT fk_matricula_plano FOREIGN KEY (plano_id) REFERENCES plano(plano_id),
    CONSTRAINT fk_matricula_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

CREATE TABLE pagamento (
    pagamento_id INT AUTO_INCREMENT PRIMARY KEY,
    matricula_id INT NOT NULL,
    data_pagamento DATE NOT NULL,
    valor_pago DECIMAL(8,2) NOT NULL,
    forma_pagamento VARCHAR(20),
    status VARCHAR(20) DEFAULT 'pago',
    numero_parcela SMALLINT,
    CONSTRAINT fk_pagamento_matricula FOREIGN KEY (matricula_id) REFERENCES matricula(matricula_id)
);

CREATE TABLE avaliacao_fisica (
    avaliacao_id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    professor_id INT NOT NULL,
    unidade_id INT NOT NULL,
    data_avaliacao DATE NOT NULL,
    peso DECIMAL(5,2),
    percentual_gordura DECIMAL(4,2),
    massa_magra DECIMAL(5,2),
    classificacao_risco_cardiovascular VARCHAR(20),
    CONSTRAINT fk_avaliacao_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(pessoa_id),
    CONSTRAINT fk_avaliacao_professor FOREIGN KEY (professor_id) REFERENCES professor(pessoa_id),
    CONSTRAINT fk_avaliacao_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(unidade_id)
);

-- AJUSTE: removidas as colunas redundantes professor_modalidade_*.
-- A FK composta agora aponta direto para (professor_id, modalidade_id),
-- garantindo que só dá aula quem está habilitado na modalidade.
CREATE TABLE aula (
    aula_id INT AUTO_INCREMENT PRIMARY KEY,
    professor_id INT NOT NULL,
    modalidade_id INT NOT NULL,
    ambiente_id INT NOT NULL,
    data_aula DATE NOT NULL,
    horario_inicio TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'agendada',
    horario_efetivo_termino TIME,
    observacoes VARCHAR(255),
    CONSTRAINT fk_aula_prof_mod FOREIGN KEY (professor_id, modalidade_id)
        REFERENCES professor_modalidade(professor_id, modalidade_id),
    CONSTRAINT fk_aula_ambiente FOREIGN KEY (ambiente_id) REFERENCES ambiente(ambiente_id)
);

-- ALTERAÇÃO: nova tabela associativa (aluno N:N aula).
-- Permite o aluno se inscrever nas aulas e a academia controlar a lotação
-- (comparando o total de inscritos com ambiente.capacidade_fisica).
CREATE TABLE inscricao_aula (
    aluno_id INT NOT NULL,
    aula_id INT NOT NULL,
    data_inscricao DATE NOT NULL DEFAULT (CURRENT_DATE),
    status VARCHAR(20) DEFAULT 'confirmada',
    PRIMARY KEY (aluno_id, aula_id),
    CONSTRAINT fk_inscricao_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(pessoa_id),
    CONSTRAINT fk_inscricao_aula FOREIGN KEY (aula_id) REFERENCES aula(aula_id)
);
