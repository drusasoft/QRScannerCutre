package com.aar.qrscannercutre;

import android.app.Application;

import com.aar.qrscannercutre.modelo.DataManagerCreadorCodigos;
import com.aar.qrscannercutre.modelo.DataManagerDataBase;
import com.aar.qrscannercutre.modelo.DataManagerDetectorCodigos;

public class QRScannerCutre extends Application
{

    private DataManagerDetectorCodigos dataManagerDetectorCodigos;
    private DataManagerCreadorCodigos dataManagerCreadorCodigos;
    private DataManagerDataBase dataManagerDataBase;

    public QRScannerCutre()
    {
        dataManagerDetectorCodigos = new DataManagerDetectorCodigos();
        dataManagerCreadorCodigos = new DataManagerCreadorCodigos(this);
        dataManagerDataBase = new DataManagerDataBase(this);
    }

    public DataManagerDetectorCodigos getDataManagerDetectorCodigos() {
        return dataManagerDetectorCodigos;
    }

    public DataManagerCreadorCodigos getDataManagerCreadorCodigos()
    {
        return dataManagerCreadorCodigos;
    }

    public DataManagerDataBase getDataManagerDataBase() {
        return dataManagerDataBase;
    }

}
