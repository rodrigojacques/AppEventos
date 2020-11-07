package br.senai.sc.appEventos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import br.senai.sc.appEventos.database.EventoDAO;
import br.senai.sc.appEventos.modelo.Evento;

public class MainActivity extends AppCompatActivity {

    ListView listViewEventos;
    public static ArrayAdapter<Evento> adapterEventos;
    private EditText editTextPesquisarEvento;
    private EditText editTextPesquisarCidade;
    private Switch btn_switch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eventos");

        listViewEventos = findViewById(R.id.listView_eventos);
        editTextPesquisarEvento = findViewById(R.id.editTextPesquisarEvento);
        editTextPesquisarCidade = findViewById(R.id.editTextPesquisarCidade);
        btn_switch = findViewById(R.id.btn_switch);

        definirOnClickListenerListView();
        definirOnLongClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventoDAO eventoDao = new EventoDAO(getBaseContext());
        adapterEventos = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, eventoDao.listar());
        listViewEventos.setAdapter(adapterEventos);
    }

    private void definirOnClickListenerListView(){
        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento eventoClicado = adapterEventos.getItem(position);
                Intent intent = new Intent(MainActivity.this, CadastroEventoActivity.class);
                intent.putExtra("eventoEdicao", eventoClicado);
                startActivity(intent);
            }
        });
    }

    private void definirOnLongClickListener(){
        listViewEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Evento eventoClicado = adapterEventos.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Excluir evento?");
                builder.setMessage("tem certeza?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventoDAO eventoDAO = new EventoDAO(getBaseContext());
                        eventoDAO.delete(eventoClicado);
                        adapterEventos.remove(eventoClicado);
                        adapterEventos.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Evento removido!", Toast.LENGTH_LONG).show();
                        onResume();
                    } } );
                builder.setNegativeButton("Não", null).show();
                return true;
            }
        });
    }

    public void onClickCadastrarEvento(View view){
        Intent intent = new Intent(MainActivity.this, CadastroEventoActivity.class);
        startActivity(intent);
    }

    public void onClickLocais(View view){
        Intent intent = new Intent(MainActivity.this, ListarLocalActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickPesquisar(View view){
        EventoDAO eventoDAO = new EventoDAO(getBaseContext());
        String eventoPesquisado = editTextPesquisarEvento.getText().toString();
        String cidadePesquisada = editTextPesquisarCidade.getText().toString();
        String ordem;
        if(btn_switch.isChecked()){
            ordem = "ASC";
        }else {
            ordem = "DESC";
        }
        adapterEventos = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, eventoDAO.pesquisar(eventoPesquisado, cidadePesquisada, ordem));
        listViewEventos.setAdapter(adapterEventos);
    }
}
