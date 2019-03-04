package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.thiago.robotPi.model.Empresa;

public class EmpresaAdapter extends BaseAdapter {
    private Context context;
    private List<Empresa> empresas;

    public EmpresaAdapter(Context context, List<Empresa> empresas) {
        this.context = context;
        this.empresas = empresas;
    }

    @Override
    public int getCount() {
        return empresas.size();
    }

    @Override
    public Object getItem(int i) {
        return empresas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView text = new TextView(context);
        text.setText(empresas.get(i).getNome());

        return text;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }
}
