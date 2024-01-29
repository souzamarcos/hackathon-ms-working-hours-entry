# language: pt
Funcionalidade: API - Pedidos

  Cenario: Registrar um novo pedido
    Quando submeter um novo pedido
    Entao o pedido é registrado com sucesso

  Cenario: Buscar um pedido existente
    Dado que um pedido já foi registrado
    Quando requisitar a busca de um pedido por id
    Entao pedido é exibido com sucesso detalhando os produtos

  Cenario: Levar um pedido até o finalizado
    Dado que um pedido já foi registrado
    Quando realizar o checkout do pedido
    E mudar status para em preparacao
    E mudar status para pronto
    E mudar status para finalizado
    Entao pedido deve estar gravado com status finalizado

  Cenario: Buscar pedidos em progresso
    Dado que um pedido já foi registrado
    Quando realizar o checkout do pedido
    E requisitar a busca de pedidos em progresso
    Entao pedido deve estar entre os pedidos retornados

  Cenario: Buscar pedidos com status recebido
    Dado que um pedido já foi registrado
    Quando realizar o checkout do pedido
    E requisitar a busca de pedidos com status recebido
    Entao pedido deve estar entre os pedidos retornados

  Cenario: Registrar um novo pedido especificando cliente
    Quando submeter um novo pedido especificando cliente
    Entao o pedido especificando cliente é registrado com sucesso

  Cenario: Buscar um pedido especificando cliente existente
    Dado que um pedido com cliente já foi registrado
    Quando requisitar a busca de um pedido por id
    Entao o pedido especificando cliente é registrado com sucesso
