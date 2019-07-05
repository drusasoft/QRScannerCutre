package com.aar.qrscannercutre.iu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aar.qrscannercutre.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentQRScanner extends Fragment implements SurfaceHolder.Callback
{

    @Nullable @BindView(R.id.surfaceScanner) SurfaceView surfaceScanner;
    @Nullable @BindView(R.id.imageViewTransparente) ImageView imageViewTransparente;
    @Nullable @BindView(R.id.imagenCapturada) ImageView imagenCapturada;
    @Nullable @BindColor(android.R.color.holo_red_dark) int colorRojo;
    @Nullable @BindColor(android.R.color.holo_green_dark) int colorVerde;

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



    //Metodo llamado desde la Actividad (MainActivity) cuando se pulsa el boton Escanear
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

                        borrarFoto();
                        dibujarRectanguloRojo();//Se pinta el rectangulo de color Rojo

                        barcodeDetector.setProcessor(((MainActivity)getActivity()).getDetectorCodigos());

                    }

                }catch(IOException exception)
                {
                    Toast.makeText(getContext(), R.string.txtErrorFragmentQRScanner, Toast.LENGTH_LONG).show();
                }

            }else
            {
                Toast.makeText(getContext(), R.string.txtErrorFragmentQRScanner, Toast.LENGTH_LONG).show();
            }

        }

    }



    //se realiza una foto del codigo capturado y se muestra; este metodo es llamado desde la clase mainActivity
    //Cuando un codigo ha sido capturado correctamente
    public void tomarFoto(Barcode barCode)
    {

        cameraSource.takePicture(null, new CameraSource.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] bytes)
            {

                //Se obtine el tamaño del ImageView donde se va mostrar la imagen
                int altoImagen = imagenCapturada.getHeight();
                int anchoImagen = imagenCapturada.getWidth();

                if(altoImagen>0 && anchoImagen>0)
                {

                    //Se calcula el tamaño del rectangulo que se va a dibujar alrededor del codigo capturado
                    Rect rectBarcode = barCode.getBoundingBox();
                    int anchoRect = rectBarcode.width();
                    int altoRect = rectBarcode.height();
                    int leftRect = rectBarcode.left - (anchoRect/3);
                    int rightRect = rectBarcode.right+anchoRect;
                    int topRect = rectBarcode.top-(altoRect/3);
                    int bottomRect = rectBarcode.bottom+(altoRect/3);

                    //Se comprueba si las nuevas medidas del rectangulo se sale de los limites del ImageView y si es asi se corrige
                    if(leftRect <= 0)
                        leftRect = 10;

                    if(rightRect>=anchoImagen)
                        rightRect = anchoImagen-10;

                    if(topRect <= 0 )
                        topRect = 10;

                    if(bottomRect >= altoImagen)
                        bottomRect = altoImagen-10;

                    Rect newRect = new Rect(leftRect, topRect, rightRect, bottomRect);
                    Bitmap bitmapCapturado = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap imagenAjustada = Bitmap.createScaledBitmap(bitmapCapturado, anchoImagen, altoImagen, false);//Se crea el Bitmap con el tamaño del ImageView que lo va a contener

                    Canvas canvas = new Canvas(imagenAjustada);
                    imagenCapturada.draw(canvas);
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(colorVerde);
                    paint.setStrokeWidth(4);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(newRect, paint);

                    imagenCapturada.setImageBitmap(imagenAjustada);
                    imagenCapturada.setVisibility(View.VISIBLE);

                }

                mostrarDialogo(barCode);
                liberarRecursos();

            }

        });

    }


    //Se borra la foto tomada del ImageView carganfo en el mismo una nueva imagen transparente
    private void borrarFoto()
    {
        int altoImagen = imagenCapturada.getHeight();
        int anchoImagen = imagenCapturada.getWidth();

        if(altoImagen>0 && anchoImagen>0)
        {

            //Se crea un Bitmap Tranparente para pintar sobre el rectangulo
            Bitmap bitmap = Bitmap.createBitmap(
                    anchoImagen, // Width
                    altoImagen, // Height
                    Bitmap.Config.ARGB_8888 // Config
            );

            imagenCapturada.setImageBitmap(bitmap);
        }

        imagenCapturada.setVisibility(View.INVISIBLE);
    }



    //Se liberan los recursos CameraSource BarcodeDetector y se llama al metodo parra que libere el Detector
    public void liberarRecursos()
    {

        capturaIniciada = false;

        if(imageViewTransparente != null)
            imageViewTransparente.setVisibility(View.INVISIBLE);

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

    }


    //**********************************************************************************************
        //Metodos de la Clase SurfaceHolder.Callback
    //**********************************************************************************************
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //Una vez que el surfaceview esta disponible se muestra el boton que permite al usaurio iniciar el escaneo
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


    //Se llama al metodo de la pantalla MainActivity que muestra el dialogo correspondiente al tipo de codigo capturado
    private void mostrarDialogo(Barcode barCode)
    {
        switch (barCode.valueFormat) {

            case Barcode.URL:
                ((MainActivity) getActivity()).lanzarURL(barCode);
                break;

            case Barcode.PHONE:
                ((MainActivity) getActivity()).lanzarLlamada(barCode);
                break;

            case Barcode.GEO:
                ((MainActivity) getActivity()).lanzarMapa(barCode);
                break;

            case Barcode.EMAIL:
                ((MainActivity) getActivity()).lanzarEmail(barCode);
                break;

            case Barcode.SMS:
                ((MainActivity) getActivity()).lanzarSms(barCode);
                break;

            case Barcode.CONTACT_INFO:
                ((MainActivity) getActivity()).lanzarAgenda(barCode);
                break;

            case Barcode.TEXT:
                ((MainActivity)getActivity()).lanzarTexto(barCode);
                break;

            /*case Barcode.WIFI:
                ((MainActivity) getActivity()).lanzarWifi(barCode);
                break;*/

        }

    }


    //Este metodo dibuja un rectangulo rojo en un ImageView tranparente que esta por encima del surfaceview en el que se muestra lo enfocado por la camara
    private void dibujarRectanguloRojo()
    {

        int alto = imageViewTransparente.getHeight();
        int ancho = imageViewTransparente.getWidth();

        int leftRect = 40;
        int rightRect = ancho - 40;
        int topRect = 40;
        int bottomRect = alto - 40;

        Rect rectSurface = new Rect(leftRect, topRect, rightRect, bottomRect);

        //Se crea un Bitmap Tranparente para pintar sobre el rectangulo
        Bitmap bitmap = Bitmap.createBitmap(
                ancho, // Width
                alto, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Canvas canvas = new Canvas(bitmap);
        imageViewTransparente.draw(canvas);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(colorRojo);
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectSurface, p);

        imageViewTransparente.setImageBitmap(bitmap);
        imageViewTransparente.setVisibility(View.VISIBLE);

    }


}
