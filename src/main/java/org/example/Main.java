package org.example;

import org.example.domain.Prioridade;
import org.example.domain.Status;
import org.example.domain.Tarefa;
import org.example.expeption.ErroAoCarregarArquivoException;
import org.example.expeption.ErroAoSalvarArquivoException;
import org.example.service.GerenciadorDeArquivos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<Tarefa> tarefas = new HashSet<>();
        try {
            System.out.println("Carregando tarefas...");
            tarefas = GerenciadorDeArquivos.carregarTarefas();
            Tarefa.atualizarContador(tarefas);
            System.out.println("Sistema iniciado. Tarefas carregadas: " + tarefas.size());
        } catch (ErroAoCarregarArquivoException e) {
            System.err.println("ATENÇÃO: Falha ao carregar tarefas antigas.");
            System.err.println("Motivo: " + e.getMessage());
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);
        int opc = 0;
        do {
            System.out.println("\n=== TODO LIST ===");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Atualizar Tarefa");
            System.out.println("4. Remover Tarefa");
            System.out.println("5. Relatório de Status");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            if (in.hasNextInt()) {
                opc = in.nextInt();
                in.nextLine();
            } else {
                in.nextLine();
                opc = -1;
            }
            switch (opc) {
                case 1:
                    criarTarefa(in, tarefas);
                    break;
                case 2:
                    listarTarefas(in, tarefas);
                    break;
                case 3:
                    atualizarTarefa(in, tarefas);
                    break;
                case 4:
                    removerTarefa(in, tarefas);
                    break;
                case 5:
                    consultarStatusTarefas(tarefas);
                    break;
                case 6:
                    System.out.println("Saindo e Salvando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opc != 6);
        try {
            GerenciadorDeArquivos.salvarTarefas(tarefas);
        } catch (ErroAoSalvarArquivoException e) {
            System.err.println("ERRO GRAVE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void criarTarefa(Scanner in, Set<Tarefa> lista) {
        System.out.println("\n==== NOVO TAREFA ====");
        System.out.println("Digite o nome da tarefa: ");
        String nome = limparTexto(in.nextLine());
        System.out.println("Digite o descricao da tarefa: ");
        String descricao = limparTexto(in.nextLine());
        System.out.println("Categoria: ");
        String categoria = limparTexto(in.nextLine());

        LocalDateTime dataInicio = null;
        while (dataInicio == null) {
            try {
                System.out.println("Data de Início:");
                System.out.print("Dia: ");
                int diaIn = in.nextInt();
                System.out.print("Mês: ");
                int mesIn = in.nextInt();
                System.out.print("Ano: ");
                int anoIn = in.nextInt();
                System.out.print("Hora (0-23): ");
                int hI = in.nextInt();
                System.out.print("Minuto (0-59): ");
                int minI = in.nextInt();
                dataInicio = LocalDateTime.of(anoIn, mesIn, diaIn, hI, minI);
            } catch (Exception e) {
                System.out.println("Data inválida (ex: mês 13 ou dia 32). Tente novamente.");
                in.nextLine();
            }
        }

        LocalDateTime dataFim = null;
        while (dataFim == null) {
            try {
                System.out.println("Data de Término : ");
                System.out.print("Dia: ");
                int diaFim = in.nextInt();
                System.out.print("Mês: ");
                int mesFim = in.nextInt();
                System.out.print("Ano: ");
                int anoFim = in.nextInt();
                System.out.print("Hora (0-23): ");
                int hF = in.nextInt();
                System.out.print("Minuto (0-59): ");
                int minF = in.nextInt();
                dataFim = LocalDateTime.of(anoFim, mesFim, diaFim, hF, minF);
                in.nextLine();

                LocalDateTime tentativaFim = LocalDateTime.of(anoFim, mesFim, diaFim, hF, minF);

                if (tentativaFim.isBefore(dataInicio)) {
                    System.out.println("ERRO: A data de término não pode ser anterior ao início (" + dataInicio + ")");
                    System.out.println("Por favor, digite uma data válida.");
                } else {
                    dataFim = tentativaFim;
                }

            } catch (Exception e) {
                System.out.println("Data inválida. Tente novamente.");
                in.nextLine();
            }
        }

        List<Integer> alarmes = new ArrayList<>();
        System.out.print("Deseja adicionar alarmes para esta tarefa? (S/N): ");
        String resp = in.nextLine();

        if (resp.equalsIgnoreCase("S")) {
            while (true) {
                System.out.println("Com quanta antecedência deseja ser avisado?");
                System.out.println("1. 15 minutos antes");
                System.out.println("2. 1 hora antes");
                System.out.println("3. 1 dia antes");
                System.out.println("4. Personalizado (minutos)");
                System.out.println("0. Parar de adicionar alarmes");
                System.out.print("Opção: ");

                int opAlarme = in.nextInt();
                in.nextLine();

                if (opAlarme == 0) break;

                switch (opAlarme) {
                    case 1:
                        alarmes.add(15);
                        break;
                    case 2:
                        alarmes.add(60);
                        break;
                    case 3:
                        alarmes.add(1440);
                        break;
                    case 4:
                        System.out.print("Digite os minutos: ");
                        alarmes.add(in.nextInt());
                        in.nextLine();
                        break;
                }
                System.out.println("Alarme adicionado! Adicionar outro? (S/N): ");
                if (!in.nextLine().equalsIgnoreCase("S")) break;
            }
        }

        Prioridade prioridade = null;
        while (prioridade == null) {
            System.out.println("Prioridade(MINIMA(1), BAIXA(2), MEDIA(3), ALTA(4),CRITICA(5)");
            if (in.hasNextInt()) {
                int prioridadeOpc = in.nextInt();
                in.nextLine();
                prioridade = switch (prioridadeOpc) {
                    case 1 -> Prioridade.MINIMA;
                    case 2 -> Prioridade.BAIXA;
                    case 3 -> Prioridade.MEDIA;
                    case 4 -> Prioridade.ALTA;
                    case 5 -> Prioridade.CRITICA;
                    default -> {
                        System.out.println("Valor inválido! Escolha um número entre 1 e 5.");
                        yield null;
                    }
                };
            } else {
                System.out.println("Entrada inválida! Digite apenas números.");
                in.nextLine();
            }
        }
        try {
            Tarefa novaTarefa = new Tarefa(
                    nome, descricao, dataInicio, dataFim, prioridade, categoria, Status.A_FAZER, alarmes
            );

            if (!lista.add(novaTarefa)) {
                System.out.println("Erro: Tarefa duplicada (mesmo ID).");
            }
            System.out.println("Tarefa " + novaTarefa.getNome() + " adicionada com sucesso!");
        } catch (Exception e) {
            System.out.println("\"Erro ao criar tarefa: \" + e.getMessage()");
        }
    }

    public static void listarTarefas(Scanner in, Set<Tarefa> lista) {
        int opc = 0;
        if (lista.isEmpty()) {
            System.out.println("\n[AVISO] A lista de tarefas está vazia.");
            return;
        }

        do {
            System.out.println("\n====== MENU DE LISTAGENS ======");
            System.out.println("1. Listar por Categoria");
            System.out.println("2. Listar por Prioridade");
            System.out.println("3. Listar por Status");
            System.out.println("4. Listar por Data");
            System.out.println("5. Listar TODAS (Ordenadas por Prioridade)");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            if (in.hasNextInt()) {
                opc = in.nextInt();
                in.nextLine(); // Limpar buffer

                switch (opc) {
                    case 1:
                        System.out.print("Digite a categoria que deseja buscar: ");
                        String cat = in.nextLine();
                        listarTarefaPorCategoria(cat, lista);
                        break;
                    case 2:
                        System.out.print("Digite a prioridade (1-5): ");
                        if (in.hasNextInt()) {
                            int prio = in.nextInt();
                            in.nextLine();
                            listarTarefaPorPrioridade(prio, lista);
                        } else {
                            in.nextLine();
                            System.out.println("Prioridade inválida.");
                        }
                        break;
                    case 3:
                        System.out.print("Digite o status (1-A Fazer, 2-Em Andamento, 3-Concluído): ");
                        if (in.hasNextInt()) {
                            int stat = in.nextInt();
                            in.nextLine();
                            listarTarefaPorStatus(stat, lista);
                        }
                        break;
                    case 4:
                        listarTarefaPorData(in, lista);
                        break;
                    case 5:
                        listarTodasPorPrioridade(lista);
                    case 0:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } else {
                in.nextLine();
                System.out.println("Digite um número válido.");
            }

        } while (opc != 0);
    }

    private static void listarTarefaPorCategoria(String categoriaBuscada, Set<Tarefa> lista) {
        System.out.println("\n--- Filtrando por Categoria: " + categoriaBuscada + " ---");
        boolean encontrou = false;

        for (Tarefa t : lista) {
            if (t.getCategoria().equalsIgnoreCase(categoriaBuscada)) {
                System.out.println(t);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma tarefa encontrada nesta categoria.");
        }
    }

    private static void listarTarefaPorPrioridade(int nivelPrioridade, Set<Tarefa> lista) {
        Prioridade prioridadeBuscada = switch (nivelPrioridade) {
            case 1 -> Prioridade.MINIMA;
            case 2 -> Prioridade.BAIXA;
            case 3 -> Prioridade.MEDIA;
            case 4 -> Prioridade.ALTA;
            case 5 -> Prioridade.CRITICA;
            default -> null;
        };

        if (prioridadeBuscada == null) {
            System.out.println("Nível de prioridade inválido.");
            return;
        }

        System.out.println("\n--- Filtrando por Prioridade: " + prioridadeBuscada + " ---");
        boolean encontrou = false;

        for (Tarefa t : lista) {
            if (t.getPrioridade() == prioridadeBuscada) {
                System.out.println(t);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma tarefa com esta prioridade.");
        }
    }

    private static void listarTarefaPorData(Scanner in, Set<Tarefa> lista) {
        System.out.println("\n--- Filtrar por Data de Início ---");
        System.out.print("Dia: ");
        int dia = in.nextInt();
        System.out.print("Mês: ");
        int mes = in.nextInt();
        System.out.print("Ano: ");
        int ano = in.nextInt();
        in.nextLine();

        try {
            //Revisar codigo
            LocalDateTime dataBuscada = LocalDateTime.of(ano, mes, dia, 0, 0, 0);
            boolean encontrou = false;

            System.out.println("\nTarefas iniciadas em " + dataBuscada + ":");
            for (Tarefa t : lista) {
                if (t.getDataInicio().isEqual(dataBuscada)) {
                    System.out.println(t);
                    encontrou = true;
                }
            }

            if (!encontrou) {
                System.out.println("Nenhuma tarefa encontrada para esta data.");
            }
        } catch (Exception e) {
            System.out.println("Data inválida digitada.");
        }
    }

    private static void listarTarefaPorStatus(int opcaoStatus, Set<Tarefa> lista) {
        Status statusBuscado = switch (opcaoStatus) {
            case 1 -> Status.A_FAZER;
            case 2 -> Status.EM_ANDAMENTO;
            case 3 -> Status.CONCLUIDO;
            default -> null;
        };

        if (statusBuscado == null) {
            System.out.println("Status inválido.");
            return;
        }

        System.out.println("\n--- Filtrando por Status: " + statusBuscado + " ---");
        boolean encontrou = false;

        for (Tarefa t : lista) {
            if (t.getStatus() == statusBuscado) {
                System.out.println(t);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma tarefa com este status.");
        }
    }
    private static void listarTodasPorPrioridade(Set<Tarefa> tarefas) {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }
        List<Tarefa> listaOrdenada = new ArrayList<>(tarefas);

        listaOrdenada.sort((t1, t2) -> t2.getPrioridade().compareTo(t1.getPrioridade()));

        System.out.println("\n=== TODAS AS TAREFAS (Ordem de Prioridade: CRÍTICA -> MÍNIMA) ===");
        for (Tarefa t : listaOrdenada) {
            System.out.println(t);
        }
        System.out.println("------------------------------------------------------------------");
    }

    private static void consultarStatusTarefas(Set<Tarefa> lista) {
        System.out.println("\n==== RELATÓRIO DE STATUS ====");

        int contarTodo = 0;
        int contarDoing = 0;
        int contarDone = 0;

        for (Tarefa t : lista) {
            switch (t.getStatus()) {
                case A_FAZER:
                    contarTodo++;
                    break;
                case EM_ANDAMENTO:
                    contarDoing++;
                    break;
                case CONCLUIDO:
                    contarDone++;
                    break;
            }
        }

        System.out.println("Total de Tarefas: " + lista.size());
        System.out.println("-----------------------------");
        System.out.println("🔴 Para Fazer (To Do): " + contarTodo);
        System.out.println("🟡 Fazendo (Doing):    " + contarDoing);
        System.out.println("🟢 Concluídas (Done):  " + contarDone);
        System.out.println("-----------------------------");
    }

    public static void atualizarTarefa(Scanner in, Set<Tarefa> lista) {
        System.out.println("\n==== ATUALIZAR TAREFA ====");
        System.out.print("Digite o ID da tarefa que deseja alterar: ");

        if (!in.hasNextLong()) {
            System.out.println("ID inválido. Digite um número.");
            in.nextLine();
            return;
        }
        Long idBuscado = in.nextLong();
        in.nextLine();

        Tarefa tarefaEncontrada = null;
        for (Tarefa t : lista) {
            if (t.getId().equals(idBuscado)) {
                tarefaEncontrada = t;
                break;
            }
        }

        if (tarefaEncontrada == null) {
            System.out.println("Nenhuma tarefa encontrada com o ID " + idBuscado);
            return;
        }

        int opc = 0;
        do {
            System.out.println("\nEditando: " + tarefaEncontrada.getNome());
            System.out.println("1. Editar Nome");
            System.out.println("2. Editar Descrição");
            System.out.println("3. Editar Categoria");
            System.out.println("4. Editar Prioridade");
            System.out.println("5. Editar Status");
            System.out.println("6. Editar Datas e Horários");
            System.out.println("0. Finalizar Edição");
            System.out.print("O que deseja alterar? ");

            if (in.hasNextInt()) {
                opc = in.nextInt();
                in.nextLine();

                switch (opc) {
                    case 1:
                        System.out.print("Novo Nome: ");
                        tarefaEncontrada.setNome(limparTexto(in.nextLine()));
                        System.out.println("Nome atualizado!");
                        break;
                    case 2:
                        System.out.print("Nova Descrição: ");
                        tarefaEncontrada.setDescricao(limparTexto(in.nextLine()));
                        System.out.println("Descrição atualizada!");
                        break;
                    case 3:
                        System.out.print("Nova Categoria: ");
                        tarefaEncontrada.setCategoria(limparTexto(in.nextLine()));
                        System.out.println("Categoria atualizada!");
                        break;
                    case 4:
                        System.out.println("Nova Prioridade (1-MINIMA, 2-BAIXA, 3-MEDIA, 4-ALTA, 5-CRITICA): ");
                        if (in.hasNextInt()) {
                            int prio = in.nextInt();
                            in.nextLine();
                            Prioridade novaPrio = switch (prio) {
                                case 1 -> Prioridade.MINIMA;
                                case 2 -> Prioridade.BAIXA;
                                case 3 -> Prioridade.MEDIA;
                                case 4 -> Prioridade.ALTA;
                                case 5 -> Prioridade.CRITICA;
                                default -> Prioridade.MEDIA;
                            };
                            tarefaEncontrada.setPrioridade(novaPrio);
                            System.out.println("Prioridade alterada para " + novaPrio);
                        } else {
                            in.nextLine();
                            System.out.println("Prioridade inválida.");
                        }
                        break;
                    case 5:
                        System.out.println("Novo Status (1-A FAZER, 2-EM ANDAMENTO, 3-CONCLUIDO): ");
                        if (in.hasNextInt()) {
                            int stat = in.nextInt();
                            in.nextLine();
                            Status novoStatus = switch (stat) {
                                case 1 -> Status.A_FAZER;
                                case 2 -> Status.EM_ANDAMENTO;
                                case 3 -> Status.CONCLUIDO;
                                default -> Status.A_FAZER;
                            };
                            tarefaEncontrada.setStatus(novoStatus);
                            System.out.println("Status alterado para " + novoStatus);
                        } else {
                            in.nextLine();
                            System.out.println("Status inválido.");
                        }
                        break;

                    case 6:
                        System.out.println("=== Alterando Datas e Horários ===");

                        LocalDateTime novoInicio = null;
                        while (novoInicio == null) {
                            try {
                                System.out.println("Nova Data de Início:");
                                System.out.print("Dia: "); int d = in.nextInt();
                                System.out.print("Mês: "); int m = in.nextInt();
                                System.out.print("Ano: "); int a = in.nextInt();
                                System.out.print("Hora (0-23): "); int h = in.nextInt();
                                System.out.print("Minuto (0-59): "); int min = in.nextInt();

                                novoInicio = LocalDateTime.of(a, m, d, h, min);
                            } catch (Exception e) {
                                System.out.println("Data/Hora inválida. Tente novamente.");
                                in.nextLine();
                            }
                        }

                        LocalDateTime novoFim = null;
                        while (novoFim == null) {
                            try {
                                System.out.println("Nova Data de Término:");
                                System.out.print("Dia: "); int d = in.nextInt();
                                System.out.print("Mês: "); int m = in.nextInt();
                                System.out.print("Ano: "); int a = in.nextInt();
                                System.out.print("Hora (0-23): "); int h = in.nextInt();     // Novo
                                System.out.print("Minuto (0-59): "); int min = in.nextInt(); // Novo

                                LocalDateTime tempFim = LocalDateTime.of(a, m, d, h, min);

                                if (tempFim.isBefore(novoInicio)) {
                                    System.out.println("Erro: Data de término deve ser posterior ao início (" + novoInicio + ")");
                                } else {
                                    novoFim = tempFim;
                                }
                            } catch (Exception e) {
                                System.out.println("Data/Hora inválida.");
                                in.nextLine();
                            }
                        }
                        in.nextLine();

                        tarefaEncontrada.setDataInicio(novoInicio);
                        tarefaEncontrada.setDataFim(novoFim);
                        System.out.println("Datas atualizadas com sucesso!");
                        break;

                    default:
                        System.out.println("Opção desconhecida.");
                }
            } else {
                in.nextLine();
                System.out.println("Digite um número válido.");
            }
        } while (opc != 0);
    }

    public static void removerTarefa(Scanner in, Set<Tarefa> lista) {
        System.out.println("\n==== REMOVER TAREFA ====");
        System.out.print("Digite o ID da tarefa para excluir: ");

        if (in.hasNextLong()) {
            Long idAlvo = in.nextLong();
            in.nextLine();

            boolean removeu = lista.removeIf(tarefa -> tarefa.getId().equals(idAlvo));

            if (removeu) {
                System.out.println("Tarefa (ID " + idAlvo + ") removida com sucesso!");
            } else {
                System.out.println("Nenhuma tarefa encontrada com este ID.");
            }
        } else {
            in.nextLine();
            System.out.println("ID inválido.");
        }
    }

    private static void verificarAlarmes(Set<Tarefa> tarefas) {
        System.out.println("Verificando alarmes...");
        LocalDateTime agora = LocalDateTime.now();
        boolean temAlerta = false;

        for (Tarefa t : tarefas) {
            if (t.getStatus() == Status.CONCLUIDO) continue;

            List<Integer> alarmes = t.getAlarmes();
            for (Integer minutosAntes : alarmes) {
                LocalDateTime horarioDisparo = t.getDataFim().minusMinutes(minutosAntes);

                if (agora.isAfter(horarioDisparo) && agora.isBefore(t.getDataFim())) {
                    System.out.println("\n🔔 [ALARME DISPARADO] 🔔");
                    System.out.println("Tarefa: " + t.getNome());
                    System.out.println("Prazo final: " + t.getDataFim());
                    System.out.println("Aviso configurado para: " + minutosAntes + " minutos antes.");

                    long horasRestantes = java.time.Duration.between(agora, t.getDataFim()).toHours();
                    System.out.println("⚠️ Faltam aproximadamente " + horasRestantes + " horas para o término!\n");
                    temAlerta = true;
                    break;
                }
            }
        }

        if (!temAlerta) {
            System.out.println("Nenhum alarme ativo no momento.");
        }
        System.out.println("--------------------------------\n");
    }

    private static String limparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace(";", " -").trim();
    }

}
