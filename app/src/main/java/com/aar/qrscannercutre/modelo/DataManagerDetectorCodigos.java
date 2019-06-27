package com.aar.qrscannercutre.modelo;

import android.util.Log;
import com.aar.qrscannercutre.presentador.PresentadorMainActivity;
import com.google.android.gms.vision.barcode.Barcode;

public class DataManagerDetectorCodigos
{

    private DetectorCodigos detectorCodigos;
    private PresentadorMainActivity presentadorMainActivity;

    public void setPresentadorMainActivity(PresentadorMainActivity presentadorMainActivity)
    {
        this.presentadorMainActivity = presentadorMainActivity;
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
        presentadorMainActivity.gestionarCapturaCodigo(barCode);
    }



    public void liberarDetectorCodigos()
    {

        Log.e("liberarDetectorCodigos", "liberarDetectorCodigos");

        if(detectorCodigos != null)
        {
            detectorCodigos.release();
            detectorCodigos = null;

            Log.e("liberarDetectorCodigos", "liberarDetectorCodigos 2");
        }
    }

}