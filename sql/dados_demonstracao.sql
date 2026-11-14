-- DADOS DE DEMONSTRAÇÃO DO BANCO "academia"
-- Rodar DEPOIS do modelo_academia_3.sql (ele recria o banco vazio).
-- Os ids começam em 1 porque o banco é recriado do zero.

USE academia;
SET NAMES utf8mb4; -- garante que os acentos sejam gravados corretamente

-- =========================================================
-- UNIDADES (ids 1 a 3)
-- =========================================================
INSERT INTO unidade (nome, endereco, telefone, horario_funcionamento, data_inauguracao, custo_mensal_ocupacao) VALUES
('Unidade Asa Norte',   'SCLN 210 Bloco B, Brasília - DF',     '(61) 3333-1001', 'Seg a Sex 6h-23h / Sáb 8h-14h', '2019-03-10', 18500.00),
('Unidade Taguatinga',  'QS 03 Rua 210, Taguatinga - DF',      '(61) 3333-2002', 'Seg a Sex 6h-22h / Sáb 8h-13h', '2021-08-02', 14200.00),
('Unidade Águas Claras','Av. Araucárias 1500, Águas Claras - DF','(61) 3333-3003', 'Seg a Dom 5h-23h',              '2023-01-16', 21000.00);

-- =========================================================
-- PLANOS (ids 1 a 4)
-- =========================================================
INSERT INTO plano (nome, valor, duracao_meses, acesso_multiunidade) VALUES
('Mensal',      119.90,  1, 0),
('Trimestral',  329.90,  3, 0),
('Semestral',   599.90,  6, 1),
('Anual',       1079.90, 12, 1);

-- =========================================================
-- MODALIDADES (ids 1 a 4)
-- =========================================================
INSERT INTO modalidade (nome, categoria, ambiente_necessario, experiencia_minima, duracao_limite) VALUES
('Musculação', 'Força',     'Sala de musculação', 'Iniciante',     90),
('Spinning',   'Aeróbico',  'Sala de bike',       'Iniciante',     50),
('Funcional',  'Condicionamento', 'Área funcional', 'Intermediário', 60),
('Pilates',    'Flexibilidade', 'Estúdio',         'Iniciante',     55);

-- =========================================================
-- ALUNOS (pessoa + aluno) (pessoa_id 1 a 10)
-- =========================================================
INSERT INTO pessoa (nome, cpf, data_nascimento, telefone, email) VALUES
('Ana Beatriz Souza',    '11122233301', '2001-04-12', '(61) 99101-0001', 'ana.souza@email.com'),
('Bruno Carvalho Lima',  '11122233302', '1998-09-23', '(61) 99101-0002', 'bruno.lima@email.com'),
('Carla Mendes Rocha',   '11122233303', '2003-01-05', '(61) 99101-0003', 'carla.rocha@email.com'),
('Diego Ferreira Alves', '11122233304', '1995-07-30', '(61) 99101-0004', 'diego.alves@email.com'),
('Eduarda Pires Nunes',  '11122233305', '2000-11-18', '(61) 99101-0005', 'eduarda.nunes@email.com'),
('Felipe Gomes Costa',   '11122233306', '1992-02-14', '(61) 99101-0006', 'felipe.costa@email.com'),
('Gabriela Ramos Silva', '11122233307', '2004-06-09', '(61) 99101-0007', 'gabriela.silva@email.com'),
('Henrique Dias Moura',  '11122233308', '1989-12-01', '(61) 99101-0008', 'henrique.moura@email.com'),
('Isabela Teixeira',     '11122233309', '2002-03-27', '(61) 99101-0009', 'isabela.teixeira@email.com'),
('João Pedro Martins',   '11122233310', '1999-08-15', '(61) 99101-0010', 'joao.martins@email.com');

INSERT INTO aluno (pessoa_id, endereco, status) VALUES
(1,  'Asa Norte, Brasília - DF',     'ativo'),
(2,  'Asa Sul, Brasília - DF',       'ativo'),
(3,  'Taguatinga Norte - DF',        'ativo'),
(4,  'Taguatinga Sul - DF',          'ativo'),
(5,  'Águas Claras - DF',            'ativo'),
(6,  'Guará II - DF',                'inativo'),
(7,  'Águas Claras - DF',            'ativo'),
(8,  'Lago Norte - DF',              'ativo'),
(9,  'Vicente Pires - DF',           'ativo'),
(10, 'Sudoeste, Brasília - DF',      'inativo');

-- =========================================================
-- MATRÍCULAS (rotina principal)
-- Meses, planos, unidades e status variados para testar o JOIN e o relatório.
-- data_termino = data_inicio + duração do plano
-- =========================================================
INSERT INTO matricula (aluno_id, plano_id, unidade_id, data_inicio, data_termino, status, forma_renovacao, motivo_cancelamento, canal_matricula) VALUES
(1,  4, 1, '2026-01-10', '2027-01-10', 'ativa',     'automatica', NULL,                  'presencial'),
(2,  1, 1, '2026-02-03', '2026-03-03', 'encerrada', 'manual',     NULL,                  'site'),
(3,  2, 2, '2026-03-15', '2026-06-15', 'encerrada', 'manual',     NULL,                  'presencial'),
(4,  3, 2, '2026-04-20', '2026-10-20', 'cancelada', 'manual',     'Mudança de cidade',   'aplicativo'),
(5,  4, 3, '2026-05-02', '2027-05-02', 'ativa',     'automatica', NULL,                  'site'),
(6,  1, 2, '2026-06-11', '2026-07-11', 'cancelada', 'manual',     'Problemas financeiros','presencial'),
(7,  3, 3, '2026-07-07', '2027-01-07', 'ativa',     'automatica', NULL,                  'aplicativo'),
(8,  2, 1, '2026-08-19', '2026-11-19', 'ativa',     'manual',     NULL,                  'presencial'),
(9,  1, 3, '2026-09-25', '2026-10-25', 'encerrada', 'manual',     NULL,                  'site'),
(10, 3, 1, '2026-10-05', '2027-04-05', 'cancelada', 'manual',     'Lesão',               'presencial'),
(2,  2, 1, '2026-10-12', '2027-01-12', 'ativa',     'manual',     NULL,                  'aplicativo'),
(9,  2, 3, '2026-10-28', '2027-01-28', 'ativa',     'automatica', NULL,                  'site');
