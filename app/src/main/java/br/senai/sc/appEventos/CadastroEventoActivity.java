package br.senai.sc.appEventos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.senai.sc.appEventos.database.EventoDAO;
import br.senai.sc.appEventos.database.LocalDAO;
import br.senai.sc.appEventos.modelo.Evento;
import br.senai.sc.appEventos.modelo.Local;


public class CadastroEventoActivity extends AppCompatActivity {

    private int id = 0;
    private Spinner spinnerLocais;
    private ArrayAdapter<Local> locaisAdapter;
    EditText editTextNome;
    EditText editTextData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        setTitle("Cadastrar Evento");

        spinnerLocais = findViewById(R.id.spinner_locais);
        editTextNome = findViewById(R.id.editText_nome);
        editTextData = findViewById(R.id.editText_data);

        carregarLocal();
        carregarEvento();
    }

    private void carregarLocal(){
        LocalDAO localDao = new LocalDAO(getBaseContext());
        locaisAdapter = new ArrayAdapter<>(CadastroEventoActivity.this,
                android.R.layout.simple_list_item_1, localDao.listar());
        spinnerLocais.setAdapter(locaisAdapter);

    }

    private void carregarEvento(){
        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null && intent.getExtras().get("eventoEdicao") != null){
            Evento evento = (Evento) intent.getExtras().getSerializable("eventoEdicao");

            editTextNome.setText(evento.getNome());
            editTextData.setText(evento.getData());

            int posicaoLocal = obterPosicaoLocal(evento.getLocal());
            spinnerLocais.setSelection(posicaoLocal);

            id = evento.getId();
        }
    }

    public int obterPosicaoLocal(Local local){
        for (int posicao = 0; posicao < locaisAdapter.getCount(); posicao++){
            if (locaisAdapter.getItem(posicao).getId() == local.getId()){
                return posicao;
            }
        }
        return 0;
    }

    public void onClickVoltar(View v){
        finish();
    }

    public void onClickSalvar(View v){
        String nome = editTextNome.getText().toString();
        String data = editTextData.getText().toString();
        Local local = (Local) spinnerLocais.getSelectedItem();

        if (!nome.isEmpty() && !data.isEmpty() && local != null) {

            Evento evento = new Evento(id, nome, data, local);
            EventoDAO eventoDao = new EventoDAO(getBaseContext());
            boolean salvou = eventoDao.salvar(evento);

            if (salvou){
                finish();
            } else {
                Toast.makeText(CadastroEventoActivity.this, "Erro ao salvar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CadastroEventoActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }
}