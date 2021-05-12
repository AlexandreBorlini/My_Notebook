package com.my_notebook.Utilitarios;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Texto {




    // --------------------------------------------------------------------------------------------- Colocar/tirar estilo do texto selecionado

    // Deixa o texto no estilo passado
    // Caso já esteja estilizado, desestiliza
    public static void colocarRetirarEstilo(EditText campoTexto, int estilo){

        // Pega o texto selecionado pelo usuário
        int inicioSelecao = campoTexto.getSelectionStart();
        int fimSelecao = campoTexto.getSelectionEnd();

        // Transforma o texto em SpannableStringBuilder
        SpannableStringBuilder texto = new SpannableStringBuilder(campoTexto.getText());

        // Pega os spans dele para ver se já está estilizado
        StyleSpan[] ss = texto.getSpans(inicioSelecao, fimSelecao, StyleSpan.class);

        for (int i = 0; i < ss.length; i++) {

            // Se já estiver estilizado, retira
            if (ss[i].getStyle() == estilo){

                texto.removeSpan(ss[i]);
                campoTexto.setText(texto);
                campoTexto.setSelection(fimSelecao); // Faz o ponteiro retornar aonde estava anteriormente
                return;
            }
        }

        // Senão estiliza o texto
        estilizarTexto(texto, estilo, inicioSelecao, fimSelecao);
        campoTexto.setText(texto);
        campoTexto.setSelection(fimSelecao);
    }

    // ---- Apesar do nome, não coloca estilo como int mas como object(span)

    public static void colocarRetirarEstilo(EditText campoTexto, Object estilo){

        int inicioSelecao = campoTexto.getSelectionStart();
        int fimSelecao = campoTexto.getSelectionEnd();

        System.out.println("Inicio selecao: " + inicioSelecao);
        System.out.println("Fim selecao: " + fimSelecao);

        SpannableStringBuilder texto = new SpannableStringBuilder(campoTexto.getText());

        // Pega todas as ocorrencias do span no texto
        Object[] ss = texto.getSpans(inicioSelecao, fimSelecao, estilo.getClass());

        System.out.println("Quantidade de spans " + ss.length);

        // Caso não haja spans, coloca na parte selecionada
        if(ss.length == 0){

            spanTexto(texto, estilo, inicioSelecao, fimSelecao);
            campoTexto.setText(texto);
            campoTexto.setSelection(fimSelecao);
            return;
        }

        // Senão retira
        for (Object s : ss)
            texto.removeSpan(s);

        campoTexto.setText(texto);
        campoTexto.setSelection(fimSelecao);
    }




    // --------------------------------------------------------------------------------------------- Estiliza o texto

    public static void estilizarTexto(SpannableStringBuilder texto, int estilo,
                                                        int inicio, int fim){

        texto.setSpan(new android.text.style.StyleSpan(estilo), inicio, fim,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }




    // --------------------------------------------------------------------------------------------- Coloca span no texto

    public static void spanTexto(SpannableStringBuilder texto, Object span,
                                 int inicio, int fim){

        texto.setSpan(span, inicio,
                fim, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
