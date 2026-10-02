# AutoGestor

Sistema web de gestão para oficinas e funilarias, desenvolvido em Java com Spring Boot.

O **AutoGestor** centraliza o gerenciamento de clientes, veículos, serviços, ordens de serviço e usuários em uma única aplicação, com autenticação, níveis de acesso e área de perfil.

## Funcionalidades

- Dashboard com totais de clientes, veículos, serviços e ordens de serviço
- Cadastro, edição, listagem e exclusão de clientes
- Cadastro, edição, listagem e exclusão de veículos
- Cadastro, edição, listagem e exclusão de serviços
- Cadastro, edição, listagem e exclusão de ordens de serviço
- Login e logout com Spring Security
- Senhas armazenadas com BCrypt
- Dois níveis de acesso:
  - **Administrador**
  - **Funcionário**
- Área de gerenciamento de usuários exclusiva para administradores
- Proteção contra exclusão da própria conta
- Proteção para evitar que o sistema fique sem administrador
- Perfil do usuário
- Alteração de nome e nome de usuário
- Alteração de senha
- Upload de foto de perfil
- Sidebar reutilizável com Thymeleaf Fragment
- Exclusões realizadas por requisições POST

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- Thymeleaf Extras Spring Security
- MySQL
- Maven
- HTML
- CSS

## Estrutura do projeto

```text
src/main/java/org/example/autogestor
├── config
├── controller
├── model
├── repository
├── service
└── AutoGestorApplication.java

src/main/resources
├── static
│   └── css
├── templates
│   ├── fragments
│   └── ...
└── application.properties
```

## Principais módulos

### Clientes
Permite cadastrar e gerenciar os clientes da oficina.

### Veículos
Cada veículo é associado a um cliente cadastrado.

### Serviços
Permite cadastrar os serviços oferecidos e seus respectivos valores.

### Ordens de Serviço
As ordens relacionam veículo, cliente e serviço, além de armazenar data, status e valor total.

### Usuários
Administradores podem cadastrar e gerenciar contas de acesso ao sistema.

Os usuários podem possuir os perfis:
- `ADMIN` — exibido no sistema como **Administrador**
- `USUARIO` — exibido no sistema como **Funcionário**

### Meu Perfil
Cada usuário pode:
- visualizar seus dados;
- alterar nome e username;
- alterar sua senha;
- adicionar ou trocar a foto de perfil.

## Segurança

O projeto utiliza **Spring Security** para autenticação e autorização.

As senhas são codificadas com **BCrypt** antes de serem armazenadas no banco de dados.

A área `/usuarios/**` é restrita a contas com perfil de administrador.

Informações sensíveis, como a senha do MySQL, não ficam gravadas diretamente no código-fonte.

## Configuração do banco de dados

Crie um banco MySQL chamado:

```sql
CREATE DATABASE autogestor
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

A aplicação utiliza:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/autogestor
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

O Hibernate está configurado com:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Variáveis de ambiente

Antes de executar o projeto, configure:

```text
DB_PASSWORD=sua_senha_do_mysql
```

Em uma instalação nova, sem nenhum usuário cadastrado, também configure:

```text
ADMIN_NAME=Administrador
ADMIN_USERNAME=admin
ADMIN_PASSWORD=sua_senha_inicial
```

## Como executar

### Pré-requisitos
- Java 17
- MySQL
- Maven, ou o Maven Wrapper incluído no projeto
- IntelliJ IDEA, Eclipse ou outra IDE Java

### 1. Clone o repositório

```bash
git clone https://github.com/Martchellinho/AutoGestor.git
```

### 2. Entre na pasta

```bash
cd AutoGestor
```

### 3. Crie o banco

```sql
CREATE DATABASE autogestor
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 4. Configure as variáveis de ambiente

```text
DB_PASSWORD=sua_senha_do_mysql
ADMIN_NAME=Administrador
ADMIN_USERNAME=admin
ADMIN_PASSWORD=sua_senha_inicial
```

> As variáveis `ADMIN_*` só são necessárias para criar o primeiro administrador em um banco sem usuários.

### 5. Execute a aplicação

No Windows:

```bash
mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

Ou execute diretamente `AutoGestorApplication.java`.

### 6. Acesse

```text
http://localhost:8080
```

## Upload de fotos

As fotos de perfil são armazenadas localmente em:

```text
uploads/perfil
```

A pasta `uploads/` está ignorada pelo Git para evitar publicar imagens pessoais ou arquivos enviados pelos usuários.

O limite configurado para upload é de **10 MB**.

# Screenshots

## Login
Tela de autenticação do AutoGestor.

![Login](docs/images/login.png.png)

## Dashboard
Visão geral com os totais de clientes, veículos, serviços e ordens de serviço.

![Dashboard](docs/images/dashboard.png.png)

## Clientes
Tela de gerenciamento dos clientes cadastrados.

![Clientes](docs/images/clientes.png.png)

## Veículos
Tela de gerenciamento dos veículos vinculados aos clientes.

![Veículos](docs/images/veiculos.png.png)

## Serviços
Tela de cadastro e gerenciamento dos serviços oferecidos pela funilaria.

![Serviços](docs/images/servicos.png.png)

## Ordens de Serviço
Gerenciamento das ordens, relacionando cliente, veículo, serviço, data, status e valor.

![Ordens de Serviço](docs/images/ordens.png.png)

## Meu Perfil
Área para visualizar dados, alterar informações, senha e foto de perfil.

![Meu Perfil](docs/images/perfil.png.png)

## Usuários
Área administrativa para gerenciamento das contas de acesso, incluindo perfis de Administrador e Funcionário.

![Usuários](docs/images/usuarios.png.png)

## Possíveis melhorias futuras

- Busca e filtros nas tabelas
- Paginação
- Relatórios de faturamento
- Histórico de alterações das ordens
- Status com indicadores visuais
- Upload de imagens dos veículos e serviços realizados
- Deploy da aplicação em servidor
- Banco de dados em ambiente de produção
- Testes automatizados mais completos

## Objetivo do projeto

O AutoGestor foi desenvolvido como projeto de portfólio para aplicar, em um sistema completo, conceitos de:

- desenvolvimento web com Java;
- arquitetura em camadas;
- banco de dados relacional;
- CRUD;
- autenticação e autorização;
- relacionamentos entre entidades;
- segurança de senhas;
- gerenciamento de arquivos;
- interface web com Thymeleaf.

## Autor

Desenvolvido por **Martchellinho**.

GitHub: https://github.com/Martchellinho
