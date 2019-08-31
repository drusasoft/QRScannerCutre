package com.aar.qrscannercutre.modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.presentador.PresentadorBase;
import com.aar.qrscannercutre.presentador.PresentadorPantallaQRCreator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class DataManagerCreadorCodigos
{

    private PresentadorPantallaQRCreator presentador;
    private Context contex;

    public DataManagerCreadorCodigos(Context contex)
    {
        this.contex = contex;
    }

    public void setPresentador(PresentadorBase presentador)
    {
        this.presentador = (PresentadorPantallaQRCreator) presentador;
    }

    public void crearCodigoQR(String datosCodigo)
    {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try
        {
            BitMatrix bitMatrix = multiFormatWriter.encode(datosCodigo, BarcodeFormat.QR_CODE,1024,1024);

            //Se convierte el objeto BitMatrix a Bitmap
            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();

            Bitmap codigoBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for(int x=0; x<width;x++)
            {
                for (int y = 0; y < height; y++)
                    codigoBmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }

            presentador.mostrarCodigoQR(codigoBmp);

        } catch (WriterException e)
        {
            Toast.makeText(contex, R.string.errorCrearCodigoQR, Toast.LENGTH_LONG);
        }

    }

}
