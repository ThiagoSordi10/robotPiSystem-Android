package br.com.thiago.robotPi.dto;

import java.util.List;

import br.com.thiago.robotPi.model.Estacao;

public class EstacaoSync {
    private List<Estacao> estacoes;
    private Estacao Estacao;


    public List<Estacao> getEstacoes() {
        return estacoes;
    }

    public void setEstacoes(List<Estacao> estacoes) {
        this.estacoes = estacoes;
    }

    public Estacao getEstacao() {
        return Estacao;
    }

    public void setEstacao(Estacao Estacao) {
        this.Estacao = Estacao;
    }
}

