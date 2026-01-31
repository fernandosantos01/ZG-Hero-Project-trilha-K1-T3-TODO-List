package org.example.domain;

import java.time.LocalDate;
import java.util.Objects;


public class Tarefa {
    private static Long contadorId = 1L;
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Prioridade prioridade;
    private String categoria;
    private Status status;

    public Tarefa(String nome, String descricao, int anoInicio, int mesInicio, int diaInicio, int anoFim, int mesFim, int diaFIm, Prioridade prioridade, String categoria, Status status) {
        this.id = GenerateValueId();
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = LocalDate.of(anoInicio, mesInicio, diaInicio);
        this.dataFim = LocalDate.of(anoFim, mesFim, diaFIm);
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
    }


    private Long GenerateValueId() {
        return contadorId++;
        //Ler o txt e para ver a posição do ID
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
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
