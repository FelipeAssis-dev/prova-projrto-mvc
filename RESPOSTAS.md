# RESPOSTAS.md — Prova Prática PetVida

**Aluno:** Felipe
**NN:** 52

## Parte A — Construção do sistema

Evidências de execução das três rotas e do H2 Console:

- Ficha do animal (`/ficha_52`): ![ficha](evidencias/FichaAnimalEvi.png)
- Ficha do tutor (`/tutor_52`): ![tutor](evidencias/FichaTutorEvi.png)
- Resumo da clínica (`/resumo_52`): ![resumo](evidencias/ResumoClinicaEvi.png)
- Console H2 com SELECT do tutor 152 e animal 152: ![h2](evidencias/EvidenciaH2AnimalTutor.png)


## Parte B — Planejamento da camada de controle

B.1 (2 a 4 linhas) Qual das duas informações que identificam uma requisição HTTP — o verbo ou a URL — o Spring usa para escolher entre os métodos ficha() e resumo() do seu Controller? Por que a outra informação sozinha não bastaria?

Seria uma combinação entre ambos, mas se for para escolher qual indica de verdade entre ficha() e resumo() é a URL. Porque no projeto ficha_52 e resumo_52 usam o mesmo verbo (GET), então só o verbo não é o suficiente para o programa saber qual seria o metodo chamar quando as duas requisições chegam. O verbo sozinho reduziria as opções a "qualquer rota GET", mas ainda existiriam várias, é a URL que aponta exatamente para um único.

## Parte C — 


D.1 — Suas duas capturas com o defeito presente: a do erro 404 (item 1, classe sem @Controller) e a do primeiro erro 500 (item 3, EmptyResultDataAccessException). Cole no RESPOSTAS.md junto com a saída de git log --oneline do seu terminal (rode esse comando agora e cole o resultado — deve mostrar, na ordem, pelo menos os commits parte-d: codigo com defeitos e depois parte-d: defeitos corrigidos).

D.2 — Rascunho de resposta:

A linha <p th:text="${animal.nome}">nome do animal</p> usa um atributo Thymeleaf (th:text), então o Thymeleaf sabe que precisa processar essa expressão e substituir o conteúdo da tag pelo valor calculado. Já a linha <p>${animal.especie}</p> não tem nenhum atributo th:* — para o Thymeleaf, isso é HTML comum, e ele nunca chega a tentar interpretar o ${...}. O texto é enviado ao navegador exatamente como está escrito no arquivo fonte. Isso revela que o Thymeleaf não processa expressões ${} "soltas" no meio do HTML: ele só ativa seu mecanismo de substituição quando encontra um atributo do seu dialeto (th:text, th:if, etc.) marcando aquele ponto do template.

D.3 — Rascunho de resposta:

O item 2 não é defeito porque a rota /consulta funcionou normalmente mesmo com @GetMapping("consulta") sem a barra inicial — confirmei isso rodando a aplicação e observando, no log em nível TRACE (habilitado pelo logging.level.web=trace do A.2), a linha Mapped to br.edu.iftm.petvida.controller.ConsultaController#consulta(Model) respondendo à requisição GET "/consulta". Isso é comportamento documentado do Spring MVC: os padrões de rota passam por normalização, que adiciona a barra / no início quando ela está ausente.

## Parte D — Análise crítica de uma resposta de IA.

E.3 — Prova documental (afirmação a)

URL: https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html

A documentação oficial do Spring Framework, na seção sobre o uso das classes JDBC Core, afirma que esse mecanismo de tradução de exceções "is used behind the common JdbcTemplate and JdbcTransactionManager entry points which do not propagate SQLException but rather DataAccessException".

Essa frase mostra que a tradução de SQLException para a hierarquia de exceções do Spring (DataAccessException) acontece dentro dos próprios métodos do JdbcTemplate  é um comportamento embutido na classe, não algo que depende de uma anotação externa. Como o TutorRepository e o AnimalRepository do meu projeto usam JdbcTemplate diretamente, eles já teriam essa tradução funcionando mesmo que a classe não estivesse anotada com @Repository.

Isso confirma que a afirmação (a) da IA está incorreta: sem @Repository, o JdbcTemplate não passaria a lançar SQLException bruta ele continuaria devolvendo DataAccessException (ou uma subclasse dela) normalmente, porque essa tradução é interna ao JdbcTemplate, e não uma função da anotação @Repository.

## PARTE E - Rastreamento...

F.2 — Onde o cálculo/formatação teria ficado na view???
## model.addAttribute("mediaIdade", String.format("%.2f", media));

No padrão MVC, a View é responsável apenas por apresentar dados, não por calculá-los ou transformá-los. Quando a formatação (como arredondar para 2 casas decimais ou formatar uma data) fica no template, a View passa a carregar lógica de negócio/apresentação que deveria estar no Controller ou em uma camada de serviço — isso viola a separação de responsabilidades do MVC. Ao proibir #numbers/#dates e exigir que tudo chegue pronto como texto, a prova força que todo cálculo e formatação aconteçam no Java (Controller), deixando a View com a única função de exibir. Isso também facilita testes: é mais fácil testar um método Java que formata uma string do que testar lógica embutida dentro de um template HTML.

