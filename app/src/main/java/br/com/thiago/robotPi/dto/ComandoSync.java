package br.com.thiago.robotPi.dto;

import java.util.List;

import br.com.thiago.robotPi.model.Comando;
import br.com.thiago.robotPi.model.Mensagem;

public class ComandoSync {
    private List<Comando> comandos;
    private Comando comando;
    private Mensagem mensagem;


    public ComandoSync(Comando comando, Mensagem mensagem){
        this.comando = comando;
        this.mensagem = mensagem;
    }
    public List<Comando> getComandos() {
        return comandos;
    }

    public void setComandos(List<Comando> comandos) {
        this.comandos = comandos;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando comando) {
        this.comando = comando;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
}

