package com.my_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.my_notebook.R;
import com.my_notebook.Utilitarios.Arquivo;
import com.my_notebook.Utilitarios.Imagem;
import com.my_notebook.Utilitarios.Texto;
import com.my_notebook.Utilitarios.Utilitarios;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Pagina extends AppCompatActivity {


    EditText textoPagina;
    String diretorio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina);

        textoPagina = findViewById(R.id.etm_textoPagina);

        try {
            iniciarPagina();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // --------------------------------------------------------------------------------------------- Inicializa a pagina

    void iniciarPagina() throws Exception {

        if (getIntent().hasExtra("NOME"))
            setTitle(getIntent().getStringExtra("NOME"));

        // Coloca a cor de fundo da pagina
        ConstraintLayout container;
        container = findViewById(R.id.cl_pagina);
        container.setBackgroundColor(getIntent().getIntExtra("COR", -1));

        // Pega o diretorio
        diretorio = getIntent().getStringExtra("DIRETORIO");

        inicializarActionBar();
        negrito();
        italico();
        sublinhar();
        deletar();
        carregarTexto();
    }




    // --------------------------------------------------------------------------------------------- Carrega o texto do arquivo

    private void carregarTexto() throws Exception {

        String textoHtml = Arquivo.textoDoArquivo(filenameDaPagina());

        System.out.println(textoHtml);
        textoPagina.setText(Html.fromHtml(textoHtml, 0));
    }

    String filenameDaPagina(){

        String pageName = getIntent().getStringExtra("NOME");
        int color = getIntent().getIntExtra("COR", -1);

        return diretorio + "/" + pageName + " " + color + ".txt";
    }




    // --------------------------------------------------------------------------------------------- Botões de estilizar texto

    void negrito() {

        Button botaoNegrito = findViewById(R.id.btn_negrito);
        botaoNegrito.setOnClickListener((v -> {

            // Checar se o texto é bold, se sim tira o bold
            Texto.colocarRetirarEstilo(textoPagina, Typeface.BOLD);
        }));
    }

    void italico() {

        Button botaoItalico = findViewById(R.id.btn_italico);
        botaoItalico.setOnClickListener((v ->
                Texto.colocarRetirarEstilo(textoPagina, Typeface.ITALIC)));
    }

    void sublinhar() {

        Button botaoSublinhar = findViewById(R.id.btn_sublinhado);
        botaoSublinhar.setText("U̲");

        botaoSublinhar.setOnClickListener((v -> {

            Texto.colocarRetirarEstilo(textoPagina, new UnderlineSpan());
        }));
    }


    void deletar() {

        Button botaoDeletar = findViewById(R.id.btn_deletado);

        botaoDeletar.setText("\uD835\uDDB2̶");

        botaoDeletar.setOnClickListener((v ->
                Texto.colocarRetirarEstilo(textoPagina, new StrikethroughSpan())));
    }




    // --------------------------------------------------------------------------------------------- Retornar para o caderno

    // Retornar usando o botão do aplicativo
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Apenas volta para o caderno sem retornar nada
        if (id == android.R.id.home) {

            setResult(Activity.RESULT_OK, new Intent());
            salvarPagina();
            finish();
        }
        else if (id == R.id.opt_salvar)
            salvarPagina();

        return super.onOptionsItemSelected(item);
    }

    // Ao pressionar o retornar do celular
    @Override
    public void onBackPressed() {

        setResult(Activity.RESULT_OK, new Intent());
        salvarPagina();
        super.onBackPressed();
    }



    // --------------------------------------------------------------------------------------------- Inicializar a action bar

    public void inicializarActionBar(){

       Utilitarios.definirCorActionBar(Objects.requireNonNull(getSupportActionBar()), "#323232");
        this.getWindow().setStatusBarColor(Color.parseColor("#323232"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }




    // --------------------------------------------------------------------------------------------- Salvar página no arquivo

    void salvarPagina(){

        // Salva o texto no arquivo
        FileWriter file = null;
        try {

            file = new FileWriter(filenameDaPagina());
            file.write( Html.toHtml(textoPagina.getText()) );
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // --------------------------------------------------------------------------------------------- Menu Pagina (salvar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pagina_menu, menu);
        return true;
    }

}