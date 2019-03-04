package br.com.thiago.robotPi.dto;

import java.util.List;

import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.User;

public class EmpresaSync {
    private List<Empresa> empresas;
    private Empresa empresa;
    //private String momentoDaUltimaModificacao;


    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}

