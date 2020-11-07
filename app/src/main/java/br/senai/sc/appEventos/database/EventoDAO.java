package br.senai.sc.appEventos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import br.senai.sc.appEventos.database.entity.LocalEntity;
import br.senai.sc.appEventos.database.entity.EventoEntity;
import br.senai.sc.appEventos.modelo.Local;
import br.senai.sc.appEventos.modelo.Evento;

public class EventoDAO {

    private DBGateway dbGateway;
    private final String SQL_LISTAR_TODOS = "SELECT " + EventoEntity.TABLE_NAME + "." + EventoEntity._ID + ", " +
            EventoEntity.TABLE_NAME + "." + EventoEntity.COLUMN_NAME_NOME + ", " +
            EventoEntity.TABLE_NAME + "." + EventoEntity.COLUMN_NAME_DATA + ", " +
            EventoEntity.TABLE_NAME + "." + EventoEntity.COLUMN_NAME_ID_LOCAL + ", " +
            LocalEntity.TABLE_NAME + "." + LocalEntity.COLUMN_NAME_NOME + ", " +
            LocalEntity.TABLE_NAME + "." + LocalEntity.COLUMN_NAME_CIDADE + ", " +
            LocalEntity.TABLE_NAME + "." + LocalEntity.COLUMN_NAME_BAIRRO + ", " +
            LocalEntity.TABLE_NAME + "." + LocalEntity.COLUMN_NAME_CAPACIDADE +
            " FROM " + EventoEntity.TABLE_NAME + " INNER JOIN " + LocalEntity.TABLE_NAME +
            " ON " + EventoEntity.TABLE_NAME + "." + EventoEntity.COLUMN_NAME_ID_LOCAL + " = " +
            LocalEntity.TABLE_NAME + "." + LocalEntity._ID;



    public EventoDAO(Context context){
        dbGateway = DBGateway.getInstance(context);
    }


    public boolean salvar(Evento evento){
        ContentValues contentValues = new ContentValues();

        contentValues.put(EventoEntity.COLUMN_NAME_NOME, evento.getNome());
        contentValues.put(EventoEntity.COLUMN_NAME_DATA, evento.getData());
        contentValues.put(EventoEntity.COLUMN_NAME_ID_LOCAL, evento.getLocal().getId());

        if (evento.getId() > 0){
            return dbGateway.getDatabase().update(EventoEntity.TABLE_NAME,
                    contentValues, EventoEntity._ID + "=?",
                    new String[]{String.valueOf(evento.getId())}) > 0;
        }
        return dbGateway.getDatabase().insert(EventoEntity.TABLE_NAME, null, contentValues) > 0;
    }


    public List listar(){
        return cursorEventos(SQL_LISTAR_TODOS);
    }


    public void delete(Evento evento) {
        dbGateway.getDatabase().delete(EventoEntity.TABLE_NAME,
                EventoEntity._ID + "=?", new String[]{String.valueOf(evento.getId())});
    }


    public List<Evento> pesquisar(String eventoPesquisado, String cidadePesquisada, String ordem){

        String SQL_PESQUISAR = SQL_LISTAR_TODOS;

        if(!eventoPesquisado.isEmpty() && !cidadePesquisada.isEmpty()){
            SQL_PESQUISAR = SQL_LISTAR_TODOS + " WHERE evento.nome LIKE '%"+ eventoPesquisado +"%' AND cidade LIKE '%"+ cidadePesquisada
                    +"%' ORDER BY evento.nome " + ordem;

        }else if(!eventoPesquisado.isEmpty() && cidadePesquisada.isEmpty()){
            SQL_PESQUISAR = SQL_LISTAR_TODOS + " WHERE evento.nome LIKE '%"+ eventoPesquisado +"%' ORDER BY evento.nome "+ordem;

        } else if(eventoPesquisado.isEmpty() && !cidadePesquisada.isEmpty()){
            SQL_PESQUISAR = SQL_LISTAR_TODOS + " WHERE local.cidade LIKE '%"+ cidadePesquisada +"%' ORDER BY evento.nome "+ordem;

        } else if(eventoPesquisado.isEmpty() && cidadePesquisada.isEmpty()){
            SQL_PESQUISAR = SQL_LISTAR_TODOS + " ORDER BY evento.nome " + ordem;
        }
        return cursorEventos(SQL_PESQUISAR);
    }

    public List<Evento> cursorEventos(String sql){

        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = dbGateway.getDatabase().rawQuery(sql, null);

        while (cursor.moveToNext()){

            int id = cursor.getInt(cursor.getColumnIndex(EventoEntity._ID));
            String nome = cursor.getString(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_NOME));
            String data = cursor.getString(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_DATA));
            int idLocal = cursor.getInt(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_ID_LOCAL));
            String nomeLocal = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_NOME));
            String cidade = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_CIDADE));
            String bairro = cursor.getString(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_BAIRRO));
            int capacidade = cursor.getInt(cursor.getColumnIndex(LocalEntity.COLUMN_NAME_CAPACIDADE));

            Local local = new Local(idLocal, nomeLocal, cidade, bairro, capacidade);
            eventos.add(new Evento(id, nome, data, local));
        }
        cursor.close();
        return eventos;
    }
}
