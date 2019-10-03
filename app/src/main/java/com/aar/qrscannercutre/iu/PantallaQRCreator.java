package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.aar.qrscannercutre.QRScannerCutre;
import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.presentador.PresentadorPantallaQRCreator;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class PantallaQRCreator extends AppCompatActivity implements VistaPantallaQRCreator
{

    @BindView(R.id.layoutParentQRCreator) CoordinatorLayout layoutParent;
    @BindView(R.id.layoutTipoCodigo) LinearLayout layoutTipoCodigo;
    @BindView(R.id.layoutContenedorFragments) FrameLayout layoutContenedorFragments;
    @BindView(R.id.toolbarPantallaCreator) BottomAppBar toolbar;
    @BindView(R.id.titBootomBarPantallaCreator) TextView titToolbarCreator;
    @BindView(R.id.titTipoCodigoQR) TextView titTipoCodigoQR;
    @BindView(R.id.floatingBtnBack) FloatingActionButton floatingBtnBack;
    @BindView(R.id.chipGroupTipoCodigo) ChipGroup chipGroupTipoCodigo;
    @BindView(R.id.chipTexto) Chip chipTexto;
    @BindView(R.id.chipUrl) Chip chipUrl;

    @BindString(R.string.txtTipoCodigoTexto) String txtTipoCodigoTexto;
    @BindString(R.string.txtTipoCodigoUrl) String txtTipoCodigoUrl;
    @BindString(R.string.txtTipoCodigoSms) String txtTipoCodigoSms;
    @BindString(R.string.txtTipoCodigoEmail) String txtTipoCodigoEmail;
    @BindString(R.string.txtTipoCodigoTelf) String txtTipoCodigoTelf;
    @BindString(R.string.txtTipoCodigoGeo) String txtTipoCodigoGeo;

    private PresentadorPantallaQRCreator presentadorPantallaQRCreator;

    private AnimationDrawable animacionFondo;
    private boolean animacionIniciada = false;

    private Bitmap codigoBmp;
    private String tipoCodigoSelecc = "";
    private String tipoCodigo = "";

    private int SELECC_COORD = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_qrcreator);
        ButterKnife.bind(this);

        //Muestra en Pantalla Completa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        animacion_fondo();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Se cambia la fuente de los TextViews de esta pantalla
        Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(), "fonts/sabo_filled.otf");
        Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(), "fonts/sabo_regular.otf");
        titToolbarCreator.setTypeface(fuente_sabo_filled);
        titTipoCodigoQR.setTypeface(fuente_sabo_regular);


        layoutParent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                layoutParent.removeOnLayoutChangeListener(this);
                efecto_mostrar_circular(toolbar);
            }
        });

        //Se crea y configura el objeto presentador para esta Activity
        QRScannerCutre qrScannerCutre = (QRScannerCutre) getApplicationContext();
        presentadorPantallaQRCreator = new PresentadorPantallaQRCreator(qrScannerCutre.getDataManagerCreadorCodigos(),
                qrScannerCutre.getDataManagerDataBase(), this);
        presentadorPantallaQRCreator.setVista(this);

        //Se jecuta cuando se pulsa sobre alguna de las chips de tipo de codigo
        chipGroupTipoCodigo.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.chipTexto: tipoCodigoSelecc = "texto";
                                         tipoCodigo = txtTipoCodigoTexto;
                                         break;

                    case R.id.chipUrl:  tipoCodigoSelecc = "url";
                                        tipoCodigo = txtTipoCodigoUrl;
                                        break;

                    case R.id.chipSms:  tipoCodigoSelecc = "sms";
                                        tipoCodigo = txtTipoCodigoSms;
                                        break;

                    case R.id.chipEmail:  tipoCodigoSelecc = "email";
                                          tipoCodigo = txtTipoCodigoEmail;
                                          break;

                    case R.id.chipLlamada:  tipoCodigoSelecc = "telf";
                                            tipoCodigo = txtTipoCodigoTelf;
                                            break;

                    case R.id.chipGeo:  tipoCodigoSelecc = "geo";
                                        tipoCodigo = txtTipoCodigoGeo;
                                        break;

                    case -1: //Cuando se deselecciona una opcion ya seleccionada la variable checkedId = -1
                             tipoCodigoSelecc = "ninguno";
                             break;
                }

                if(layoutContenedorFragments.getVisibility() == View.VISIBLE)
                    efecto_ocultar_circular(layoutContenedorFragments);
                else
                    cargarFragmentTipoCodigo();

            }

        });

    }


    @Override
    protected void onStop()
    {
        super.onStop();

        //Se para la animacion de fondo
        if(animacionIniciada && animacionFondo.isRunning())
            animacionFondo.stop();
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        //Se inicia de nuevo la animacion de fondo
        if(animacionIniciada && !animacionFondo.isRunning())
            animacion_fondo();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home: onBackPressed();
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECC_COORD && resultCode == RESULT_OK)
        {

            double lat = data.getDoubleExtra("latitud", 0);
            double lng = data.getDoubleExtra("longitud", 0);

            FragmentCodigoGeo fragmentCodigoGeo = (FragmentCodigoGeo) getSupportFragmentManager().findFragmentByTag("Fragment_CodigoGeo");

            if(fragmentCodigoGeo != null)
                fragmentCodigoGeo.setLatLng(String.valueOf(lat), String.valueOf(lng));

        }

    }


    @OnClick(R.id.floatingBtnBack)
    public void clicBtnVolver()
    {
        onBackPressed();
    }


    @Override
    //Este metodo es llamado desde el presentador una vez que ya ha sido creado el CodigoQR
    public void mostrarCodigoQR(Bitmap codigoBmp)
    {
        this.codigoBmp = codigoBmp;

        FragmentImagenCodigo fragmentImagenCodigo = new FragmentImagenCodigo();

        //Se carga el Fragment que se encarda de mosrtar al usaurio el Codigo QR creado
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layoutContenedorFragments, fragmentImagenCodigo, "Fragment_ImagenCodigo")
                .commit();

        layoutContenedorFragments.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                layoutContenedorFragments.removeOnLayoutChangeListener(this);
                efecto_mostrar_circular(layoutContenedorFragments);
            }
        });

    }


    //Este metodo es llamado desde el fragment FragmentImagenCodigo cuando se descarta el codigo creado
    public void descartarCodigoQR()
    {
        efecto_ocultar_circular(layoutContenedorFragments);
    }


    //Se llama al metodo del presentador que se encarga de Crear el Codigo QR de tipo texto
    //Este metodo es llamado desde los frgaments donde el usuario introduce los datos que tendra el codigo
    public void crearCodigoQR(String datosCodigo)
    {
        presentadorPantallaQRCreator.crearCodigoQR(datosCodigo);
    }


    //Se llama al metodo del presentador que se encarga de guardar en la BD los datos del CodigoQR creado y guardar la imagen en sistema de archivos del telf
    //Este metodo es llamado desde el Fragment FragmentImagenCodigo
    public void guardarCodigoQRBD(Bitmap codigoBmp)
    {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setView(R.layout.layout_dialog_guardar);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView titDialogGuardar = dialog.findViewById(R.id.titDialogGuardar);
        MaterialButton dialogButton = dialog.findViewById(R.id.btnAceptarDialogGuardar);
        TextInputEditText editTextNombre = dialog.findViewById(R.id.editText_nombre);

        //Se cambia la fuente de los TextViews de esta pantalla
        Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(), "fonts/sabo_regular.otf");
        titDialogGuardar.setTypeface(fuente_sabo_regular);
        dialogButton.setTypeface(fuente_sabo_regular);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String nombreCodigo = editTextNombre.getText().toString().trim();

                if(nombreCodigo.length()>0)
                {
                    presentadorPantallaQRCreator.guardarCodigoQR(nombreCodigo, codigoBmp, tipoCodigo);
                    dialog.dismiss();
                }else
                {
                    Toast.makeText(getApplicationContext(), R.string.errorNombreCodigoQR, Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    //Se llama al metodo del presentador que se encarga de mostrar la aplicacion que permite compartir la imagen del CodigoQR Creado
    //Este metodo es llamado desde el Fragment FragmentImagenCodigo
    public void compartirCodigoQR(Bitmap codigoBmp)
    {
        presentadorPantallaQRCreator.compartirCodigoQR(codigoBmp);
    }


    //Este metodo es llamado desde el fragment FragmentImagenCodigo para obtener el Bitmap
    // del codigo creado y mostrarlo al usuario
    public Bitmap getCodigoBmp()
    {
        return codigoBmp;
    }


    //Este metodo es llamado desde el FragmentCodigoGeo para ir a la pantalla mapa y seleecionar unas coordenadas
    public void irPantallaMapa()
    {
        Intent intent = new Intent(this, PantallaSeleccionarPosicion.class);
        startActivityForResult(intent,  SELECC_COORD, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    //Se carga el fragment para introducir los datos necesarios para el tipo de codigo de codigoQR que se quiere crear
    private void cargarFragmentTipoCodigo()
    {

        if(tipoCodigoSelecc.equals("texto"))
        {
            FragmentCodigoTexto fragmentCodigoTexto = new FragmentCodigoTexto();

            //Se carga el Fragment que se encarda de mosrtar al usaurio el Codigo QR creado
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoTexto, "Fragment_CodigoTexto")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;
        }

        if(tipoCodigoSelecc.equals("url"))
        {

            FragmentCodigoUrl fragmentCodigoUrl = new FragmentCodigoUrl();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoUrl, "Fragment_CodigoUrl")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;

        }


        if(tipoCodigoSelecc.equals("sms"))
        {

            FragmentCodigoSms fragmentCodigoSms = new FragmentCodigoSms();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoSms, "Fragment_CodigoSms")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;

        }


        if(tipoCodigoSelecc.equals("email"))
        {

            FragmentCodigoEmail fragmentCodigoEmail = new FragmentCodigoEmail();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoEmail, "Fragment_CodigoSms")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;

        }


        if(tipoCodigoSelecc.equals("telf"))
        {

            FragmentCodigoTelf fragmentCodigoTelf = new FragmentCodigoTelf();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoTelf, "Fragment_CodigoTelf")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;

        }


        if(tipoCodigoSelecc.equals("geo"))
        {

            FragmentCodigoGeo fragmentCodigoGeo = new FragmentCodigoGeo();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutContenedorFragments, fragmentCodigoGeo, "Fragment_CodigoGeo")
                    .commit();

            efecto_mostrar_circular(layoutContenedorFragments);

            return;

        }


    }


    //Se inicia la animacion (AnimationDrawable) de la imagen de fondo de la pantalla principal
    private void animacion_fondo()
    {
        layoutParent.setBackgroundResource(R.drawable.animacion_fondo_creator);

        layoutParent.post(new Runnable()
        {
            @Override
            public void run()
            {
                animacionFondo = (AnimationDrawable) layoutParent.getBackground();
                animacionFondo.start();
                animacionIniciada = true;
            }

        });

    }


    private void efecto_mostrar_circular(View view)
    {

        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadious = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadious);
        anim.setDuration(1000);//Establezco una duracion mayor al la animacion para ver mejor el efecto

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);

                if(view == toolbar)
                    efecto_mostrar_circular(layoutTipoCodigo);

                if(view == layoutTipoCodigo)
                    floatingBtnBack.show();

            }

        });

        view.setVisibility(View.VISIBLE);
        anim.start();

    }


    private void efecto_ocultar_circular(View view)
    {

        int cx = view.getWidth()/2;
        int cy = view.getHeight()/2;

        // get the final radius for the clipping circle
        int startRadious = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadious, 0);
        anim.setDuration(1000);//Establezco una duracion mayor al la animacion para ver mejor el efecto

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                cargarFragmentTipoCodigo();
            }
        });

        anim.start();

    }

}
