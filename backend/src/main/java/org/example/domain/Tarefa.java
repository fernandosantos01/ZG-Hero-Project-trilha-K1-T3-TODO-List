package org.example.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class Tarefa {
    private static Long contadorId = 1L;
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Prioridade prioridade;
    private String categoria;
    private Status status;
    private List<Integer> alarmes = new ArrayList<>();

    public Tarefa(String nome, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim,
                  Prioridade prioridade, String categoria, Status status, List<Integer> alarmes) {
        this.id = GenerateValueId();
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
        this.alarmes = (alarmes != null) ? alarmes : new ArrayList<>();
    }

    public Tarefa(Long id, String nome, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, Prioridade prioridade, String categoria, Status status, List<Integer> alarmes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
        this.alarmes = (alarmes != null) ? alarmes : new ArrayList<>();
    }

    private Long GenerateValueId() {
        return contadorId++;
    }

    public String paraArquivo() {
        String alarmesString = alarmes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        if (alarmesString.isEmpty()) alarmesString = "sem_alarme";
        return  id + ";" +
                nome + ";" +
                descricao + ";" +
                dataInicio + ";" +
                dataFim + ";" +
                prioridade + ";" +
                categoria + ";" +
                status + ";" +
                alarmesString;
    }

    @Override
    public String toString() {
        return "\n------Tarefa-------" +
                "\nID = " + id +
                "\nNome = " + nome +
                "\nDescricao = " + descricao +
                "\nData Inicio = " + dataInicio +
                "\nData Fim = " + dataFim +
                "\nPrioridade = " + prioridade +
                "\nCategoria = " + categoria +
                "\nStatus = " + status;
    }

    public static void atualizarContador(Set<Tarefa> tarefas) {
        long maiorId = 0;
        for (Tarefa t : tarefas) {
            if (t.getId() > maiorId) maiorId = t.getId();
        }
        contadorId = maiorId + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public List<Integer> getAlarmes() {
        return alarmes;
    }

    public void setAlarmes(List<Integer> alarmes) {
        this.alarmes = alarmes;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
