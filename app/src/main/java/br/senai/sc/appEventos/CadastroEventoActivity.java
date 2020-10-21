package br.senai.sc.appEventos;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.senai.sc.appEventos.modelo.Evento;


public class CadastroEventoActivity extends AppCompatActivity {

    private boolean edicao = false;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        setTitle("Cadastrar Evento");
        carregarEvento();
    }

    private void carregarEvento(){
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().get("eventoEdicao") != null){
            Evento evento = (Evento) intent.getExtras().getSerializable("eventoEdicao");
            EditText editTextNome = findViewById(R.id.editText_nome);
            EditText editTextLocal = findViewById(R.id.editText_local);
            EditText editTextData = findViewById(R.id.editText_data);
            editTextNome.setText(evento.getNome());
            editTextLocal.setText(evento.getLocal());
            editTextData.setText(evento.getData());
            edicao = true;
            id = evento.getId();
        }
    }

    public void onClickVoltar(View v){
        finish();
    }

    public void onClickSalvar(View v){

        EditText editTextNome = findViewById(R.id.editText_nome);
        EditText editTextLocal = findViewById(R.id.editText_local);
        EditText editTextData = findViewById(R.id.editText_data);
        String nome = editTextNome.getText().toString();
        String local = editTextLocal.getText().toString();
        String data = editTextData.getText().toString();
        if (!nome.isEmpty() && !local.isEmpty() && !data.isEmpty()) {
            Evento evento = new Evento(id, nome, local, data);
            Intent intent = new Intent();

            if (edicao) {
                intent.putExtra("eventoEditado", evento);
                setResult(11, intent);
            } else {
                intent.putExtra("novoEvento", evento);
                setResult(10, intent);
            }

            finish();
        } else{
            Toast.makeText(CadastroEventoActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }
}