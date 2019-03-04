package br.com.thiago.robotPi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.EnumMap;

import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.User;

public class EmpresaDAO extends SQLiteOpenHelper {
    public EmpresaDAO(Context context) {
        super(context, "RobotPi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Empresa (id VARCHAR(255) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "cep TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "CREATE TABLE Empresa (id VARCHAR(255) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "cep TEXT NOT NULL);";
                db.execSQL(sql);
        }

    }

    public boolean insere(Empresa empresa) {
        SQLiteDatabase db = getWritableDatabase();
        if(existeEmpresa(empresa)){
            return false;
        }else {
            ContentValues dados = pegaDadosDaEmpresa(empresa);
            db.insert("Empresa", null, dados);
            return true;
        }
    }

    public boolean existeEmpresa(Empresa empresa){
        SQLiteDatabase db = getReadableDatabase();
        String existe = "SELECT id FROM Empresa WHERE id = ? LIMIT 1";
        Cursor cursor = db.rawQuery(existe, new String[]{empresa.getId()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
    }

    @NonNull
    private ContentValues pegaDadosDaEmpresa(Empresa empresa) {
        ContentValues dados = new ContentValues();
        System.out.println(empresa.getId());
        dados.put("id", empresa.getId());
        dados.put("nome", empresa.getNome());
        dados.put("cep", empresa.getCep());
        return dados;
    }

    public Empresa buscaEmpresa() {
        String sql = "SELECT * FROM Empresa";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        Empresa empresa = populaEmpresa(c);
        c.close();

        return empresa;
    }

    public void deleta(Empresa empresa) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {empresa.getId()};
        db.delete("Empresa", "id = ?", params);
    }


   private Empresa populaEmpresa(Cursor c) {
        Empresa empresa = new Empresa();
        while(c.moveToNext()) {
            empresa.setId(c.getString(c.getColumnIndex("id")));
            empresa.setNome(c.getString(c.getColumnIndex("nome")));
            empresa.setCep(c.getString(c.getColumnIndex("cep")));
        }

        return empresa;
    }


}
