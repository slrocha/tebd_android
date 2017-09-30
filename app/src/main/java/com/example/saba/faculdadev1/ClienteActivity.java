package com.example.saba.faculdadev1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClienteActivity extends Activity {
    protected EditText editTextCliente;
    protected  ClienteValue clienteSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        Button button = (Button) findViewById(R.id.botao);

        this.editTextCliente = (EditText) findViewById(R.id.cliente);


        Intent intent = getIntent();
        clienteSelecionado = (ClienteValue)     intent.getSerializableExtra("clienteSelecionado");
        if(clienteSelecionado!=null) {
            button.setText("Alterar");
            editTextCliente.setText(clienteSelecionado.getNome());
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClienteDAO dao = new ClienteDAO(ClienteActivity.this);
                if (clienteSelecionado==null) {
                    ClienteValue clienteValue = new ClienteValue();
                    clienteValue.setNome(editTextCliente.getText().toString());
                    dao.salvar(clienteValue);
                }else{
                    clienteSelecionado.setNome(editTextCliente.getText().toString());
                    dao.alterar(clienteSelecionado);

                }

                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_disciplina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
