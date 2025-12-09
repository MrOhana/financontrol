### OBJETIVO
Construa uma aplicação utilizando que seja um controle financeiro onde eu posso cadastrar metas e lançar minhas receitas e minhas despesas.

### REQUISITOS TECNICOS
1- A aplicação deve ser construida em Java
2- A aplicação deve ser construida em Spring como backend
3- A aplicação deve ser construida em Spring Data JPA
4- A aplicação deve ser construida em Spring Web
5- A aplicação deve ser construida em Spring Security
6- A aplicação deve ser construida em Apache Wicket como frontend
7- A aplicação deve ser capaz de acessar um banco de dados PostgreSQL
8- A aplicação deve ser capaz de gerar logs utilizando o SLF4J e o Logback

### REQUISITOS FUNCIONAIS
1- O sistema deve permitir que eu cadastre uma despesa lançando o nome, a categoria, se é uma despesa fixa ou variável, o valor e a data da despesa, 
porque eu posso lançar despesas retroativas.
2- O sistema deve permitir que eu cadastre uma receita lançando o nome, a categoria, se é uma receita fixa ou variável, o valor e a data da receita, 
porque eu posso lançar receitas retroativas.
3- Ao cadastrar metas, eu posso informar metas com data fim vazia, ou seja, serão metas de longo prazo que ainda não terei visibilidade da realização.
4- Ao entrar na aplicação devo ver uma tela de login onde eu devo entrar com o usuário e a senha.
5- Caso eu ainda não possua cadastro, o sistema deve permitir que eu realize um cadastro, informando o nome, email e senha.
6- A senha do cliente deve ser criptografada usando hash Argon2 e um salt aleatório.
7- O sistema deve permitir que eu recupere minha senha, informando o email cadastrado.
8- O sistema deve enviar um email para o email cadastrado com um link para recuperação de senha.

### ENTIDADES DATA JPA
1- User
Cadastra os usuários que irão utilizar a aplicação

Campos:
- name
- email (unique)
- password
- created_at
- updated_at
- status (ACTIVE, INACTIVE)

2- Goal
Cadastra as metas macro que queremos alcançar, como por exemplo, realizar uma viagem, trocar de carro, mudar de casa, etc.

Campos:
- name
- description
- start_date
- end_date
- created_at
- updated_at

3- ExpenseCategory
Cadastra as categorias que as despesas serão classificadas sempre que forem lançadas

Campos:
- name
- description
- created_at

4- Expense
Cadastra as despesas que serão lançadas sempre que forem lançadas

Campos:
- name
- description
- value
- date
- category_id
- goal_id
- created_at

5- Income
Cadastra as receitas que serão lançadas sempre que forem lançadas

Campos:
- name
- description
- value
- date
- category_id
- created_at

### REQUISITOS DE SEGURANÇA
1- O sistema deve evitar expor chaves, senhas e demais dados sensíveis.
2- O sistema deve utilizar o Spring Security para autenticar e autorizar os usuários.
3- O sistema deve utilizar o Spring Security para proteger as rotas da aplicação.

### IMPORTANTE
1- O sistema deve ser capaz de ser executado em uma máquina virtual.
2- O Sistema deve ser todo programado em Inglês.
3- Execute todos os testes unitários na camada de Service.

### BOAS PRÁTICAS DE ARQUITETURA
1- Evite colocar qualquer regra de negócio na camada de Controller ou Repository.
2- Uma Controller nunca deve chamar diretamente um Repository.