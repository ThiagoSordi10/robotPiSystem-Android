package br.com.thiago.robotPi.event;

import br.com.thiago.robotPi.dto.MensagemSync;
import br.com.thiago.robotPi.dto.RaspberrySync;

public class AtualizaRaspberriesEvent {
    private RaspberrySync raspberrySync;

    public AtualizaRaspberriesEvent(RaspberrySync raspberrySync){
        this.raspberrySync = raspberrySync;
    }

    public RaspberrySync getRaspberrySync() {
        return raspberrySync;
    }

    public void setRaspberrySync(RaspberrySync raspberrySync) {
        this.raspberrySync = raspberrySync;
    }

}
