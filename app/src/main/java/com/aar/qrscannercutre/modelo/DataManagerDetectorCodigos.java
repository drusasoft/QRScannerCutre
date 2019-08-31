package com.aar.qrscannercutre.modelo;

import com.aar.qrscannercutre.presentador.PresentadorBase;
import com.aar.qrscannercutre.presentador.PresentadorMainActivity;
import com.google.android.gms.vision.barcode.Barcode;

public class DataManagerDetectorCodigos
{

    private DetectorCodigos detectorCodigos;
    private PresentadorMainActivity presentador;


    public void setPresentador(PresentadorBase presentador)
    {
        this.presentador = (PresentadorMainActivity) presentador;
    }



    //Este metodo es llamado desde el presentador para obtener el objeto DetectorCodigos
    public DetectorCodigos getDetectorCodigos()
    {
        detectorCodigos = new DetectorCodigos(this);
        return detectorCodigos;
    }



    //Metodo llamado desde la clase DetectorCodigos
    //Se llama al presentador para que muestre el resultado de la captura en la Pantalla
    public void mostrarResultadoCaptura(Barcode barCode)
    {
        presentador.gestionarCapturaCodigo(barCode);
    }



    //Se libera el objeto de la clase Detector y se le asigna el valor Null
    public void liberarDetectorCodigos()
    {

        if(detectorCodigos != null)
        {
            detectorCodigos.release();
            detectorCodigos = null;
        }

    }

}
