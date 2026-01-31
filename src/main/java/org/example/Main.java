package org.example;

import org.example.domain.Prioridade;
import org.example.domain.Status;
import org.example.domain.Tarefa;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Tarefa> tarefas = new HashSet<>();
        Scanner in = new Scanner(System.in);
        int opc = 0;
        do {
            System.out.println("\n=== TODO LIST ===");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Sair");
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
//                case 2:
//                    listarTarefas(tarefas);
//                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opc != 3);
    }

    public static void criarTarefa(Scanner in, Set<Tarefa> lista) {
        System.out.println("\n==== NOVO TAREFA ====");
        System.out.println("Digite o nome da tarefa: ");
        String nome = in.nextLine();
        System.out.println("Digite o descricao da tarefa: ");
        String descricao = in.nextLine();
        System.out.println("Categoria: ");
        String categoria = in.nextLine();

        System.out.println("Data de Início:");
        System.out.print("Dia: ");
        int diaIn = in.nextInt();
        System.out.print("Mês: ");
        int mesIn = in.nextInt();
        System.out.print("Ano: ");
        int anoIn = in.nextInt();

        System.out.println("Data de Término:");
        System.out.print("Dia: ");
        int diaFim = in.nextInt();
        System.out.print("Mês: ");
        int mesFim = in.nextInt();
        System.out.print("Ano: ");
        int anoFim = in.nextInt();
        in.nextLine();

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
                    nome,
                    descricao,
                    anoIn, mesIn, diaIn,
                    anoFim, mesFim, diaFim,
                    prioridade,
                    categoria,
                    Status.A_FAZER
            );
            lista.add(novaTarefa);
            System.out.println("Tarefa " + novaTarefa.getNome() + " adicionada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar data. Verifique se os dias/meses são válidos.");
        }
    }
}