F.4 — Impacto de 500 mil animais com SELECT * + cálculo em Java

Se a alternativa proibida fosse usada, cada chamada a mediaIdade(), contarAnimais() ou animalMaisVelho() exigiria trazer 500 mil linhas completas do banco para a aplicação Java através da rede (ou do driver JDBC), só para descartar quase tudo e calcular um único número. Isso significa: mais tráfego de dados entre aplicação e banco, mais uso de memória na aplicação para armazenar temporariamente 500 mil objetos, e mais tempo de resposta. Com o cálculo feito pelo SQL (como o mediaIdade() atual faz), o banco de dados processa a agregação internamente e devolve apenas um valor pela rede — independente de a tabela ter 7 ou 500 mil linhas, o tráfego de rede continua sendo mínimo.

## Parte G - 

public Animal buscarPorId(int id) {
    // Bloco 1: monta a consulta SQL com JOIN entre animal e tutor.
    // Usa aliases (a_nome, t_nome) porque as duas tabelas têm uma coluna "nome",
    // e sem o alias o ResultSet.getString("nome") pegaria sempre a primeira ocorrência.
    String sql = "SELECT a.id_animal, a.nome AS a_nome, a.especie, a.idade, "
            + "t.id_tutor, t.nome AS t_nome, t.telefone "
            + "FROM animal a JOIN tutor t ON a.tutor_id_tutor = t.id_tutor "
            + "WHERE a.id_animal = ?";

    // Bloco 2: executa a consulta parametrizada, passando "id" no lugar do "?",
    // e delega a conversão de cada linha do ResultSet em objeto Java para o
    // RowMapper (animalComTutorMapper), definido como atributo da classe.
    return jdbc.queryForObject(sql, animalComTutorMapper, id);
}
## Respondendo as perguntas

Quem chama a função (rs, rowNum) -> {...} e quantas vezes ela é executada?
Quem chama é o próprio JdbcTemplate, internamente, dentro do método queryForObject. Ele executa o SQL, recebe o ResultSet de volta do banco, e para cada linha desse ResultSet invoca a função lambda passando a linha atual (rs) e o índice dela (rowNum), esperando receber de volta um objeto Java já montado. Como a consulta usa WHERE a.id_animal = ? (busca por chave primária) combinada com um JOIN que sempre resulta em exatamente uma linha por animal, a função é executada uma única vez nesta consulta.

O que aconteceria se a consulta retornasse duas linhas em vez de uma?
O queryForObject é feito para retornar exatamente um resultado. Se o ResultSet tivesse duas (ou mais) linhas, o Spring lançaria IncorrectResultSizeDataAccessException: Incorrect result size: expected 1, actual 2 — uma exceção diferente da que já vimos na Parte C (EmptyResultDataAccessException, que é o caso de zero linhas). Ambas são subclasses de IncorrectResultSizeDataAccessException... na verdade a de zero linhas é EmptyResultDataAccessException, que estende essa; a de múltiplas linhas usa a classe base diretamente.

Por que essa consulta precisa do JOIN, se você poderia buscar o tutor depois, em outra consulta?
Poderia funcionar sem o JOIN (fazendo duas consultas: uma para o animal, outra para o tutor usando tutor_id_tutor), mas isso significaria duas idas ao banco em vez de uma — duas conexões/round-trips de rede, dois SELECTs separados. Com o JOIN, o próprio banco já junta as duas tabelas e devolve tudo em uma única linha, numa única viagem entre aplicação e banco. Além disso, a própria prova exige explicitamente "UMA única consulta SQL com JOIN" no quadro da A.4 — então também é um requisito direto do enunciado, não só uma otimização.

## G2
 -- Sinceramente boa parte de todo esse projeto ainda tenho muita dificuldade em aplicar, principalmente em relação ao uso das propriedades  partes com @, não fixarão tão bem ora mim, pois acredito que aprendi bem a teoria da materia mas ainda peco na pratica. mas para deixar um codigo especifico aqui: public class PetvidaApplication implements CommandLineRunner {

    private final TutorRepository tutorRepository;
    private final AnimalRepository animalRepository;

    public PetvidaApplication(TutorRepository tutorRepository, AnimalRepository animalRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(PetvidaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // tutores da Seção 2 — gravados antes dos animais (FK exige que o tutor já exista)
        Tutor marina = new Tutor(1, "Marina Alves", "34 99101-0001");
        Tutor carlos = new Tutor(2, "Carlos Prado", "34 99101-0002");
        tutorRepository.salvar(marina);
        tutorRepository.salvar(carlos);

        // seu tutor (semente NN=52)
        Tutor felipe = new Tutor(152, "Felipe", "34 95252-5252");
        tutorRepository.salvar(felipe);

        // animais da Seção 2
        animalRepository.salvar(new Animal(2, "Mimi", "gato", 3, marina));
        animalRepository.salvar(new Animal(3, "Thor", "cao", 1, carlos));
        animalRepository.salvar(new Animal(4, "Lila", "gato", 11, carlos));

        // seu animal (semente NN=52)
        animalRepository.salvar(new Animal(152, "Pet_52", "cao", 52, felipe));
    }
}

Acho o uso do Respository bem complicado, entendo a sintaxe de SQL, mas o resto fico bem perdido!
