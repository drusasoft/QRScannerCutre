package com.aar.qrscannercutre.iu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aar.qrscannercutre.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentQRScanner extends Fragment implements SurfaceHolder.Callback
{

    @Nullable @BindView(R.id.surfaceScanner) SurfaceView surfaceScanner;

    private Unbinder unbinder;
    private boolean capturaIniciada;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View viewRoot = inflater.inflate(R.layout.layout_fragment_scanner, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        surfaceScanner.getHolder().addCallback(this);

        return viewRoot;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unbinder.unbind();

        liberarRecursos();
    }


    //Metodo llamada desde la Actividad (MainActivity) cuando se pulsa el boton Escanear
    //Se crean los objetos barcodeDetector, cameraSource
    public void iniciarCaptura()
    {

        if(!capturaIniciada)
        {

            ((MainActivity) getActivity()).ocultarBtnScanner();

            capturaIniciada = true;

            barcodeDetector = new BarcodeDetector.Builder(getContext())
                    .setBarcodeFormats(Barcode.QR_CODE | Barcode.DATA_MATRIX | Barcode.PDF417 | Barcode.AZTEC)
                    .build();

            cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(surfaceScanner.getWidth(), surfaceScanner.getHeight())
                    .setAutoFocusEnabled(true)
                    .build();

            if(cameraSource != null)
            {


                try
                {
                    if(getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(surfaceScanner.getHolder());
                        Log.e("iniciarCaptura", "cameraSource.start");

                        barcodeDetector.setProcessor(((MainActivity)getActivity()).getDetectorCodigos());

                    }

                }catch(IOException exception)
                {
                    Toast.makeText(getContext(), "No se ha podido realziar la captura", Toast.LENGTH_LONG).show();
                }

            }else
            {
                Toast.makeText(getContext(), "No se ha podido realziar la captura", Toast.LENGTH_LONG).show();
            }

        }

    }

    //Se liberan los recursos CameraSource BarcodeDetector y se llama al metodo parfa que libere el Detector
    public void liberarRecursos()
    {

        Log.e("liberarRecursos", "1");

        capturaIniciada = false;

        if(cameraSource != null)
        {
            cameraSource.stop();
            cameraSource.release();
            cameraSource = null;
        }

        if(barcodeDetector != null)
        {
            barcodeDetector.release();
            barcodeDetector = null;
        }


        ((MainActivity)getActivity()).liberarDetectorCodigos();

        Log.e("liberarRecursos", "2");

    }


    //**********************************************************************************************
        //Metodos de la Clase SurfaceHolder.Callback
    //**********************************************************************************************
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.e("asdasdasd", "cuuuuulo");
        ((MainActivity)getActivity()).mostrarBtnScanner();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    //**********************************************************************************************
        // Fin Metodos de la Clase SurfaceHolder.Callback
    //**********************************************************************************************
}
