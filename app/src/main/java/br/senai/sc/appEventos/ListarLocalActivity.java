package br.senai.sc.appEventos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import br.senai.sc.appEventos.database.LocalDAO;
import br.senai.sc.appEventos.modelo.Local;

public class ListarLocalActivity extends AppCompatActivity {

    ListView listViewLocais;
    ArrayAdapter<Local> adapterLocais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_local);
        setTitle("Locais");

        listViewLocais = findViewById(R.id.listView_locais);
        definirOnClickListenerListView();
        definirOnLongClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDAO localDao = new LocalDAO(getBaseContext());
        adapterLocais = new ArrayAdapter<>(ListarLocalActivity.this, android.R.layout.simple_list_item_1, localDao.listar());
        listViewLocais.setAdapter(adapterLocais);
    }

    private void definirOnClickListenerListView(){
        listViewLocais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Local localClicado = adapterLocais.getItem(position);
                Intent intent = new Intent(ListarLocalActivity.this, CadastroLocalActivity.class);
                intent.putExtra("localEdicao", localClicado);
                startActivity(intent);
            }
        });
    }

    private void definirOnLongClickListener(){
        listViewLocais.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Local localClicado = adapterLocais.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListarLocalActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Excluir Local?");
                builder.setMessage("tem certeza?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalDAO localDao = new LocalDAO(getBaseContext());
                        localDao.delete(localClicado);
                        adapterLocais.remove(localClicado);
                        adapterLocais.notifyDataSetChanged();
                        Toast.makeText(ListarLocalActivity.this, "Local removido!", Toast.LENGTH_LONG).show();
                        onResume();
                    } } );
                builder.setNegativeButton("Não", null).show();
                return true;
            }
        });
    }

    public void onClickCadastrarLocal(View view){
        Intent intent = new Intent(ListarLocalActivity.this, CadastroLocalActivity.class);
        startActivity(intent);
    }

    public void onClickEventos(View view){
        Intent intent = new Intent(ListarLocalActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}