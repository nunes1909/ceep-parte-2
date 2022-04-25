package com.example.ceep.ui.recyclerview.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.dao.NotaDAO;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapterRecycler;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapterRecycler adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapterRecycler adapter) {
        this.adapter = adapter;
    }

    //este metodo define a animação que vai ser permitida - left or right
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //criando o objeto das marcações de deslize
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;

        //criando o objeto das marcações de arrastar
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        //criando o movimento das marcações
        //primeiro parâmetro: dragFlags - Arrastar
        //segundo parâmetro: seipedFlags - Deslizar
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    //este metodo é chamado quando o movimento for de arrastar dentro da recycler view
    //parametro viewHolder é referente ao item que estou arrastando
    //parametro target é referente ao item que eu estou sobreponto na recycler
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaNotas(int posicaoInicial, int posicaoFinal) {
        new NotaDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    //este metodo é chamado quando o movimento for de deslize dentro do recycler view
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNotaDeslizada);
    }

    private void removeNota(int posicao) {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
