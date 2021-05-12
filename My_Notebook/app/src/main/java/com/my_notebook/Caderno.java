package com.my_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.my_notebook.R;
import com.my_notebook.Utilitarios.Arquivo;
import com.my_notebook.Utilitarios.Utilitarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;




public class Caderno extends AppCompatActivity {

    // Diretorio onde este caderno está
    public String diretorio;

    // Definição de nomes para os EXTRAS
    static String NOME = "NOME";
    static String DIRETORIO = "DIRETORIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caderno);

        inicializarCaderno();
    }




    // --------------------------------------------------------------------------------------------- Inicializar o caderno

    void inicializarCaderno(){

        // Se for o caderno principal o diretório é o padrão
        // se foi criado pelo usuário pega o diretório pelo extra passado
        if(getIntent().hasExtra(NOME)){

            String nomeCaderno = getIntent().getStringExtra(NOME);
            setTitle(nomeCaderno); // Nome do notebook

            diretorio = getIntent().getStringExtra(DIRETORIO);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Botão de retornar
        }
        else{

            diretorio = getFilesDir().getAbsolutePath() + "/My Notebook -1";

            // Cria o notebook principal
            File notebookPrincipal = new File(diretorio);
            notebookPrincipal.mkdirs();

            setTitle("My Notebook");
        }

        // Cor da actionBar
        Utilitarios.definirCorActionBar(Objects.requireNonNull(getSupportActionBar()), "#323232");
        this.getWindow().setStatusBarColor(Color.parseColor("#323232"));

        // Inicializa a pesquisa
        pesquisa();
    }




    // --------------------------------------------------------------------------------------------- Pesquisa de supplys

    public void pesquisa() {

        Button botaoPesquisar = findViewById(R.id.btn_buscar);
        botaoPesquisar.setOnClickListener((v -> renderizarMateriaisPesquisados()));
    }




    // --------------------------------------------------------------------------------------------- Renderizar botões dos materiais que tem 'pesquisa' no nome
    //                                                                                               para renderizar todos basta colocar "" como pesquisa

    public void renderizarMateriaisPesquisados(){

        // Limpa a view para criar os botões novamente
        LinearLayout btnScrollView = findViewById(R.id.ll_elementos);
        btnScrollView.removeAllViews();

        EditText etPesquisa = findViewById(R.id.et_buscar);
        String pesquisa = etPesquisa.getText().toString();

        File f = new File(diretorio);
        if (f.listFiles() != null) { // Para cada arquivo (material) dentro do caderno cria um botão

            for (final File arquivoAtual : Objects.requireNonNull(f.listFiles())) {

                // Pega o nome do material
                String nomeMaterial = Material.nomeMaterial(arquivoAtual.getName());

                // Se o nome conter a pesquisa, renderiza ele
                if(nomeMaterial.toLowerCase().contains(pesquisa.toLowerCase())) {

                    // Cor do material
                    int corMaterial = Material.corMaterial(arquivoAtual.getName());

                    if (arquivoAtual.isFile()) // Pagina
                        Material.renderizarMaterial(this, nomeMaterial, corMaterial, 1, diretorio);
                    else // Caderno
                        Material.renderizarMaterial(this, nomeMaterial, corMaterial, 0, diretorio);
                }
            }
        }
    }




    // --------------------------------------------------------------------------------------------- Abrir criação de Material

    public void abrirCriacaoMaterial(){

        Intent intent = new Intent(getBaseContext(), CriacaoMaterial.class);
        intent.putExtra(DIRETORIO, diretorio);
        startActivityForResult(intent, 0);
    }




    // --------------------------------------------------------------------------------------------- Menu de opções

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.botao_menu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
        } else if (id == R.id.opt_adicionar)
            abrirCriacaoMaterial();


        else if(id == R.id.opt_exportar)
            exportarCaderno();
        else if(id == R.id.opt_importar)
            importarCaderno();


        return super.onOptionsItemSelected(item);
    }




    // --------------------------------------------------------------------------------------------- onActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            // Retorno da criacao do material
            case (0): {
                if (resultCode == Activity.RESULT_OK)
                    renderizarMateriaisPesquisados();

                break;
            }

            // Retorno da exportação do material
            case(1):{

                // Apos exportar exclui o criado
                String nomeCaderno = getTitle().toString();
                File pastaFora = new File(getFilesDir().getAbsolutePath());
                String diretorioZip = pastaFora + "/" + nomeCaderno + ".zip";

                File zip = new File(diretorioZip);
                zip.delete();
            }

            // Retorno da importação
            case(2):{

                // Copia o zip para o caderno atual
                String dirPath = diretorio + "/importado.zip";

                if(data != null){

                    Uri uriAquivoSelecionado = data.getData();
                    String extensao =
                            uriAquivoSelecionado.toString().substring(uriAquivoSelecionado.toString().lastIndexOf("."));

                    if(!extensao.equals(".zip")){

                        Toast.makeText(this, "The file must be a .zip",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Arquivo.copiarArquivoUriPara(this, uriAquivoSelecionado, dirPath);
                }

                File arquivoZip = new File(dirPath);

                try {
                    Arquivo.unzip(arquivoZip, new File(diretorio));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Exclui o arquivo zip após a extração
                arquivoZip.delete();
            }
        }
    }



    // --------------------------------------------------------------------------------------------- Exportar caderno

    public void exportarCaderno(){

        // Avisa que apenas serão salvos cadernos que terminam em alguma pagina
        // Cadernos sem Paginas serão ignorados
        AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle("Export Notebook").setMessage("Only Notebooks ended in Pages will be saved")
                .setPositiveButton("Continue", null)
                .setNegativeButton("Cancel", null)
                .show();

        // Se aceitar continua com a exportação
        Button botaoConfirmar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
        botaoConfirmar.setOnClickListener(v -> {

            // Pega este notebook
            // Se o notebook a ser salvo é o principal, não zipa a pasta My Notebook junto,
            // apenas seu conteudo
            File f;
            if(getIntent().hasExtra(NOME))
                f = new File(diretorio);
            else
                f = new File(diretorio + "\\");

            // Nome deste notebook
            String nomeCaderno = getTitle().toString();

            // Caminho completo do zip (ele ficará fora do caderno principal, e não dentro dele)
            File pastaFora = new File(getFilesDir().getAbsolutePath());
            String zipDirectory = pastaFora + "/" + nomeCaderno + ".zip";

            // Transforma em Zip
            Arquivo.criarArquivoZip(f.getAbsolutePath(),
                    zipDirectory);

            // Pega o zip
            File zipFile = new File(zipDirectory);

            // Cria a intent para que o usuario possa exportá-lo ao endereço desejado
            Uri path = FileProvider.getUriForFile(getApplicationContext(),
                    "com.my_notebook.fileprovider", zipFile);

            Intent fileIntent = new Intent(Intent.ACTION_SEND);

            fileIntent.setType("application/zip");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);

            startActivityForResult(Intent.createChooser(fileIntent, "Export"), 1);

            dialogo.dismiss();
        });
    }




    // --------------------------------------------------------------------------------------------- Importa o notebook

    public void importarCaderno(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Import"),
                2);
    }





    // --------------------------------------------------------------------------------------------- OnResume

    @Override
    public void onResume() {
        super.onResume();

        // Quando mostrar este caderno na tela, renderiza todos os materiais
        // pode ser que o caderno tenha sido atualizado exteriormente

        // ! Caso aconteça de travar por ficar pesado demais, fazer renderizar apenas no início e
        // atualizar apenas quando necessario
        renderizarMateriaisPesquisados();
    }
}