package br.com.thiago.robotPi.dto;

import java.util.List;

import br.com.thiago.robotPi.model.Raspberry;
import br.com.thiago.robotPi.model.User;

public class RaspberrySync {
    private List<Raspberry> raspberries;
    private Raspberry raspberry;


    public List<Raspberry> getRaspberries() {
        return raspberries;
    }

    public void setRaspberries(List<Raspberry> raspberries) {
        this.raspberries = raspberries;
    }

    public Raspberry getRaspberry() {
        return raspberry;
    }

    public void setRaspberry(Raspberry raspberry) {
        this.raspberry = raspberry;
    }
}

