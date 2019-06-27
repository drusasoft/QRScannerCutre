package com.aar.qrscannercutre.iu;

import com.google.android.gms.vision.barcode.Barcode;

public interface VistaMainActivity extends VistaBase
{
    void liberarRecursosCamara();
    void mostrarCapturaOK();
    void lanzarURL(Barcode barCode);
    void lanzarLlamada(Barcode barcode);
}
