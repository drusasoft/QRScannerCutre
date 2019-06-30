package com.aar.qrscannercutre.iu;

import com.google.android.gms.vision.barcode.Barcode;

public interface VistaMainActivity extends VistaBase
{
    void mostrarCapturaOK(Barcode barCode);
    void lanzarURL(Barcode barCode);
    void lanzarLlamada(Barcode barCode);
    void lanzarMapa(Barcode barCode);
    void lanzarSms(Barcode barCode);
    void lanzarEmail(Barcode barCode);
    void lanzarAgenda(Barcode barCode);
    void lanzarWifi(Barcode barCode);
}
