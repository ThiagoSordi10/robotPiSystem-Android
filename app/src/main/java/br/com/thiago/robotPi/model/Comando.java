package br.com.thiago.robotPi.model;

import android.util.Log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.TimeZone;

import br.com.thiago.robotPi.config.ConfiguracaoFirebase;


public class Comando implements Serializable {

    private String id;
    private int executado;
    private Estacao estacaoSaida;
    private Estacao estacaoChegada;
    private User user;
    private Raspberry raspberry;
    private Date horarioSaida;
    private Date horarioChegada;


    public Date getHorarioChegada() {
        return horarioChegada;
    }

    public void setHorarioChegada(Date horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public Date getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(Date horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public Raspberry getRaspberry() {
        return raspberry;
    }

    public void setRaspberry(Raspberry raspberry) {
        this.raspberry = raspberry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Estacao getEstacaoChegada() {
        return estacaoChegada;
    }

    public void setEstacaoChegada(Estacao estacaoChegada) {
        this.estacaoChegada = estacaoChegada;
    }

    public Estacao getEstacaoSaida() {
        return estacaoSaida;
    }

    public void setEstacaoSaida(Estacao estacaoSaida) {
        this.estacaoSaida = estacaoSaida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.estacaoSaida.getNome()+" ----> "+this.estacaoChegada.getNome();
    }

    public String dataString(){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String data = formater.format(this.getHorarioSaida());
        return data;
    }

    public int getExecutado() {
        return executado;
    }

    public void setExecutado(int executado) {
        this.executado = executado;
    }
}
