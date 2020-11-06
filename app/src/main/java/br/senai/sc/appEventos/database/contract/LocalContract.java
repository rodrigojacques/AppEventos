package br.senai.sc.appEventos.database.contract;

import br.senai.sc.appEventos.database.entity.LocalEntity;

public final class LocalContract {

    private LocalContract(){}

    public static final String criarTabela(){
        return "CREATE TABLE " + LocalEntity.TABLE_NAME + " ( " +
                LocalEntity._ID + " INTEGER PRIMARY KEY, " +
                LocalEntity.COLUMN_NAME_NOME + " TEXT, " +
                LocalEntity.COLUMN_NAME_CIDADE + " TEXT, " +
                LocalEntity.COLUMN_NAME_BAIRRO + " TEXT, " +
                LocalEntity.COLUMN_NAME_CAPACIDADE + " INTEGER)";
    }

    public static final String removerTabela(){
        return "DROP TABLE IF EXISTS " + LocalEntity.TABLE_NAME;
    }
}