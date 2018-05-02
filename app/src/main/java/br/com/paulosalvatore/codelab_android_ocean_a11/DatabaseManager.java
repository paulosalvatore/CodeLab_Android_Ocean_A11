package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo on 01/05/2018.
 */

public class DatabaseManager {
    private static DatabaseManager instancia;
    private static DatabaseHelper helper;

    private boolean conexaoAberta = false;
    private SQLiteDatabase db;

    public static DatabaseManager getInstance(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);

        if (instancia == null) {
            instancia = new DatabaseManager();
            instancia.helper = helper;
        }

        return instancia;
    }

    public void abrirConexao() {
        if (!conexaoAberta) {
            db = helper.getWritableDatabase();
            conexaoAberta = true;
        }
    }

    public void fecharConexao() {
        if (conexaoAberta) {
            db.close();
            conexaoAberta = false;
        }
    }

    public List<Compromisso> obterCompromissos() {
        List<Compromisso> compromissos = new ArrayList<Compromisso>();

        abrirConexao();

        String sql = "SELECT * FROM compromissos";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                long data = cursor.getLong(cursor.getColumnIndex("data"));

                Compromisso compromisso = new Compromisso(
                        id,
                        titulo,
                        latitude,
                        longitude,
                        data
                );

                compromissos.add(compromisso);
            } while (cursor.moveToNext());
        }

        return compromissos;
    }

    public void inserirCompromisso(Compromisso compromisso) {
        abrirConexao();

        ContentValues contentValues = new ContentValues();
        contentValues.put("titulo", compromisso.getTitulo());
        contentValues.put("latitude", compromisso.getLatitude());
        contentValues.put("longitude", compromisso.getLongitude());
        contentValues.put("data", compromisso.getData());

        db.insert("compromissos", null, contentValues);

        fecharConexao();
    }

    public void editarCompromisso(Compromisso compromisso) {
        String sql = "UPDATE compromissos SET titulo = '" + compromisso.getTitulo() + "'," +
                "latitude = '" + compromisso.getLatitude() + "'," +
                "longitude = '" + compromisso.getLongitude() + "'," +
                "data = '" + compromisso.getData() + "' " +
                "WHERE (id = '" + compromisso.getId() + "')";

        abrirConexao();

        db.execSQL(sql);

        fecharConexao();
    }

    public void removerCompromisso(Compromisso compromisso) {
        String sql = "DELETE FROM compromissos WHERE (id = '" + compromisso.getId() + "')";

        abrirConexao();

        db.execSQL(sql);

        fecharConexao();
    }
}
