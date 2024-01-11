# language: pt
Funcionalidade: API - Pedidos

  Cenario: Registrar um novo pedido
    Dado que os produtos do pedido já foram registrados
    Quando submeter um novo pedido
    Entao o pedido é registrado com sucesso

  Cenario: Buscar um pedido existente
    Dado que os produtos do pedido já foram registrados
      E que um pedido já foi registrado
    Quando requisitar a busca de um pedido por id
    Entao pedido é exibido com sucesso detalhando os produtos

    Cenario:
