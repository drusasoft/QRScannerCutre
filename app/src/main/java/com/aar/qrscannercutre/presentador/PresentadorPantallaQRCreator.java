package com.aar.qrscannercutre.presentador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.iu.VistaBase;
import com.aar.qrscannercutre.iu.VistaPantallaQRCreator;
import com.aar.qrscannercutre.modelo.DataManagerCreadorCodigos;
import com.aar.qrscannercutre.modelo.DataManagerDataBase;
import com.aar.qrscannercutre.pojos.Codigo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static androidx.core.content.FileProvider.getUriForFile;

public class PresentadorPantallaQRCreator implements PresentadorMVPPantallaQRCreator
{

    private DataManagerCreadorCodigos dataManagerCreadorCodigos;
    private DataManagerDataBase dataManagerDataBase;
    private VistaPantallaQRCreator vista;
    private Context context;

    public PresentadorPantallaQRCreator(DataManagerCreadorCodigos dataManagerCreadorCodigos, DataManagerDataBase dataManagerDataBase, Context context)
    {
        this.dataManagerCreadorCodigos = dataManagerCreadorCodigos;
        this.dataManagerDataBase = dataManagerDataBase;
        this.context = context;
        dataManagerCreadorCodigos.setPresentador(this);
    }


    @Override
    public void setVista(VistaBase vista)
    {
        this.vista = (VistaPantallaQRCreator) vista;
    }


    @Override
    public void crearCodigoQR(String datosCodigo)
    {
        dataManagerCreadorCodigos.crearCodigoQR(datosCodigo);
    }


    @Override
    public void mostrarCodigoQR(Bitmap codigoBmp)
    {
        vista.mostrarCodigoQR(codigoBmp);
    }


    @Override
    public void compartirCodigoQR(Bitmap codigoBmp)
    {
        //Primero se guarda la imagen del codigo qr en el directorio de imagenes y despues obtemos su uri
        //para asi poder compartirlo
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            if(codigoBmp != null)
            {
                try
                {
                    //Se crea el directorio y el fichero donde se va a guardar la imagen del codigo QR que se va ha compartir
                    File fileDir = new File(Environment.getExternalStorageDirectory(),"QRScanner");
                    File fileCodigoQR = new File(fileDir,"qr_share.png");

                    if(!fileDir.exists())
                        fileDir.mkdir();

                    if(fileCodigoQR.exists())
                        fileCodigoQR.delete();

                    FileOutputStream fos = new FileOutputStream(fileCodigoQR);
                    codigoBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();

                    //Para poder acceder a la Uri de un fichero desde un Intent (como es este caso) apartir de la Api 24 es necesario crear un FileProvider (definido en el manifest)
                    //y el archivo xml provider_paths donde se establece la ruta, ya que si no se produce la Excepcion FileUriExposedException
                    Uri contentUri = getUriForFile(context, "com.aar.qrscannercutre.fileprovider", fileCodigoQR);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.setType("image/png");
                    context.startActivity(shareIntent);

                } catch (FileNotFoundException e)
                {
                    Toast.makeText(context, R.string.errorCompartirCodigoQR, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(context, R.string.errorCompartirCodigoQR, Toast.LENGTH_LONG).show();
                }

            }


        }else
        {
            Toast.makeText(context, R.string.errorAccesoTarjetaSD, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void guardarCodigoQR(String nombre, Bitmap codigoBmp, String tipo)
    {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            try
            {
                //Se obtiene la fecha actual
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH)+1;
                int año = calendar.get(Calendar.YEAR);

                String fecha = dia+"-"+mes+"-"+año;
                String nombreCodigoFile = nombre+"_"+System.currentTimeMillis()+".png";

                File fileDir = new File(Environment.getExternalStorageDirectory(),"QRScanner");
                File fileCodigoQR = new File (fileDir, nombreCodigoFile);

                if(!fileDir.exists())
                    fileDir.mkdir();

                if(fileCodigoQR.exists())
                    fileCodigoQR.delete();

                FileOutputStream fos = new FileOutputStream(fileCodigoQR);
                codigoBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                //Se guardan los datos del codigoQR en la BD
                Codigo codigo = new Codigo(nombre, nombreCodigoFile, tipo, fileCodigoQR.getPath(), fecha);
                dataManagerDataBase.addCodigo(codigo);

            } catch (FileNotFoundException e)
            {
                Toast.makeText(context, R.string.errorGuardarCodigoQR, Toast.LENGTH_LONG).show();

            } catch (IOException e)
            {
                Toast.makeText(context, R.string.errorGuardarCodigoQR, Toast.LENGTH_LONG).show();
            }


        }else
        {
            Toast.makeText(context, R.string.errorAccesoTarjetaSD, Toast.LENGTH_LONG).show();
        }


    }


}
