# Tech Challenge FIAP - Burger
Projeto da pós graduação da FIAP de Arquitetura de Software

# Repositórios relacionados
* [Serviço de produto](https://github.com/souzamarcos/tech-challenge-ms-product)
* [Serviço de pedidos](https://github.com/souzamarcos/tech-challenge-ms-order)
* [Serviço de pagamento](https://github.com/souzamarcos/tech-challenge-ms-payment)
* [Serviço de cliente](https://github.com/souzamarcos/tech-challenge-ms-customer)
* [Serviço de notificação](https://github.com/souzamarcos/tech-challenge-ms-notification)
* [Infraestrutura Terraform](https://github.com/souzamarcos/tech-challenge-terraform)
* [Configuração do Kubernetes](https://github.com/souzamarcos/tech-challenge-kubernetes)
* [Lambda de Autenticação](https://github.com/souzamarcos/tech-challenge-authentication-lambda)

## Design / Módulos

![Design dos módulos da aplicação](static/design_aplicacao.png)

## Documentação
* [Wiki - Notion](https://www.notion.so/9dfe9780ad5f4d9587adc565f54bb70f?v=f2ef9c679bcf4ad1b857479c1f317c25)
* [Event Storm - MIRO](https://miro.com/app/board/uXjVMK9Fze8=/?share_link_id=624130302810)

## Dependências
* [IntelliJ IDEA (Opcional)](https://www.jetbrains.com/idea/download/#section=windows)
* [Java JDK 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)
* [Spring Boot 3.1.0](https://spring.io/projects/spring-boot)
* [Gradle 7.6.1](https://gradle.org/)
* [Flyway](https://flywaydb.org/)
* [Jacoco](https://www.jacoco.org/jacoco/trunk/index.html)


## Endpoints

Para visualizar os endpoints disponíveis na aplicação basta acessar o swagger em [http://localhost:8080/swagger](http://localhost:8080/swagger)


## Desenvolvimento

Para executar a aplicação localmente sem depender de recursos externos à máquina a aplicação deve rodar com a variável de ambiente `SPRING_PROFILES_ACTIVE` com valor **diferente** de `production` ou **vazia**.


### Executando somente dependências

Para executar somente dependências externas (Mysql, RabbitMQ, etc) da aplicação para o ambiente de desevolvimento local basta executar o comando abaixo:

```bash
docker-compose -f docker-compose-without-application.yml up --build
```

A aplicação será exposta na porta 8080.

### Localstack
A aplicação está com o dynamodb configurado no localstack para simular a AWS localmente.
Os recursos estão sendo criados ao iniciar a imagem do localstack através do arquivo [init-aws.sh](config/localstack/init-aws.sh).

Para listar recursos do localstack use o comando `aws` com o parâmetro `--endpoint-url=http://localhost:4566`, como no exemplo abaixo:

```bash
aws --endpoint-url=http://localhost:4566 dynamodb list-tables
```

### Versionamento de libs gradle

Estamos usando um arquivo [.toml para versionar](https://docs.gradle.org/current/userguide/platforms.html#sub::toml-dependencies-format) as libs utilizadas pelo gradle. Para saber mais acesse o arquivo [libs.versions.toml](gradle/libs.versions.toml).

### Cobertura de teste

Foi configurado juntamente com o plugin do jacoco para o gradle a task com nome `codeCoverageReport` que irá disparar a execução de testes da aplicação e gerar o relatório hml. Para isso execute o comando:
```bash
./gradlew codeCoverageReport 
```
Para acessar esse relatório gerado acesse o caminho `build/reports/jacoco/codeCoverageReport/html/index.hml` e abre no navegador.

![img.png](static/jacoco_report_example.png)


### Executando aplicação completa com docker

Execute o comando abaixo para iniciar os containers com a base de dados e executar a aplicação localmente.

```bash
docker-compose up --build
```
