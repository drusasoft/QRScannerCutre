package com.aar.qrscannercutre.presentador;

import android.util.Log;
import com.aar.qrscannercutre.iu.VistaBase;
import com.aar.qrscannercutre.iu.VistaMainActivity;
import com.aar.qrscannercutre.modelo.DataManagerDetectorCodigos;
import com.aar.qrscannercutre.modelo.DetectorCodigos;
import com.google.android.gms.vision.barcode.Barcode;

public class PresentadorMainActivity implements PresentadorMVPMainActivity
{

    private VistaMainActivity vista;
    private DataManagerDetectorCodigos dataManagerDetectorCodigos;


    public PresentadorMainActivity(DataManagerDetectorCodigos dataManagerDetectorCodigos)
    {
        this.dataManagerDetectorCodigos = dataManagerDetectorCodigos;
        dataManagerDetectorCodigos.setPresentadorMainActivity(this);
    }


    @Override
    public void setVista(VistaBase vista)
    {
        this.vista = (VistaMainActivity) vista;
    }


    @Override
    public void liberarDetectorCodigos()
    {
        dataManagerDetectorCodigos.liberarDetectorCodigos();
    }


    @Override
    public DetectorCodigos getDetectorCodigos()
    {
        return dataManagerDetectorCodigos.getDetectorCodigos();
    }


    @Override
    //En funcion del tipo de Codigo Capturado se muestra el dialogo correspondiente
    public void gestionarCapturaCodigo(Barcode barCode)
    {

        vista.mostrarCapturaOK();

        switch (barCode.valueFormat)
        {

            case Barcode.URL:   vista.lanzarURL(barCode);
                                break;

            case Barcode.PHONE: vista.lanzarLlamada(barCode);
                                break;

        }

    }

}
