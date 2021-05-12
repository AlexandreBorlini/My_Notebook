package com.my_notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.my_notebook.R;
import com.my_notebook.Utilitarios.Utilitarios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CriacaoMaterial extends AppCompatActivity {

    String diretorioCaderno; // Diretorio onde será colocado o novo material
    int cor;
    int opcaoCriacao; // 0: Caderno 1: Pagina

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criacao_material);

        inicializarCriacaoMaterial();
    }




    // --------------------------------------------------------------------------------------------- Opcao de tipo do material

    private void definirTipoMaterial(){

        // Se o caderno estiver marcado ele é a opção,
        // caso contrário é a página
        RadioButton notebookOption = findViewById(R.id.rb_optVCaderno);
        notebookOption.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked)
                opcaoCriacao = 0;
            else
                opcaoCriacao = 1;
        });
    }





    // --------------------------------------------------------------------------------------------- Inicializar

    void inicializarCriacaoMaterial(){

        setTitle("Notebook creation");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Utilitarios.definirCorActionBar(Objects.requireNonNull(getSupportActionBar()), "#323232");
        this.getWindow().setStatusBarColor(Color.parseColor("#323232"));

        diretorioCaderno = getIntent().getStringExtra("DIRETORIO");

        definirCorMaterial();
        definirTipoMaterial();
        criarMaterial();
    }



    // --------------------------------------------------------------------------------------------- Cor do material

    public void definirCorMaterial(){

        // Coloca todos os botoes de cor em uma lista
        ArrayList<View> allButtons;
        allButtons = (findViewById(R.id.ll_BotoesCor)).getTouchables();

        // Para cada botão toda vezque for clicado o fundo ficará da sua cor e
        // a cor do material será aquela ao ser criado
        for (View b : allButtons){

            (b).setOnClickListener((v -> definirCorMaterial((ImageButton) b)));
        }
    }

    public void definirCorMaterial(ImageButton btn){

        // Pega a cor do botao
        ColorDrawable corDoBotao = (ColorDrawable) btn.getBackground();
        cor = corDoBotao.getColor();

        // Deixa o fundo com a cor do botao
        ConstraintLayout background = (findViewById(R.id.cl_criacao));
        background.setBackgroundColor(cor);
    }




    // --------------------------------------------------------------------------------------------- Criar material

    private void criarMaterial(){

        // TODO mudar nome do botao
        TextView tvNomeMaterial = findViewById(R.id.tv_NomeCaderno);
        Button botaoCriar = findViewById(R.id.btn_criarCaderno);
        botaoCriar.setOnClickListener((v -> {

            // Se for criar um caderno
            if(opcaoCriacao == 0){

                // Cria a pasta
                File material = new File(diretorioCaderno, tvNomeMaterial.getText().toString()
                        + " " + cor);

                if(!material.exists()) {
                    if(!material.mkdirs()){

                        System.out.println("O caderno não pode ser criado");
                    }
                }
            }
            else{ // Se não é criado uma página

                // Cria o arquivo
                File material = new File(diretorioCaderno, tvNomeMaterial.getText().toString()
                        + " " + cor + ".txt");

                try {
                    material.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();

                    System.out.println("A pagina não pode ser criado");
                }
            }

            // Retorna para o caderno
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            finish();
        }));
    }




    // --------------------------------------------------------------------------------------------- Botao de retornar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}