package com.aar.qrscannercutre.modelo;

import android.util.SparseArray;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;


public class DetectorCodigos implements Detector.Processor
{

    private DataManagerDetectorCodigos dataManagerDetectorCodigos;
    private boolean codigoDetectado;

    public DetectorCodigos(DataManagerDetectorCodigos dataManagerDetectorCodigos)
    {
        this.dataManagerDetectorCodigos = dataManagerDetectorCodigos;
        codigoDetectado = false;
    }


    @Override
    public void release()
    {

    }


    @Override
    public void receiveDetections(Detector.Detections detections)
    {

        if(!codigoDetectado)
        {
            SparseArray<Barcode> listaCodigos = detections.getDetectedItems();

            if(listaCodigos.size() > 0)
            {
                codigoDetectado = true;

                Barcode barCode = listaCodigos.valueAt(0);

                dataManagerDetectorCodigos.mostrarResultadoCaptura(barCode);

            }
        }

    }

}
