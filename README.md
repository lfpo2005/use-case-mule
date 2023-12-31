# Use Case Mulesoft

## Descrição

Este projeto é uma API desenvolvida com MuleSoft para gerenciamento de contas e transações bancárias. Ele utiliza PostgreSQL como banco de dados e é projetado para ser executado em um ambiente Docker.

## Pré-requisitos

- Docker e Docker Compose instalados
- MuleSoft Anypoint Studio

## Configuração do Ambiente

### Configuração do Docker para PostgreSQL

1. Abra o terminal e navegue até o diretório onde o arquivo `docker-compose.yml` está localizado.
   
2. Execute o seguinte comando para iniciar o container do PostgreSQL:

    ```bash
    docker-compose up -d
    ```

### Criação do Banco de Dados e Tabelas

1. Conecte-se ao banco de dados PostgreSQL usando qualquer cliente SQL ou faça isso pelo terminal.
   
2. Execute o seguinte comando SQL para criar o banco de dados:

    ```sql
    create database banco_mule;
    ```
3. Execute os seguintes comandos SQL para criar as tabelas    

    ```
    CREATE TABLE mule_account (
        account_id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL UNIQUE,
        account_number numeric(6) NOT NULL DEFAULT floor(random() * (999999 - 100000 + 1) + 100000) UNIQUE,
        balance DECIMAL(15, 2)
    );

    CREATE TABLE mule_transaction (
        transaction_id SERIAL PRIMARY KEY,
        account_id INT NOT NULL,
        from_account_id INT,
        types VARCHAR(50) NOT NULL,
        amount DECIMAL(15, 2) NOT NULL,
        date_transaction TIMESTAMP NOT NULL,
        FOREIGN KEY (account_id) REFERENCES mule_account(account_id),
        FOREIGN KEY (from_account_id) REFERENCES mule_account(account_id)
    );

    CREATE TABLE queries_extracts (
        account_number INT,
        last_query_date DATE,
        UNIQUE(account_number)
    );
    ```

## Implementação

1. Abra o projeto no Anypoint Studio.
2. Execute o projeto.

## Testando a API

Faça a importação da collection do postman, que esta na raiz do do projeto para o postman ou utilise o o API console no link a baixo e teste a api.

```
http://localhost:8081/console/
```

## Regras de Operação

### Saque
- Limite de R$1000 entre 22hs e 6hs

### Depósito
- Sem restrições

### Transferência entre contas
- Limite de R$1500 entre 22hs e 6hs

### Extrato
- Por período (limite de 1 mês)
- Por mês (limite de 1 mês por consulta)


## Próximos Passos

### Integração com GTW Kong e Keycloak

1. **Adicionar GTW Kong**: O primeiro passo é adicionar o Gateway de API Kong ao projeto.
2. **Chamada ao Keycloak**: O Kong fará uma chamada ao Keycloak para autenticação e autorização.
3. **Busca de Informações do Usuário**: O Keycloak buscará informações do usuário na aplicação Spring.
4. **Retorno de Token**: O token gerado pelo Keycloak, que contém as informações do usuário, será retornado para o Kong.

### Integração com Mule e Kafka

5. **Chamada ao Mule**: Após a autenticação e autorização, o Kong fará uma chamada ao Mule para prosseguir com o processamento.
6. **Publicação no Kafka**: A aplicação Spring publicará informações do usuário no Kafka.
7. **Consumo pelo Mule**: O Mule será responsável por consumir essas informações do Kafka para processamento adicional.
