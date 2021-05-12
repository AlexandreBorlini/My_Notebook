package com.my_notebook.Utilitarios;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Arquivo {




    public static String textoDoArquivo (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = converterStreamParaString(fin);

        fin.close();
        return ret;
    }




    public static String converterStreamParaString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder texto = new StringBuilder();
        String line;

        // Le as linhas e coloca no StringBuilder
        while ((line = reader.readLine()) != null)
            texto.append(line).append("\n");

        reader.close();
        return texto.toString();
    }




    // --------------------------------------------------------------------------------------------- Criar arquivo zip

    public static boolean criarArquivoZip(String caminhoArquivo, String caminhoDestino) {
        final int BUFFER = 2048;

        File sourceFile = new File(caminhoArquivo);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(caminhoDestino);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {

                ziparSubPasta(out, sourceFile, sourceFile.getParent().length());
            }

            else {

                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(caminhoArquivo);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(caminhoUltimoComponente(caminhoArquivo));
                entry.setTime(sourceFile.lastModified());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void ziparSubPasta(ZipOutputStream out, File pasta,
                                      int tamanhoBase) throws IOException {

        final int BUFFER = 2048;

        File[] todosArquivos = pasta.listFiles();
        BufferedInputStream origem = null;

        for (File file : todosArquivos) {

            if (file.isDirectory()) {

                ziparSubPasta(out, file, tamanhoBase);

            } else {

                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(tamanhoBase);

                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origem = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);

                entry.setTime(file.lastModified());
                out.putNextEntry(entry);

                int count;

                while ((count = origem.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origem.close();
            }
        }
    }


    private static String caminhoUltimoComponente(String caminhoArquivo) {

        String[] segmentos = caminhoArquivo.split("/");

        if (segmentos.length == 0)
            return "";

        String caminho = segmentos[segmentos.length - 1];
        return caminho;
    }





    // --------------------------------------------------------------------------------------------- Pegar o nome do arquivo pelo botão

    public static String nomeArquivo(Button botao){

        // O nome do arquivo do botão é o nome do botão + a sua cor
        ColorDrawable buttonColorDrawable = (ColorDrawable) botao.getBackground();
        int buttonColor = buttonColorDrawable.getColor();

        return (botao.getText().toString() + " " + buttonColor);
    }




    // --------------------------------------------------------------------------------------------- Nome do arquivo sem extensão

    public static String nomeArquivoSemExtensao(String nomeArquivo){

        if (nomeArquivo.lastIndexOf(".") == -1)
            return nomeArquivo;

        return nomeArquivo.substring(0, nomeArquivo.lastIndexOf("."));
    }





    // --------------------------------------------------------------------------------------------- Unzipa o arquivo especificado
    public static void unzip(File zipFile, File targetDirectory) throws IOException {

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }





    // --------------------------------------------------------------------------------------------- Copia o arquivo para o local desejado

    public static void copiarArquivoUriPara(Context c, Uri uriArquivo, String destino){

        try (InputStream ins = c.getContentResolver().openInputStream(uriArquivo)) {

            File dest = new File(destino);

            try (OutputStream os = new FileOutputStream(dest)) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = ins.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
