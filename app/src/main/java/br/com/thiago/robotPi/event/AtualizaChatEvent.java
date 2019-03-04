package br.com.thiago.robotPi.event;

import br.com.thiago.robotPi.model.Mensagem;

public class AtualizaChatEvent {
    private Mensagem mensagem;

    public AtualizaChatEvent(Mensagem mensagem){
        this.mensagem = mensagem;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
}
