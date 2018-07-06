# Star Wars Planet Api


Como pedido, a API para gerenciar os planetas do Star Wars. Ela permite buscar por nome ou id, adicionar/alterar e excluir. A quantidade de aparições nos filmes vem da SWAPI e é extraída de lá a cada PUT ou POST de um planeta.
# A busca das aparições
- As aparições dos planetas são buscadas na SWAPI pelo nome exato do planeta, ou seja "Coruscant" tem resultado diferente de "coruscant". As buscas são feitas sempre que ocorre uma inserção/atualização de planeta, no caso nos PUTs e nos POSTs.

# Dependencias
- O sistema depende do Spring Web e de um banco de dados MongoDB de nome b2w_star_wars_planets, definido no application.properties com a chave/valor = spring.data.mongodb.database=b2w_star_wars_planets

# GET

  - /planet/name/{name}   Busca por nome. O nome tem que ser exatamente igual. Falha com 404 se não encontrado
  - /planet/id/{id} Busca por id. Falha com 404 se não encontrado.
  - /planet/all/ Pega todos, em uma lista.

# PUT
  - /planet/ Adiciona um planeta NOVO. Nesse momento faz-se a busca da quantidade de aparições. Falha com NOT_ACCEPTABLE se já houver um planeta com esse nome.

# POST
  - /planet/ Adiciona um planeta novo ou altera um existente. Nesse momento faz-se a busca da quantidade de aparições.

# DELETE
  - /planet/id/{id} Deleta o planeta com o id dado. Retorna com NOT_FOUND se não encontrado.
  - /planet/name/{name} Deleta o planeta com o nome dado. Retorna com NOT_FOUND se não encontrado.

