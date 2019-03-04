package br.com.thiago.robotPi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.User;

public class DispositivoDAO extends SQLiteOpenHelper {
    private Context context;
    public DispositivoDAO(Context context) {

        super(context, "RobotPi", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Empresa (id VARCHAR(255) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "cep TEXT NOT NULL);";
        String sql1 = "CREATE TABLE User (id VARCHAR(255) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "telefone TEXT NOT NULL, " +
                "idEmpresa TEXT NOT NULL, " +
                "caminhoFoto TEXT, " +
                "desativado INT DEFAULT 0);";
        String sql2 = "CREATE TABLE Dispositivo (token VARCHAR(255) PRIMARY KEY);";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "CREATE TABLE Dispositivo (token VARCHAR(255) PRIMARY KEY);";
                db.execSQL(sql);
        }

    }

    public void salvaDispositivo(String token){
        Dispositivo dispositivo = new Dispositivo(token);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDoDispositivo(dispositivo);
        db.insert("Dispositivo", null, dados);

    }

    @NonNull
    private ContentValues pegaDadosDoDispositivo(Dispositivo dispositivo) {
        ContentValues dados = new ContentValues();
        dados.put("token", dispositivo.getToken());
        return dados;
    }

    public Dispositivo getDispositivo(){
        String sql = "SELECT * FROM Dispositivo";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);
        return populaDispositivo(c);
    }

    private Dispositivo populaDispositivo(Cursor c) {
        Dispositivo dispositivo = new Dispositivo();
        while (c.moveToNext()) {
            dispositivo.setToken(c.getString(c.getColumnIndex("token")));
            dispositivo.setUser(buscaUser());
        }
        return dispositivo;
    }

    public User buscaUser(){
        UserDAO dao = new UserDAO(this.context);
        return dao.buscaUser();
    }

    public String buscaDispositivo() {
        String sql = "SELECT * FROM Dispositivo";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);

        String token = getToken(c);
        c.close();

        return token;
    }

    public String getToken(Cursor c){
        String token="";
        while(c.moveToNext()) {
            token = c.getString(c.getColumnIndex("token"));
        }
        return token;
    }

}
