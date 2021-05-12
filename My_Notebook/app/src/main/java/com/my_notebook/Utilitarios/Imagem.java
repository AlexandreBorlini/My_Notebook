package com.my_notebook.Utilitarios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;

public class Imagem {


    // --------------------------------------------------------------------------------------------- Rotacionar imagem para sua orientação original

    public static Bitmap imagemOrientada(Context c, Uri uri) throws IOException {

        InputStream imagemStream = null;
        imagemStream = c.getContentResolver().openInputStream(uri);

        ExifInterface ei = new ExifInterface(c.getContentResolver().openInputStream(uri));
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap imagemRotacionada = BitmapFactory.decodeStream(imagemStream);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                imagemRotacionada = rotacionarImagem(imagemRotacionada, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                imagemRotacionada = rotacionarImagem(imagemRotacionada, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                imagemRotacionada = rotacionarImagem(imagemRotacionada, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                imagemRotacionada = imagemRotacionada;
        }

        return imagemRotacionada;
    }




    // --------------------------------------------------------------------------------------------- Rotaciona a imagem de acordo com o angulo dado

    public static Bitmap rotacionarImagem(Bitmap source, float angulo) {

        Matrix matriz = new Matrix();
        matriz.postRotate(angulo);

        Bitmap bitmapRotacionado = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matriz, true);

        return bitmapRotacionado;
    }




    // --------------------------------------------------------------------------------------------- Escala a imagem de acordo com a altura escolhida

    public static Bitmap escalarImagem(Bitmap imagem, int tamanho){

        float scale = (float) imagem.getWidth()/(float) imagem.getHeight();
        Bitmap imagemEscalada = Bitmap.createScaledBitmap(imagem, (int)(tamanho * scale), tamanho, true);

        return imagemEscalada;
    }
}
