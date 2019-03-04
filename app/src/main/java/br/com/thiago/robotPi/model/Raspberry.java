package br.com.thiago.robotPi.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.thiago.robotPi.config.ConfiguracaoFirebase;


public class Raspberry implements Serializable, Comparable<Raspberry>{

    private String id;
    private String nome;
    private Empresa empresa;
    private Dispositivo dispositivo;
    private Estacao estacao;
    private String endereco;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Estacao getEstacao() {
        return estacao;
    }

    public void setEstacao(Estacao estacao) {
        this.estacao = estacao;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public int compareTo(@NonNull Raspberry raspberry) {
        return this.getNome().compareTo(raspberry.getNome());
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
