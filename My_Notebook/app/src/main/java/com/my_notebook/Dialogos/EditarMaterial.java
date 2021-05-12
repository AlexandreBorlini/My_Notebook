package com.my_notebook.Dialogos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.my_notebook.R;
import com.my_notebook.Material;
import com.my_notebook.Utilitarios.Arquivo;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class EditarMaterial extends AppCompatDialogFragment {


    String diretorioMaterial = "";
    String caminhoMaterial;
    boolean ehArquivo = false;
    Button botaoMaterial;

    int novaCor = 0;

    Context c;
    View view;

    public EditarMaterial(Context c, String diretorio, Button botaoMaterial){

        this.diretorioMaterial = diretorio;
        this.botaoMaterial = botaoMaterial;
        this.c = c;
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  Objects.requireNonNull(getActivity()).getLayoutInflater();

        view = inflater.inflate(R.layout.activity_editar_material, null);
        builder.setView(view).setTitle("Edit Supply");

        preencherNomeMaterial();
        iniciarBotoesCor();
        aplicarEdicao();
        inicializarCorMaterial();

        return builder.create();
    }




    // --------------------------------------------------------------------------------------------- Preenche o textfield com o atual nome do Supply

    public void preencherNomeMaterial(){


        caminhoMaterial = diretorioMaterial + "/" + Arquivo.nomeArquivo(botaoMaterial);
        File f = new File(caminhoMaterial);

        if(!f.exists()){
            caminhoMaterial = caminhoMaterial + ".txt";
            f = new File(caminhoMaterial);
            ehArquivo = true;
        }


        String filename = Arquivo.nomeArquivo(botaoMaterial);
        String name = Material.nomeMaterial(filename);

        EditText supplyNameEt = view.findViewById(R.id.et_novoNome);
        supplyNameEt.setText(name);
    }






    // --------------------------------------------------------------------------------------------- Inicia os botões de cor

    public void iniciarBotoesCor(){

        ArrayList<View> todosBotoes;
        todosBotoes = (view.findViewById(R.id.ll_botoesCor)).getTouchables();

        for (View b : todosBotoes){

            (b).setOnClickListener(v ->
                    atualizarCorMaterial((ImageButton) b));
        }
    }




    // --------------------------------------------------------------------------------------------- Pega a cor do botão clicado

    public void atualizarCorMaterial(ImageButton btn){

        ColorDrawable corBotao = (ColorDrawable) btn.getBackground();
        novaCor = corBotao.getColor();

        ConstraintLayout background = (view.findViewById(R.id.cl_editarMaterial));
        background.setBackgroundColor(novaCor);
    }




    // --------------------------------------------------------------------------------------------- Inicializa a cor de fundo (a cor atual)

    public void inicializarCorMaterial(){

        ColorDrawable corBotao = (ColorDrawable) botaoMaterial.getBackground();
        novaCor = corBotao.getColor();

        ConstraintLayout background = (view.findViewById(R.id.cl_editarMaterial));
        background.setBackgroundColor(novaCor);
    }




    // --------------------------------------------------------------------------------------------- Aplica as alterações

    public void aplicarEdicao(){

        Button applyEditButton = view.findViewById(R.id.btn_aplicarEdicao);
        applyEditButton.setOnClickListener((g -> {

            String extensao = "";
            if(ehArquivo)
                extensao = ".txt";

            String novoNome = ((EditText)view.findViewById(R.id.et_novoNome)).getText().toString();
            String filename = novoNome + " " + novaCor + extensao;

            File arquivoAntigo = new File(caminhoMaterial);
            File arquivoNovo = new File(diretorioMaterial + "/" + filename);

            arquivoAntigo.renameTo(arquivoNovo);

            botaoMaterial.setText(novoNome);
            botaoMaterial.setBackgroundColor(novaCor);

            dismiss();
        }));
    }
}