package com.aar.qrscannercutre.presentador;

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
    //Se llama al metodo de la clase que compone el Modelo y en el se libera al objeto de la clase Detector.Processor
    public void liberarDetectorCodigos()
    {
        dataManagerDetectorCodigos.liberarDetectorCodigos();
    }



    @Override
    //Metodo que devuelve un Objeto de la clase Detector.Processor que es la que realiza el proceso de deteccion
    public DetectorCodigos getDetectorCodigos()
    {
        return dataManagerDetectorCodigos.getDetectorCodigos();
    }



    @Override
    //En funcion del tipo de Codigo Capturado se muestra el dialogo correspondiente
    public void gestionarCapturaCodigo(Barcode barCode)
    {
        vista.mostrarCapturaOK(barCode);
    }


}
