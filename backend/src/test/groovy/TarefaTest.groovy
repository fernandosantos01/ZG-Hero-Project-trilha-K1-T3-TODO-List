import org.example.Main
import org.example.domain.Prioridade
import org.example.domain.Status
import org.example.domain.Tarefa
import spock.lang.Specification

import java.time.LocalDateTime

class TarefaTest extends Specification {
    def "deve remover uma tarefa chamando o metodo removerTarefa da classe main"() {
        given: "uma lista contendo uma tarefa com ID 1"
        def tarefa1 = new Tarefa(1L, "Estudar groovy", "Descrição", LocalDateTime.now(), LocalDateTime.now(), Prioridade.ALTA, "Categoria", Status.A_FAZER, [])
        Set<Tarefa> lista = [tarefa1] as HashSet

        and: "um Scanner simulando o usario digitando o ID '1' e apertando Enter"
        def inputSimulado = "1\n"
        def scannerMock = new Scanner(inputSimulado)

        when: "Chamamos o metodo removerTarefda do main"
        Main.removerTarefa(scannerMock, lista)

        then: "a lista deve ficar vazia, provando que o metodo funcionou"
        lista.isEmpty()
    }

    def "deve tentar remover uma tarefa com ID inexistente"() {
        given: "instanciando tarefa"
        def tarefa1 = new Tarefa(1L, "Estudar groovy", "Descrição", LocalDateTime.now(), LocalDateTime.now(), Prioridade.ALTA, "Categoria", Status.A_FAZER, [])
        Set<Tarefa> lista = [tarefa1] as HashSet

        and: "um Scanner simulando o usario digitando o ID '99' um ID inexistente"
        def inputSimulado = "99\n"
        def scannerMock = new Scanner(inputSimulado)

        when: "Chamamos o metodo removerTarefa do main"
        Main.removerTarefa(scannerMock, lista)

        then: "a lista deve ficar vazia, provando que o metodo funcionou"
        lista.size() == 1

    }

    def "deve garantir a criacao de uma nova tarefa"() {
        given: "cria uma lista vazia"
        Set<Tarefa> tarefaSet = new HashSet<>()

        and: "um scanner com as respostas pre-digiadas para a criacao"
        String fluxoDeRespostas =
                "Estudar Spock\n" +
                        "Estudar TDD\n" +
                        "Programação\n" +
                        "18\n05\n2026\n10\n0\n" +
                        "18\n06\n2026\n10\n0\n" +
                        "N\n" +
                        "4\n"
        def scannerMock = new Scanner(fluxoDeRespostas)

        when: "chamamos o metodo do main passando as respostas simuladas"
        Main.criarTarefa(scannerMock, tarefaSet)

        then: "a lista deve conter exatamente 1 tarefa cadastrada"
        tarefaSet.size() == 1

        and: "os dados da tarefa criada devem ser os mesmos digitados"
        def tarefaCriada = tarefaSet.first()
        tarefaCriada.nome == "Estudar Spock"
        tarefaCriada.prioridade == Prioridade.ALTA
        tarefaCriada.categoria == "Programação"
    }

