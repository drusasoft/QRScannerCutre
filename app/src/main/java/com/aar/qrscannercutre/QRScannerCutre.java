package com.aar.qrscannercutre;

import android.app.Application;

import com.aar.qrscannercutre.modelo.DataManagerDetectorCodigos;

public class QRScannerCutre extends Application
{

    DataManagerDetectorCodigos dataManagerDetectorCodigos;

    public QRScannerCutre()
    {
        dataManagerDetectorCodigos = new DataManagerDetectorCodigos();
    }

    public DataManagerDetectorCodigos getDataManagerDetectorCodigos() {
        return dataManagerDetectorCodigos;
    }
}
