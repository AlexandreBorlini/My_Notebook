package com.my_notebook.Dialogos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.my_notebook.R;
import com.my_notebook.Material;

import java.util.Objects;

/*
    Classe contendo as opções de edição sobre o material escolhido (excluir, modificar, mover)

 */

public class OpcoesMaterial extends AppCompatDialogFragment {


    String diretorioMaterial;
    Button botaoMaterial;
    Context c;
    View view;


    // --------------------------------------------------------------------------------------------- Constructor

    public OpcoesMaterial(Context c, String diretorio, Button botaoMaterial){

        this.diretorioMaterial = diretorio;
        this.botaoMaterial = botaoMaterial;
        this.c = c;
    }




    // --------------------------------------------------------------------------------------------- onCreate

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  Objects.requireNonNull(getActivity()).getLayoutInflater();

        view = inflater.inflate(R.layout.activity_opcoes_material, null);
        builder.setView(view).setTitle("Options");

        excluirMaterial();
        editarMaterial();
        moverMaterial();

        return builder.create();
    }




    // --------------------------------------------------------------------------------------------- Excluir material

    public void excluirMaterial(){

        // TODO fazer excluir arquivos sem a cor

        Button deleteButton = view.findViewById(R.id.btn_excluir);
        deleteButton.setOnClickListener((g -> {


            AlertDialog dialogo = new AlertDialog.Builder(c)
                    .setTitle("Delete Supply").setMessage("Are you sure?")
                    .setPositiveButton("Delete", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            Button botaoConfirmar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
            botaoConfirmar.setOnClickListener(v -> {

                Material.excluirMaterialDoCaderno(c, diretorioMaterial, botaoMaterial);
                dialogo.dismiss();
            });

            dismiss();
        }));
    }





    // --------------------------------------------------------------------------------------------- Mover material

    public void moverMaterial(){

        Button moveButton = view.findViewById(R.id.btn_mover);
        moveButton.setOnClickListener((g -> {

            MoverMaterial popUpMover = new MoverMaterial(c, diretorioMaterial, botaoMaterial);
            popUpMover.show(((FragmentActivity) c).getSupportFragmentManager(), "Move Supply");
            dismiss();
        }));
    }




    // --------------------------------------------------------------------------------------------- Editar material

    public void editarMaterial(){

        Button editButton = view.findViewById(R.id.btn_editar);
        editButton.setOnClickListener((g -> {

            EditarMaterial optionsPopUpMenu = new EditarMaterial(c, diretorioMaterial, botaoMaterial);
            optionsPopUpMenu.show(((FragmentActivity) c).getSupportFragmentManager(),
                    "Supply Options");
            dismiss();
        }));
    }




    // --------------------------------------------------------------------------------------------- Extrair caderno


}