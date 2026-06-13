-- Categorias
INSERT INTO categoria (nome) VALUES ('Aventura');
INSERT INTO categoria (nome) VALUES ('Acao');
INSERT INTO categoria (nome) VALUES ('RPG');

-- Empresas
INSERT INTO empresa (nome, paisorigem, descricao) VALUES ('Nintendo', 'Japao', 'Criadora do Mario');
INSERT INTO empresa (nome, paisorigem, descricao) VALUES ('Rockstar Games', 'EUA', 'Criadora do GTA');
INSERT INTO empresa (nome, paisorigem, descricao) VALUES ('CD Projekt Red', 'Polonia', 'Criadora do Witcher');

-- Plataformas
INSERT INTO plataforma (nome) VALUES ('PC');
INSERT INTO plataforma (nome) VALUES ('PlayStation 5');
INSERT INTO plataforma (nome) VALUES ('Xbox Series X');
INSERT INTO plataforma (nome) VALUES ('Nintendo Switch');

-- Jogos
INSERT INTO jogo (titulo, descricao, preco, estoque, data_lancamento, id_empresa) VALUES ('The Legend of Zelda', 'Aventura em Hyrule', 299.90, 50, '2017-03-03', 1);
INSERT INTO jogo (titulo, descricao, preco, estoque, data_lancamento, id_empresa) VALUES ('Grand Theft Auto V', 'Mundo aberto criminal', 89.90, 100, '2013-09-17', 2);
INSERT INTO jogo (titulo, descricao, preco, estoque, data_lancamento, id_empresa) VALUES ('Cyberpunk 2077', 'RPG futurista', 199.90, 20, '2020-12-10', 3);

-- Vinculos Jogo <-> Plataforma
INSERT INTO jogo_plataforma (id_jogo, id_plataforma) VALUES (1, 4);
INSERT INTO jogo_plataforma (id_jogo, id_plataforma) VALUES (2, 1);
INSERT INTO jogo_plataforma (id_jogo, id_plataforma) VALUES (2, 2);
INSERT INTO jogo_plataforma (id_jogo, id_plataforma) VALUES (3, 1);

-- Vinculos Jogo <-> Categoria
INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (1, 1);
INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (2, 2);
INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (3, 3);

-- Usuarios
INSERT INTO tb_usuario (nome, email, senha, perfil, telefone, cpf) VALUES ('Kauan Batista', 'kauanbs@gmail.com', 'XhxmhwV0yzMP0iPzTYBk8m6M4NJbXZhswMxmm+OFOFg2USii7Ubp3/sJcK8Cc7heLtfn7tAkXRjCJnilyEu0Cw==', 'ADM', '(63) 99999-0000', '000.000.000-00');
INSERT INTO tb_usuario (nome, email, senha, perfil, telefone, cpf) VALUES ('Jon Doe', 'jondoe@gmail.com', 'XhxmhwV0yzMP0iPzTYBk8m6M4NJbXZhswMxmm+OFOFg2USii7Ubp3/sJcK8Cc7heLtfn7tAkXRjCJnilyEu0Cw==', 'USER', '(63) 98888-0000', '111.111.111-11');

-- Endereco
INSERT INTO endereco (cep, cidade, logradouro, numero, bairro, complemento, estado) VALUES ('77023-050', 'Palmas', 'Av. Teotonio Segurado', '14', 'Plano Diretor Sul', 'Casa', 'TO');
INSERT INTO usuario_endereco (id_usuario, id_endereco) VALUES (2, 1);

-- Cupons
INSERT INTO cupom (codigo, percentualdesconto, ativo) VALUES ('GAME10', 10.0, true);
INSERT INTO cupom (codigo, percentualdesconto, ativo) VALUES ('PIX15', 15.0, true);

-- Pagamento
INSERT INTO pagamento (valor, dataconfirmacao) VALUES (89.90, '2024-05-20 10:00:00');
INSERT INTO pagamentoboleto (id, numeroboleto, datavencimento) VALUES (1, '12345.67890', '2024-05-25');

-- Pedido
INSERT INTO pedido (datapedido, valortotal, id_usuario, id_pagamento, id_endereco) VALUES ('2024-05-20 09:30:00', 89.90, 2, 1, 1);

-- Itens do Pedido
INSERT INTO itempedido (precounitario, quantidade, id_jogo, id_pedido) VALUES (89.90, 1, 2, 1);

ALTER SEQUENCE categoria_id_seq RESTART WITH 100;
ALTER SEQUENCE empresa_id_seq RESTART WITH 100;
ALTER SEQUENCE plataforma_id_seq RESTART WITH 100;
ALTER SEQUENCE jogo_id_seq RESTART WITH 100;
ALTER SEQUENCE usuario_id_seq RESTART WITH 100;
ALTER SEQUENCE endereco_id_seq RESTART WITH 100;
ALTER SEQUENCE pagamento_id_seq RESTART WITH 100;
ALTER SEQUENCE pedido_id_seq RESTART WITH 100;
ALTER SEQUENCE itempedido_id_seq RESTART WITH 100;
ALTER SEQUENCE cupom_id_seq RESTART WITH 100;
