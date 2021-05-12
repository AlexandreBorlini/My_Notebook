package com.my_notebook;

/*
    Classe contendo funções para se manejar os materiais (paginas e cadernos)
    Materiais são arquivos de nome: Nome cor(.txt caso pagina ou uma pasta caso caderno)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.my_notebook.R;
import com.my_notebook.Dialogos.OpcoesMaterial;
import com.my_notebook.Utilitarios.Arquivo;
import com.my_notebook.Utilitarios.Utilitarios;

import java.io.File;

public class Material {



    // --------------------------------------------------------------------------------------------- Nome do material
    public static String nomeMaterial(String nomeArquivo){

        // Caso não tenha cor retorna apenas o nome
        nomeArquivo = Arquivo.nomeArquivoSemExtensao(nomeArquivo);
        if (nomeArquivo.lastIndexOf(" ") == -1)
            return nomeArquivo;

        // Retira a cor e retorna o nome
        return nomeArquivo.substring(0, nomeArquivo.lastIndexOf(" "));
    }




    // --------------------------------------------------------------------------------------------- Cor do material
    public static int corMaterial(String nomeArquivo) {

        nomeArquivo = Arquivo.nomeArquivoSemExtensao(nomeArquivo);
        String colorString = nomeArquivo.substring(nomeArquivo.lastIndexOf(" ") + 1);

        int cor = -1; // Define cor padrão branco se não houver cor

        try {
            cor = Integer.parseInt(colorString);

        } catch (final NumberFormatException e) {

            System.out.println("A cor " + cor + " não pode ser convertida para int");
        }

        return cor;
    }




    // --------------------------------------------------------------------------------------------- Excluir material

    public static void excluirMaterialDoCaderno(Context c, String diretorio,
                                                Button botaoArquivo) {

        String nomeArquivo = Arquivo.nomeArquivo(botaoArquivo);

        botaoArquivo.setVisibility(View.GONE);
        File f = new File(diretorio + "/" + nomeArquivo);

        System.out.println("EXCLUINDO: " + diretorio + "/" + nomeArquivo);

        excluirMaterial(f);
    }

    public static void excluirMaterial(File f){

        // Se existe uma pasta (o nome do arquivo pode (e está) vindo sem a extensão)
        if (f.exists()) {

            System.out.println("O ARQUIVO EXISTE EXCLUSÃO");

            File[] arquivos = f.listFiles();

            // Se existe e não teve como ser listado, é uma pasta vazia, portanto apenas exclui
            if (arquivos == null) {

                f.delete();
                return;
            }

            // Caso contrario exclui todos os materiais de dentro do caderno
            for (int i = 0; i < arquivos.length; i++) {

                if (arquivos[i].isDirectory()) {
                    excluirMaterial(arquivos[i]);
                } else {
                    arquivos[i].delete();
                }
            }

            // TODO retornar aqui
        }

        // O arquivo pode ser passado sem a extensao .txt mesmo sendo uma pagina,
        // neste caso tambem e tentado excluir o arquivo colocando .txt
        File txtFile = new File(f.getPath() + ".txt");
        if (txtFile.exists()) {
            txtFile.delete();
            return;
        }

        // Por fim exclui o caderno em si
        f.delete();
    }




    // --------------------------------------------------------------------------------------------- Criar botão do material
    // Tipo 0: caderno
    // Tipo 1: pagina
    public static void renderizarMaterial(Context c, String nome, int cor, int tipo, String diretorio){

        // Descobre a activity do caderno para renderizar o material nele
        Activity activityCaderno = (Activity) c;

        // Cria um botão de material
        LinearLayout container = activityCaderno.findViewById(R.id.ll_elementos);
        Button botaoMaterial = Utilitarios.criarBotao(c, container, nome,
                LinearLayout.LayoutParams.MATCH_PARENT, 300, cor);

        // Coloca o icone do material
        if (tipo == 0) {

            Drawable icon = ContextCompat.getDrawable(activityCaderno, R.mipmap.ic_caderno_foreground);
            botaoMaterial.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        } else {

            Drawable icon = ContextCompat.getDrawable(activityCaderno, R.mipmap.ic_pagina_foreground);
            botaoMaterial.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        }

        // Quando o botão é clicado abre o material
        botaoMaterial.setOnClickListener((v -> {

            String nomeArquivo = Arquivo.nomeArquivo((Button) v);

            if (tipo == 0) { // Caderno

                Intent intent = new Intent(c, Caderno.class);
                String supplyDirectory = diretorio + "/" + nomeArquivo;

                intent.putExtra("DIRETORIO", supplyDirectory);
                intent.putExtra("NOME", nome);

                c.startActivity(intent);

            } else if (tipo == 1) { // Pagina

                Intent intent = new Intent(c, Pagina.class);
                intent.putExtra("NOME", nome);
                intent.putExtra("COR", corMaterial(nomeArquivo));
                intent.putExtra("DIRETORIO", diretorio);

                c.startActivity(intent);
            }
        }));

        // Quando o botão é segurado
        botaoMaterial.setOnLongClickListener(v -> {

            OpcoesMaterial optionsPopUpMenu = new OpcoesMaterial(c, diretorio,
                    botaoMaterial);
            optionsPopUpMenu.show(((FragmentActivity)activityCaderno).getSupportFragmentManager(),
                    "Supply Options");
            return false;
        });
    }
}
