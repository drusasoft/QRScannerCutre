package com.aar.qrscannercutre.presentador;

import android.graphics.Bitmap;

import com.aar.qrscannercutre.pojos.Codigo;

public interface PresentadorMVPPantallaQRCreator extends PresentadorBase
{

    void crearCodigoQR(String datosCodigo);
    void mostrarCodigoQR(Bitmap codigoBmp);
    void compartirCodigoQR(Bitmap codigoBmp);
    void guardarCodigoQR(String nombre, Bitmap codigoBmp, String tipo);

}
