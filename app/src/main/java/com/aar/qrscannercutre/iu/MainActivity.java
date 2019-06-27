package com.aar.qrscannercutre.iu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.aar.qrscannercutre.QRScannerCutre;
import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.modelo.DetectorCodigos;
import com.aar.qrscannercutre.presentador.PresentadorMainActivity;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements VistaMainActivity
{

    @BindView(R.id.toolbarMain) BottomAppBar bottomAppBar;
    @BindView(R.id.floatingBtnShowScanner) FloatingActionButton floatingBtnScanner;
    @BindView(R.id.layoutParentMain) CoordinatorLayout layoutParentMain;
    @BindView(R.id.layoutContenedorFragments) FrameLayout layoutContenedorFragments;
    @BindView(R.id.layoutBtnScanner) LinearLayout layoutBtnScanner;
    @BindView(R.id.btnScanner) Button btnScanner;
    @BindView(R.id.imgInfo) ImageView imgInfo;
    @BindView(R.id.imgLuzScanner) ImageView imgLuzScanner;

    private FragmentMainActivity fragmentMainActivity;
    private FragmentQRScanner fragmentQRScanner;
    private PresentadorMainActivity presentadorMainActivity;
    private AnimationDrawable animacionFondo;
    private boolean animacionFondoIniciada = false;

    private final int PERMISSIONS_REQUEST_CAMERA = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        animacion_fondo();

        //Se crean los dos fragment que componen la pantalla principal
        fragmentMainActivity = new FragmentMainActivity();
        fragmentQRScanner = new FragmentQRScanner();

        //Se carga dinamicamente el fragment main
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layoutContenedorFragments, fragmentMainActivity, "Fragment_Main")
                .commit();


        layoutParentMain.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                layoutParentMain.removeOnLayoutChangeListener(this);
                efecto_mostrar_circular(layoutContenedorFragments);
            }
        });

        floatingBtnScanner.setTag("irScanner");

        //Se crea y configura el objeto presentador para esta Activity
        QRScannerCutre qrScannerCutre = (QRScannerCutre) getApplicationContext();
        presentadorMainActivity = new PresentadorMainActivity(qrScannerCutre.getDataManagerDetectorCodigos());
        presentadorMainActivity.setVista(this);

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //Se para la animacion de fondo
        if(animacionFondoIniciada && animacionFondo.isRunning())
            animacionFondo.stop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Se inicia de nuevo la animacion de fondo
        if(animacionFondoIniciada && !animacionFondo.isRunning())
            animacionFondo.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, "Para poder usar la App debes permitir usar la camara", Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(getSupportFragmentManager().findFragmentByTag("fragment_QRScanner") != null)
            {
                cargarFragment();
                floatingBtnScanner.setTag("irMainActivity");
                floatingBtnScanner.hide();
                floatingBtnScanner.setImageResource(android.R.drawable.ic_menu_revert);
                floatingBtnScanner.show();

            }else
            {
                finish();
            }

            return true;
        }

        return false;
    }*/


    @OnClick(R.id.floatingBtnShowScanner)
    public void pushBtnScanner(View view)
    {

        Log.e("tag", view.getTag().toString());

        if(comprobarPermisoCamara())
        {

            if(view.getTag().equals("irScanner"))
            {
                cargarFragment();

                floatingBtnScanner.setTag("irMainActivity");
                floatingBtnScanner.hide();
                floatingBtnScanner.setImageResource(android.R.drawable.ic_menu_revert);
                floatingBtnScanner.show();

                imgLuzScanner.setImageResource(R.drawable.led_black);
                efecto_mostrar_circular(layoutBtnScanner);

            }else
            {
                cargarFragment();

                floatingBtnScanner.setTag("irScanner");
                floatingBtnScanner.hide();
                floatingBtnScanner.setImageResource(R.mipmap.ic_qr);
                floatingBtnScanner.show();

                imgLuzScanner.setImageResource(R.drawable.led_black);
                efecto_ocultar_circular(layoutBtnScanner);

            }

        }

    }


    @OnClick(R.id.btnScanner)
    public void scanearCodigo()
    {
        imgLuzScanner.setImageResource(R.drawable.led_black);
        fragmentQRScanner.iniciarCaptura();
    }


    @OnClick(R.id.imgInfo)
    public void irPntallaInfo()
    {
        /*Intent intent = new Intent(this, PantallaInfo.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imgInfo, "imgInfo");
        startActivity(intent, options.toBundle());*/

        Intent intent = new Intent(this, PantallaMapa.class);
        startActivity(intent);
    }


    //**********************************************************************************************
        //Metodos llamados desde el FragmentQRScanner
    //**********************************************************************************************
    //Metodo llamado desde el FragmentQRScanner para mostrar el boton cuando el surfaceview esta disponible
    public void mostrarBtnScanner()
    {
        btnScanner.setVisibility(View.VISIBLE);
    }

    //Metoo llamado desde el FragmentQRScanner para ocultar el boton
    public void ocultarBtnScanner()
    {
        Log.e("Main Activity", "ocultarBtnScanner");
        btnScanner.setVisibility(View.INVISIBLE);
        imgLuzScanner.setImageResource(R.drawable.led_red);
    }
    //**********************************************************************************************
        //Fin Metodos llamados desde el FragmentQRScanner
    //**********************************************************************************************


    //**********************************************************************************************
        //Metodos que llaman al Presentador y que son llamados desde el Presentador
    //**********************************************************************************************
    public DetectorCodigos getDetectorCodigos()
    {
        return presentadorMainActivity.getDetectorCodigos();
    }


    public void liberarDetectorCodigos()
    {
        presentadorMainActivity.liberarDetectorCodigos();
    }


    @Override
    //Se llama al metodo del FragmentQRScanner que libera los recusrsos de la Camara...etc
    public void liberarRecursosCamara()
    {
        fragmentQRScanner.liberarRecursos();
    }


    @Override
    //Se cambia la interfaz de usuario para indicar que el codigo se ha capturado correctamente
    public void mostrarCapturaOK()
    {

        //Para poder mostrar de nuevo el boton de escanea es necesario hacero asi ya que el origen de la llamada es este metodo
        //se produce desde la Clase DetectorCodigos y los objetos de esa clase se ejecutan en un hilo a parte (Por Defecto)
        //Y android no deja modifcar los views desde un hilo distinto al prinicpal de la App
        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                Log.e("Main Activity", "mostrarDialogoResultadoQR");
                imgLuzScanner.setImageResource(R.drawable.led_green);
                btnScanner.setVisibility(View.VISIBLE);
                fragmentQRScanner.liberarRecursos();
            }
        });

    }


    @Override
    //Se le pregunta al usuario si quiere acceder a la URL escaneada
    public void lanzarURL(Barcode barCode)
    {

        //Se obtiene la URL del Barcode
        String url = barCode.rawValue;

        //Se comprueba si la url empieza por http:// o htps://
        if(!url.substring(0,4).equals("http"))
            url = "http://"+barCode.rawValue;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle("WEB")
                .setMessage("¿Quieres acceder a la dirección "+url+"?")
                .setNegativeButton("No", null);

        String finalUrl = url;
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                try
                {
                    //Se lanza el navegador
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(finalUrl));
                    startActivity(intent);

                }catch(Exception exception)
                {
                    Toast.makeText(getApplicationContext(), "Error, URL con formato incorrecto", Toast.LENGTH_LONG).show();
                }

            }
        });


        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                builder.create().show();
            }
        });



    }

    @Override
    //Se le pregunta al usuario si quiere acceder a la URL escaneada
    public void lanzarLlamada(Barcode barcode)
    {
        //Se obtiene el numero de telefono del codigo
        String numTelefono = barcode.phone.number;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle("LLAMADA")
                .setMessage("¿Quieres llamar al telefono "+numTelefono+"?")
                .setNegativeButton("No", null);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+numTelefono));
                startActivity(intent);
            }

        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });

    }

    //**********************************************************************************************
        //Fin Metodos que llaman al Presentador y que son llamados desde el Presentador
    //**********************************************************************************************




    private void cargarFragment()
    {

        if(getSupportFragmentManager().findFragmentByTag("Fragment_Main") != null)
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out)
                    .replace(R.id.layoutContenedorFragments, fragmentQRScanner, "fragment_QRScanner")
                    .commit();

        }else
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out,
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out)
                    .replace(R.id.layoutContenedorFragments, fragmentMainActivity, "Fragment_Main")
                    .commit();

            fragmentQRScanner.liberarRecursos();

        }

    }


    //Se comprueba si el usuario ha dado permiso para usar la camara y si no se solicita
    private boolean comprobarPermisoCamara()
    {

        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }else
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }

        return false;

    }


    //Se inicia la animacion (AnimationDrawable) de la imagen de fondo de la pantalla principal
    private void animacion_fondo() {

        layoutParentMain.setBackgroundResource(R.drawable.animacion_fondo_principal);

        layoutParentMain.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                animacionFondo = (AnimationDrawable) layoutParentMain.getBackground();
                animacionFondo.start();
                animacionFondoIniciada = true;
            }

        });
    }


    private void efecto_mostrar_circular(View view)
    {

        int cx = view.getWidth()/2;
        int cy = view.getHeight()/2;

        // get the final radius for the clipping circle
        int finalRadious = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadious);
        anim.setDuration(1000);//Establezco una duracion mayor al la animacion para ver mejor el efecto


        if(view == layoutContenedorFragments)
        {
            anim.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    efecto_mostrar_circular(bottomAppBar);
                }
            });
        }


        if(view == bottomAppBar)
        {
            anim.addListener(new AnimatorListenerAdapter()
            {
                public void onAnimationEnd(Animator animation)
                {
                    floatingBtnScanner.show();
                }
            });
        }

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();

    }


    private void efecto_ocultar_circular(final View view)
    {

        int cx = view.getWidth()/2;
        int cy = view.getHeight()/2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.setDuration(1000);//Establezco una duracion mayor al la animacion para ver mejor el efecto
        anim.start();

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }

        });

    }


}
