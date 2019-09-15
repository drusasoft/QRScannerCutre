package com.aar.qrscannercutre.iu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.aar.qrscannercutre.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.core.content.FileProvider.getUriForFile;

public class PantallaCodigoQR extends AppCompatActivity
{

    @BindView(R.id.toolbarPantallaCodigo) BottomAppBar toolBar;
    @BindView(R.id.imgCodigoQR) ImageView imgCodigoQR;
    @BindView(R.id.titPantallaCodigo) TextView titPantallaCodigo;
    @BindView(R.id.textViewNombreCodigo) TextView textViewNombreCodigo;
    @BindView(R.id.textViewFechaCodigo) TextView textViewFechaCodigo;
    @BindView(R.id.textViewTipoCodigo) TextView textViewTipoCodigo;

    @BindString(R.string.txtNombreCodigo) String titNombre;
    @BindString(R.string.txtTipoCodigo) String titTipo;
    @BindString(R.string.txtFechaCodigo) String titFecha;

    private String nombre, path, tipo, fecha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_codigoqr);
        ButterKnife.bind(this);

        //Se pone pantalla completa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nombre = getIntent().getStringExtra("nombreCodigo");
        path = getIntent().getStringExtra("pathCodigo");
        tipo = getIntent().getStringExtra("tipoCodigo");
        fecha = getIntent().getStringExtra("fechaCodigo");

        cargarImagenCodigo();

        cambiarFuente();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home: onBackPressed();
        }

        return true;
    }


    @OnClick(R.id.floatingBtnBack)
    public void BtnVolver()
    {
        onBackPressed();
    }


    //Se comparte el codigoQR
    @OnClick(R.id.btnShareQR)
    public void compartirCodigoQR()
    {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            File fileDir = new File(Environment.getExternalStorageDirectory(), "QRScanner");
            File fileCodigoQR = new File(fileDir,"qr_share.png");

            if(!fileDir.exists())
                fileDir.mkdir();

            if(fileCodigoQR.exists())
                fileCodigoQR.delete();

            //Se crea la imagen Bitmap del codigo a apartir de la ruta
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 1024;
            options.outWidth = 1024;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            if(bitmap != null)
            {

                try
                {
                    FileOutputStream fos = new FileOutputStream(fileCodigoQR);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();

                    //Para poder acceder a la Uri de un fichero desde un Intent (como es este caso) apartir de la Api 24 es necesario crear un FileProvider (definido en el manifest)
                    //y el archivo xml provider_paths donde se establece la ruta, ya que si no se produce la Excepcion FileUriExposedException
                    Uri contentUri = getUriForFile(getApplicationContext(), "com.aar.qrscannercutre.fileprovider", fileCodigoQR);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.setType("image/png");
                    startActivity(shareIntent);

                } catch (FileNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), R.string.errorCompartirCodigoQR, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), R.string.errorCompartirCodigoQR, Toast.LENGTH_LONG).show();
                }

            }else
            {
                Toast.makeText(getApplicationContext(), R.string.errorCompartirCodigoQR_2, Toast.LENGTH_LONG).show();
            }


        }else
        {
            Toast.makeText(getApplicationContext(), R.string.errorAccesoTarjetaSD, Toast.LENGTH_LONG).show();
        }

    }


    //Se cambia la fuente de los TextViews
    private void cambiarFuente()
    {

        Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(), "fonts/sabo_filled.otf");
        Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(), "fonts/sabo_regular.otf");

        titPantallaCodigo.setTypeface(fuente_sabo_filled);
        textViewNombreCodigo.setTypeface(fuente_sabo_regular);
        textViewFechaCodigo.setTypeface(fuente_sabo_regular);
        textViewTipoCodigo.setTypeface(fuente_sabo_regular);

    }


    private void cargarImagenCodigo()
    {

        String pathPicasso = "file://"+path;

        Picasso.get()
                .load(pathPicasso)
                .into(imgCodigoQR);

        textViewNombreCodigo.setText(titNombre+" "+nombre);
        textViewFechaCodigo.setText(titTipo+" "+tipo);
        textViewTipoCodigo.setText(titFecha+" "+fecha);

    }


}
