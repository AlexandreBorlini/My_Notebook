package com.my_notebook.Dialogos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.my_notebook.R;
import com.my_notebook.Caderno;
import com.my_notebook.Material;
import com.my_notebook.Utilitarios.Arquivo;
import com.my_notebook.Utilitarios.Utilitarios;

import java.io.File;
import java.util.Objects;

public class MoverMaterial extends AppCompatDialogFragment {

    String diretorioMaterial;
    String caminhoMaterial;
    Button botaoMaterial;
    Context c;
    View view;



    public MoverMaterial(Context c, String diretorio, Button botaoMaterial){

        this.diretorioMaterial = diretorio;
        this.botaoMaterial = botaoMaterial;
        this.c = c;
        this.caminhoMaterial = diretorio + "/" + Arquivo.nomeArquivo(botaoMaterial);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  Objects.requireNonNull(getActivity()).getLayoutInflater();

        view = inflater.inflate(R.layout.activity_mover_material, null);
        builder.setView(view).setTitle("Move Supply");

        renderizarCadernos();

        return builder.create();
    }





    // --------------------------------------------------------------------------------------------- Renderizar cadernos
    // Renderiza os cadernos para que o usuario escolha
    // para onde este material será movido

    public void renderizarCadernos(){

        LinearLayout mainLinearLayout = view.findViewById(R.id.ll_Mover);

        botaoCadernoAnterior(mainLinearLayout);

        File f = new File(diretorioMaterial);
        if (f.listFiles() != null) {
            for (final File fileEntry : Objects.requireNonNull(f.listFiles())) {

                if(!fileEntry.getName().equals(Arquivo.nomeArquivo(botaoMaterial))) {

                    String supplyName = Material.nomeMaterial(fileEntry.getName());
                    int color = Material.corMaterial(fileEntry.getName());

                    // Se for pasta (Caderno)
                    if (!fileEntry.isFile())
                        criarBotaoCaderno(mainLinearLayout, supplyName, color);

                }
            }
        }
    }




    // --------------------------------------------------------------------------------------------- Renderiza o botao de mover para o caderno anterior

    public void botaoCadernoAnterior(LinearLayout mainLinearLayout){

        String diretorioTeto = c.getFilesDir().getAbsolutePath() + "/My Notebook -1"; // Para não deixar o usuário mover
                                                                                      // um caderno para fora do caderno principal
        if(!diretorioMaterial.equals(diretorioTeto)) {

            Button notebook = Utilitarios.criarBotao(c, mainLinearLayout, "<< Previous Notebook <<",
                    LinearLayout.LayoutParams.MATCH_PARENT, 300, -1);

            notebook.setOnClickListener((v -> {
                String newDirectory = new File(diretorioMaterial).getParent();
                moverMaterial(newDirectory);
                dismiss();
            }));
        }
    }





    // --------------------------------------------------------------------------------------------- Move Supply para o diretorio especificado

     void moverMaterial(String newDirectory){

        File oldFile = new File(caminhoMaterial);
        File newFile = new File(newDirectory +
                "/" + Arquivo.nomeArquivo(botaoMaterial));

        // Se o arquivo não existir, é porque é uma página, portanto
         // testar adicionando a extensão txt
         int pagina = 0;
         if(!oldFile.exists()) {

             oldFile = new File(caminhoMaterial + ".txt");
             newFile = new File(
                     newDirectory + "/" + Arquivo.nomeArquivo(botaoMaterial) + ".txt");
         }

        boolean renomeio = oldFile.renameTo(newFile);

        if(renomeio)
            botaoMaterial.setVisibility(View.GONE);
    }





    // --------------------------------------------------------------------------------------------- Cria botão do caderno (opção)

    public void criarBotaoCaderno(LinearLayout mainLinearLayout, String supplyName, int color){

        Button notebook = Utilitarios.criarBotao(c, mainLinearLayout, supplyName,
                LinearLayout.LayoutParams.MATCH_PARENT, 300, color);

        notebook.setOnClickListener((v -> {

            moverMaterial(diretorioMaterial + "/" + Arquivo.nomeArquivo(notebook));
            botaoMaterial.setVisibility(View.GONE);
            dismiss();
        }));
    }
}