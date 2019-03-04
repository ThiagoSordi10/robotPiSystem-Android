package br.com.thiago.robotPi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.User;

public class UserDAO extends SQLiteOpenHelper {
    private Context context;
    public UserDAO(Context context) {

        super(context, "RobotPi", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE User (id VARCHAR(255) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "telefone TEXT NOT NULL, " +
                "idEmpresa TEXT NOT NULL, " +
                "desativado INT DEFAULT 0);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "CREATE TABLE User (id VARCHAR(255) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "senha TEXT NOT NULL, " +
                        "email TEXT NOT NULL, " +
                        "telefone TEXT NOT NULL, " +
                        "idEmpresa TEXT NOT NULL, " +
                        "desativado INT DEFAULT 0);";
                db.execSQL(sql);
        }

    }

    public boolean insere(User user) {
        SQLiteDatabase db = getWritableDatabase();
        if(existeUser(user)){
            return false;
        }else {
            ContentValues dados = pegaDadosDoUser(user);
            db.insert("User", null, dados);
            return true;
        }
    }

    public boolean existeUser(User user){
        if(user.getId() == null){
            return false;
        }
        SQLiteDatabase db = getReadableDatabase();
        String existe = "SELECT id FROM User WHERE id = ? LIMIT 1";
        Cursor cursor = db.rawQuery(existe, new String[]{user.getId()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
    }

    @NonNull
    private ContentValues pegaDadosDoUser(User user) {
        ContentValues dados = new ContentValues();
        System.out.println(user.getId());
        dados.put("id", user.getId());
        dados.put("nome", user.getNome());
        dados.put("senha", user.getSenha());
        dados.put("telefone", user.getTelefone());
        dados.put("email", user.getEmail());
        dados.put("idEmpresa", user.getEmpresa().getId());
        dados.put("desativado", user.getDesativado());
        return dados;
    }

    public User buscaUser() {
        String sql = "SELECT * FROM User";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        Log.i("QNT", ""+c.getCount());
        if(c.getCount() == 0){
            return null;
        }
        User user = populaUser(c);

        c.close();

        return user;
    }

    public void deleta() {
        SQLiteDatabase db = getWritableDatabase();
        User user = buscaUser();
        String[] params = {user.getId()};
        db.delete("User", "id = ?", params);
        new EmpresaDAO(this.context).deleta(user.getEmpresa());
    }


   private User populaUser(Cursor c) {
       User user = new User();
       while (c.moveToNext()) {
           user.setId(c.getString(c.getColumnIndex("id")));
           Log.i("QNT", "" + c.getString(c.getColumnIndex("idEmpresa")));
           user.setNome(c.getString(c.getColumnIndex("nome")));
           user.setEmail(c.getString(c.getColumnIndex("email")));
           user.setTelefone(c.getString(c.getColumnIndex("telefone")));
           user.setSenha(c.getString(c.getColumnIndex("senha")));
           user.setEmpresa(buscaEmpresa(c.getString(c.getColumnIndex("idEmpresa"))));
           user.setDesativado(c.getInt(c.getColumnIndex("desativado")));
       }
        return user;
    }

    public Empresa buscaEmpresa(String id){
        EmpresaDAO dao = new EmpresaDAO(this.context);
        return dao.buscaEmpresa();
    }

}