    def "deve atualizar uma tarefa já existente"() {
        given: "instanciar a tarefa ser atualizada e criar um Hash"
        def tarefa1 = new Tarefa(1L, "Estudar groovy", "Descrição", LocalDateTime.now(), LocalDateTime.now(), Prioridade.ALTA, "Categoria", Status.A_FAZER, [])
        Set<Tarefa> lista = [tarefa1] as HashSet

        and: "simula o usuario selecionando a tarefa que deve ser atualizada e a opção de atualizar o nome da tarefa"
        String fluxoDeRespostas =
                "1\n" +
                        "1\n" +
                        "Estudar PAA\n" +
                        "0\n"
        def scannerMock = new Scanner(fluxoDeRespostas)

        when: "atualiza a tarefa existente chamando o metodo atualizarTarefa da classe main"
        Main.atualizarTarefa(scannerMock, lista)

        then: "verifica se a tarefa realmente foi atualizada verificando o nome"
        def tarefaAtualizada = lista.first()
        tarefaAtualizada.nome == "Estudar PAA"
    }
    def "deve listar todas as tarefas no console quando o utilizador escolhe a opcao 5"() {
        given: "uma lista contendo duas tarefas distintas"
        def t1 = new Tarefa(1L, "Estudar Groovy", "Desc 1", LocalDateTime.now(), LocalDateTime.now(), Prioridade.ALTA, "Estudos", Status.A_FAZER, [])
        def t2 = new Tarefa(2L, "Passear o cao", "Desc 2", LocalDateTime.now(), LocalDateTime.now(), Prioridade.BAIXA, "Casa", Status.A_FAZER, [])
        Set<Tarefa> lista = [t1, t2] as HashSet

        and: "um scanner simulando a escolha '5' (Listar TODAS) e depois '0' (Voltar)"
        String fluxoDeRespostas = "5\n0\n"
        def scannerMock = new Scanner(fluxoDeRespostas)

        and: "redirecionamos o System.out para um stream em memoria para conseguirmos ler o que o programa imprime"
        def saidaConsoleMemoria = new ByteArrayOutputStream()
        def saidaOriginal = System.out
        System.setOut(new PrintStream(saidaConsoleMemoria))

        when: "chamamos o metodo listarTarefas da Main"
        Main.listarTarefas(scannerMock, lista)

        then: "o texto capturado no console deve conter os nomes das duas tarefas"
        String textoImpresso = saidaConsoleMemoria.toString()
        textoImpresso.contains("Estudar Groovy")
        textoImpresso.contains("Passear o cao")

        and: "o texto tambem deve conter os cabecalhos do menu"
        textoImpresso.contains("MENU DE LISTAGENS")

        cleanup: "Restaura o System.out original para nao estragar a impressao dos outros testes"
        System.setOut(saidaOriginal)
    }
//    def "deve garantir a criacao de uma nova tarefa"(){
//        given:"instancia a tarefa para a adicao"
//        def dataInicio = LocalDateTime.of(2026, 5, 18, 10, 0)
//        def dataFim = LocalDateTime.of(2026, 7, 18, 10, 0)
//        def alarmes = [15]
//        def tarefa1 = new Tarefa(1L, "Estudar", "Estudar IA", dataInicio, dataFim, Prioridade.ALTA, "Programação", Status.A_FAZER, alarmes)
//
//        and:"criando um set vazio"
//        Set<Tarefa> tarefaSet = new HashSet<>()
//
//        when:"adicionamos a tarefa no Hash"
//        tarefaSet.add(tarefa1)
//
//        then: "deve ter uma tarefa dentro do hash"
//        tarefaSet.size() == 1
//    }
//    def "deve garantir que tarefas com ID sejam considerada igauis no Hash"(){
//        given: "duas tarefas instanciadas com os mesmos dados e mesmo ID(forçando o ID 1L)"
//        def dataInicio = LocalDateTime.of(2026, 5, 18, 10, 0)
//        def dataFim = LocalDateTime.of(2026, 7, 18, 10, 0)
//        def alarmes = [15]
//
//        def tarefa1 = new Tarefa(1L, "Estudar", "Estudar IA", dataInicio, dataFim, Prioridade.ALTA, "Programação", Status.A_FAZER, alarmes)
//        def tarefa2 = new Tarefa(1L, "Estudar", "Estudar IA", dataInicio, dataFim, Prioridade.ALTA, "Programação", Status.A_FAZER, alarmes)
//
//        and: "um HashSet Vazio"
//        Set<Tarefa> tarefaSet = new HashSet<>()
//
//        when: "tentamos adicionar as duas tarefas no Set"
//        tarefaSet.add(tarefa1)
//        tarefaSet.add(tarefa2)
//
//        then: "o HashSet deve conter apenas 1 item, pois os IDs sao iguais e o Set não aceita duplicatas"
//        tarefaSet.size() == 1
//    }


}
