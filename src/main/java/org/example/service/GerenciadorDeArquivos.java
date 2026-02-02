package org.example.service;

import org.example.domain.Prioridade;
import org.example.domain.Status;
import org.example.domain.Tarefa;
import org.example.expeption.ErroAoCarregarArquivoException;
import org.example.expeption.ErroAoSalvarArquivoException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GerenciadorDeArquivos {
    private static final String NOME_ARQUIVO = "src/main/resources/tarefas_db.text";

    public static void salvarTarefas(Set<Tarefa> tarefas) throws ErroAoSalvarArquivoException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Tarefa tarefa : tarefas) {
                bw.write(tarefa.paraArquivo());
                bw.newLine();
            }
            System.out.println("Dados salvos com sucesso em: " + NOME_ARQUIVO);
        } catch (Exception e) {
            throw new ErroAoSalvarArquivoException("Falha crítica ao tentar gravar no disco.", e);
        }
    }

    public static Set<Tarefa> carregarTarefas() throws ErroAoCarregarArquivoException {
        Set<Tarefa> tarefas = new HashSet<>();
        File file = new File(NOME_ARQUIVO);

        if (!file.exists()) {
            return tarefas;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] linhas = linha.split(";");
                if (linhas.length < 8) continue;
                // Reconstrói o objeto (Atenção à ordem dos campos no paraArquivo)
                // 0:id, 1:nome, 2:desc, 3:dtIni, 4:dtFim, 5:prio, 6:cat, 7:status
                Long id = Long.parseLong(linhas[0]);
                String nome = linhas[1];
                String desc = linhas[2];
                LocalDateTime dtIni = LocalDateTime.parse(linhas[3]);
                LocalDateTime dtFim = LocalDateTime.parse(linhas[4]);
                Prioridade prio = Prioridade.valueOf(linhas[5]);
                String cat = linhas[6];
                Status status = Status.valueOf(linhas[7]);

                List<Integer> alarmes = new ArrayList<>();
                if (linhas.length > 8 && !linhas[8].equals("sem_alarme")) {
                    String[] alarmesStr = linhas[8].split("\\|");
                    for (String s : alarmesStr) {
                        try {
                            alarmes.add(Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            // ignora alarme corrompido
                        }
                    }
                }

                Tarefa t = new Tarefa(id, nome, desc, dtIni, dtFim, prio, cat, status, alarmes);
                tarefas.add(t);
            }
        } catch (Exception e) {
            throw new ErroAoCarregarArquivoException("Falha crítica ao tentar carregar arquivo.", e);
        }
        return tarefas;
    }

}
