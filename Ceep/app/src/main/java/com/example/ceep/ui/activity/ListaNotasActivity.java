package com.example.ceep.ui.activity;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.example.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.REQUEST_CODE_ALTERA_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.REQUEST_CODE_INSERE_NOTA;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapterRecycler;
import com.example.ceep.ui.recyclerview.helper.NotaItemTouchHelperCallback;
import com.example.ceep.ui.recyclerview.listener.OnItemClickListener;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR_LISTA_NOTAS = "Notas";
    private ListaNotasAdapterRecycler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR_LISTA_NOTAS);
        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsere = findViewById(R.id.lista_notas_insere_nota);
        botaoInsere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormulario =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormulario, REQUEST_CODE_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
//        for (int i = 1; i < 5; i++) {
//            dao.insere(new Nota("Titulo " + i, "Descrição " + i));
//        }
        return dao.todos();
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, REQUEST_CODE_ALTERA_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //nova nota - data diferente de null evita que o app quebre ao voltar do formulario sem salvar
        if (requestCode == REQUEST_CODE_INSERE_NOTA && data != null && data.hasExtra(CHAVE_NOTA)) {

            if (resultCode == RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                new NotaDAO().insere(notaRecebida);
                adapter.adiciona(notaRecebida);
            }
        }

        //alterando nota - data diferente de null evita que o app quebre ao voltar do formulario sem salvar
        if (requestCode == REQUEST_CODE_ALTERA_NOTA && data != null && data.hasExtra(CHAVE_NOTA)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

            if (posicaoRecebida > POSICAO_INVALIDA) {
                adapter.altera(posicaoRecebida, notaRecebida);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        //classe responsavel por executar os movimentos
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapterRecycler(todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }
}