package com.example.saba.faculdadev1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class ClienteDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clientes.db";

    private static final int VERSAO = 1;

    public static final String TABLE_CLIENTE = "Cliente_table";

    public static final String ColIdCliente = "IDCLIENTE";
    public static final String ColNome = "NOME";
    public static final String ColCpf = "CPF";
    public static final String ColTelefone = "TELEFONE";

    private static final String CREATE_TABLE_Cliente = "CREATE TABLE "
            + TABLE_CLIENTE + "(" + ColIdCliente + " INTEGER PRIMARY KEY AUTOINCREMENT," + ColNome
            + " TEXT," + ColCpf + " TEXT," + ColTelefone
            + " TEXT)";

    public ClienteDAO(Context context) {
        super(context, DATABASE_NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_Cliente);
            db.execSQL("create table " + TABLE_CLIENTE +" (IDCLIENTE INTEGER PRIMARY KEY AUTOINCREMENT,NOME TEXT,ENDERECO TEXT,CPF TEXT,EMAIL TEXT,TELEFONE TEXT)");
        }
        catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLIENTE);
        onCreate(db);
    }

    public void alterar(ClienteValue cliente) {
        ContentValues values = new ContentValues();
        values.put("NOME", cliente.getNome());

        getWritableDatabase().update("Cliente_table", values,
                "IDCLIENTE=?", new String[]{cliente.getId().toString()});
    }

    public void deletar(ClienteValue clienteValue) {
        String[] args = { clienteValue.getId().toString() };
        getWritableDatabase().delete("Cliente_table", "IDCLIENTE=?", args);
    }

    public void dropAll(){
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ TABLE_CLIENTE);
        onCreate( getWritableDatabase());

    }

    public void salvar(ClienteValue clienteValue) {
        ContentValues values = new ContentValues();
        values.put("NOME", clienteValue.getNome());
        values.put("CPF", clienteValue.getCpf());
        values.put("TELEFONE", clienteValue.getTelefone());

        getWritableDatabase().insert("Cliente_table", null, values);

    }

    public List getLista(){

        List<ClienteValue> clientesLista = new LinkedList<ClienteValue>();

        String query = "SELECT  * FROM Cliente_table order by NOME";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ClienteValue clienteValue = null;

        if (cursor.moveToFirst()) {
            do {
                clienteValue = new ClienteValue();
                clienteValue.setId(Long.parseLong(cursor.getString(0)));
                clienteValue.setNome(cursor.getString(1));
                clienteValue.setCpf(cursor.getString(2));
                clienteValue.setTelefone(cursor.getString(3));

                clientesLista.add(clienteValue);

            } while (cursor.moveToNext());
        }
        return clientesLista;
    }

}