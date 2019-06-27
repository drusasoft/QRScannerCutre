package com.aar.qrscannercutre.presentador;

import com.aar.qrscannercutre.modelo.DetectorCodigos;
import com.google.android.gms.vision.barcode.Barcode;

public interface PresentadorMVPMainActivity extends PresentadorBase
{
    void liberarDetectorCodigos();
    DetectorCodigos getDetectorCodigos();
    void gestionarCapturaCodigo(Barcode barCode);
}
