package br.senai.sc.appEventos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.senai.sc.appEventos.database.entity.EventoEntity;
import br.senai.sc.appEventos.database.entity.LocalEntity;
import br.senai.sc.appEventos.modelo.Local;

import static br.senai.sc.appEventos.MainActivity.adapterEventos;

public class LocalDAO {

    private final String SQL_LISTAR_TODOS = "SELECT * FROM "+ LocalEntity.TABLE_NAME;
    private DBGateway dbGateway;

    public LocalDAO(Context context){
        dbGateway = DBGateway.getInstance(context);
    }

    public boolean salvar(Local local){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalEntity.COLUMN_NAME_NOME, local.getNome());
        contentValues.put(LocalEntity.COLUMN_NAME_CIDADE, local.getCidade());
        contentValues.put(LocalEntity.COLUMN_NAME_BAIRRO, local.getBairro());
        contentValues.put(LocalEntity.COLUMN_NAME_CAPACIDADE, local.getCapacidade());

        if (local.getId() > 0){
            return dbGateway.getDatabase().update(LocalEntity.TABLE_NAME,
                    contentValues, LocalEntity._ID + "=?",
                    new String[]{String.valueOf(local.getId())}) > 0;
        }
        return dbGateway.getDatabase().insert(LocalEntity.TABLE_NAME, null, contentValues) > 0;
    }


    public List<Local> listar(){
        List<Local> locais = new ArrayList<>();
        Cursor cursor = dbGateway.getDatabase().rawQuery(SQL_LISTAR_TODOS,null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(LocalEntity._ID));
            String nome = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_NOME));
            String cidade = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_CIDADE));
            String bairro = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_BAIRRO));
            int capacidade = cursor.getInt(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_CAPACIDADE));
            locais.add(new Local(id, nome, cidade, bairro, capacidade));
        }
        cursor.close();

        return locais;
    }


   public void delete(Local local){

        // for para remover eventos relacionados.

        for (int i = 0; i < adapterEventos.getCount(); i++){
            if (adapterEventos.getItem(i).getLocal().getId() == local.getId() ){

                dbGateway.getDatabase().delete(EventoEntity.TABLE_NAME,
                        EventoEntity._ID + "=?", new String[]{String.valueOf(adapterEventos.getItem(i).getId())});
            }
        }
       dbGateway.getDatabase().delete(LocalEntity.TABLE_NAME,
               LocalEntity._ID + "=?",
               new String[]{String.valueOf(local.getId())}) ;
    }
}