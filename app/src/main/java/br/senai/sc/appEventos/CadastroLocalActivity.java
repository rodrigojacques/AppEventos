package br.senai.sc.appEventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.senai.sc.appEventos.database.LocalDAO;
import br.senai.sc.appEventos.modelo.Local;

public class CadastroLocalActivity extends AppCompatActivity {

    private int id = 0;
    private EditText editTextNome;
    private EditText editTextCidade;
    private EditText editTextBairro;
    private EditText editTextCapacidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_local);
        setTitle("Cadastrar de Local");

        editTextNome = findViewById(R.id.editTextNome);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextCapacidade = findViewById(R.id.editTextCapacidade);

        carregarLocal();
        SetTextViewLocal();
    }

    private void carregarLocal(){
        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null && intent.getExtras().get("localEdicao") != null){
            Local local = (Local) intent.getExtras().getSerializable("localEdicao");

            editTextNome.setText(local.getNome());
            editTextCidade.setText(local.getCidade());
            editTextBairro.setText(local.getBairro());
            editTextCapacidade.setText(String.valueOf(local.getCapacidade()));
            id = local.getId();
        }
    }

    public void SetTextViewLocal(){
        TextView textViewNome = findViewById(R.id.textView_nome);
        TextView textViewCidade = findViewById(R.id.textView_cidade);
        TextView textViewBairro = findViewById(R.id.textView_bairro);
        TextView textViewCapacidade = findViewById(R.id.textView_capacidade);

        textViewNome.setText("nome: " + editTextNome.getText());
        textViewCidade.setText("Cidade: " + editTextCidade.getText());
        textViewBairro.setText("Bairro: " + editTextBairro.getText());
        textViewCapacidade.setText("Capacidade: " + editTextCapacidade.getText());
    }

    public void onClickVoltar(View v){
        finish();
    }

    public void onClickSalvar(View v){
        String nome = editTextNome.getText().toString();
        String cidade = editTextCidade.getText().toString();
        String bairro = editTextBairro.getText().toString();
        String stringCapacidade = editTextCapacidade.getText().toString();


        if (!nome.isEmpty() && !cidade.isEmpty() && !bairro.isEmpty() && !stringCapacidade.isEmpty()) {
            int capacidade = Integer.valueOf(stringCapacidade);
            Local local = new Local(id, nome, cidade, bairro, capacidade);
            LocalDAO localDao = new LocalDAO(getBaseContext());
            boolean salvou = localDao.salvar(local);

            if (salvou) {
                finish();
            } else {
                Toast.makeText(CadastroLocalActivity.this, "Erro ao salvar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CadastroLocalActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }
}