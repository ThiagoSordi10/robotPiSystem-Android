package br.com.thiago.robotPi.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.helper.UsuarioHelper;

@JsonIgnoreProperties(ignoreUnknown = true)//Dados a mais vindo do server não são sincronizados
public class Estacao implements Serializable, Comparable<Estacao> {

    private String id;
    private String nome;
    private Empresa empresa;
    private int posicao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int compareTo(@NonNull Estacao estacao) {
        return this.getNome().compareTo(estacao.getNome());
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
