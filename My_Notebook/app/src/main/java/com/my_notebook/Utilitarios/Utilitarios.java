package com.my_notebook.Utilitarios;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;

import com.example.my_notebook.R;

public class Utilitarios {

    // --------------------------------------------------------------------------------------------- Criar o botão no layout dado

    public static Button criarBotao(Context context, LinearLayout layout, String texto,
                                    int largura, int altura, int cor){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (largura, altura);
        layoutParams.setMargins(0, 40, 0, 0);

        Button novoBotao = new Button(context);
        novoBotao.setLayoutParams(layoutParams);
        novoBotao.setText(texto);

        Drawable d = context.getResources().getDrawable( R.drawable.botao_com_borda );
        novoBotao.setForeground(d);
        novoBotao.setBackgroundColor(cor);

        layout.addView(novoBotao);

        return novoBotao;
    }





    // --------------------------------------------------------------------------------------------- Cor da actionBar

    public static void definirCorActionBar(ActionBar actionBar, String cor){

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(cor)));
    }
}
