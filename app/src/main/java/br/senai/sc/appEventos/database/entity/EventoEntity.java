package br.senai.sc.appEventos.database.entity;

import android.provider.BaseColumns;

public final class EventoEntity implements BaseColumns {

    public static final String TABLE_NAME = "evento";
    public static final String COLUMN_NAME_NOME = "nome";
    public static final String COLUMN_NAME_LOCAL = "local";
    public static final String COLUMN_NAME_DATA = "data";

    private EventoEntity(){}

}
