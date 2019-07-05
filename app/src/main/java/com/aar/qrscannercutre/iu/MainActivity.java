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
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aar.qrscannercutre.QRScannerCutre;
import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.modelo.DetectorCodigos;
import com.aar.qrscannercutre.presentador.PresentadorMainActivity;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements VistaMainActivity
{

    @BindView(R.id.toolbarMain) BottomAppBar bottomAppBar;
    @BindView(R.id.titBootomBar) TextView textViewTitulo;
    @BindView(R.id.floatingBtnShowScanner) FloatingActionButton floatingBtnScanner;
    @BindView(R.id.layoutParentMain) CoordinatorLayout layoutParentMain;
    @BindView(R.id.layoutContenedorFragments) FrameLayout layoutContenedorFragments;
    @BindView(R.id.layoutBtnScanner) LinearLayout layoutBtnScanner;
    @BindView(R.id.btnScanner) Button btnScanner;
    @BindView(R.id.imgInfo) ImageView imgInfo;
    @BindView(R.id.imgLuzScanner) ImageView imgLuzScanner;
    @BindView(R.id.textViewTipoCodigoQR) TextView textViewTipoCodigoQR;

    @BindString(R.string.txtDialogWeb) String txtDialogWeb;
    @BindString(R.string.txtDialogLlamada) String txtDialogLlamada;
    @BindString(R.string.txtDialogMapa) String txtDialogMapa1;
    @BindString(R.string.txtLatitud) String txtLatitud;
    @BindString(R.string.txtLongitud) String txtLongitud;
    @BindString(R.string.txtDialogSms) String txtDialogSms;
    @BindString(R.string.txtDialogSms2) String txtDialogSms2;
    @BindString(R.string.txtDialogEmail) String txtDialogEmail;
    @BindString(R.string.txtDialogAgenda) String txtDialogAgenda;
    @BindString(R.string.txtDialogTexto) String txtDialogTexto;

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

        //Se cambia la fuente de los TextViews de esta pantalla
        Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(), "fonts/sabo_filled.otf");
        Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(), "fonts/sabo_regular.otf");
        textViewTitulo.setTypeface(fuente_sabo_filled);
        btnScanner.setTypeface(fuente_sabo_regular);
        textViewTipoCodigoQR.setTypeface(fuente_sabo_regular);

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
    //Metodo que se ejecuta a raiz de la solicitud de permiso al usuari para usar la camara, si el usuario no ha dado permiso para usar la camara
    //se muestra un mensaje incandole que es necesario dicho permiso para usar la App
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, R.string.txtPermiso, Toast.LENGTH_LONG).show();
        }
    }



    @OnClick(R.id.floatingBtnShowScanner)
    public void pushBtnScanner(View view)
    {

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
        textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR);
        fragmentQRScanner.iniciarCaptura();
    }


    @OnClick(R.id.imgInfo)
    public void irPntallaInfo()
    {
        Intent intent = new Intent(this, PantallaInfo.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imgInfo, "imgInfo");
        startActivity(intent, options.toBundle());
    }


    //**********************************************************************************************
        //Metodos llamados desde el FragmentQRScanner
    //**********************************************************************************************
    //Metodo llamado desde el FragmentQRScanner para mostrar el boton de escanear cuando el surfaceview esta disponible
    public void mostrarBtnScanner()
    {
        btnScanner.setVisibility(View.VISIBLE);
    }


    //Metodo llamado desde el FragmentQRScanner para ocultar el boton de escanear
    public void ocultarBtnScanner()
    {
        btnScanner.setVisibility(View.INVISIBLE);
        imgLuzScanner.setImageResource(R.drawable.led_red);
    }
    //**********************************************************************************************
        //Fin Metodos llamados desde el FragmentQRScanner
    //**********************************************************************************************


    //**********************************************************************************************
        //Metodos que llaman al Presentador y que son llamados desde el Presentador
    //**********************************************************************************************

    //Se llama al metodo del Presntador que devuelve un objeto de la Clase Detector (que es la clase que escanea los CodigosQR)
    public DetectorCodigos getDetectorCodigos()
    {
        return presentadorMainActivity.getDetectorCodigos();
    }


    //Se llama al metodo del Presentador que se encar de liberar el objeto de la Clase Detector
    public void liberarDetectorCodigos()
    {
        presentadorMainActivity.liberarDetectorCodigos();
    }



    @Override
    //Se cambia la interfaz de usuario para indicar que el codigo se ha capturado correctamente
    public void mostrarCapturaOK(Barcode barCode)
    {

        //Para poder mostrar de nuevo el boton de escanea es necesario hacerlo asi, ya que el origen de la llamada a este metodo
        //se produce desde la Clase DetectorCodigos (Detector) y los objetos de esa clase se ejecutan en un hilo a parte (Por Defecto)
        //Y android no deja modifcar los views desde un hilo distinto al prinicpal de la App
        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                imgLuzScanner.setImageResource(R.drawable.led_green);
                btnScanner.setVisibility(View.VISIBLE);
                fragmentQRScanner.tomarFoto(barCode);
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
                .setTitle(R.string.titDialogWeb)
                .setMessage(txtDialogWeb+", "+url+" ?")
                .setNegativeButton(R.string.btnNo, null);

        String finalUrl = url;
        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), R.string.txtErrorWeb, Toast.LENGTH_LONG).show();
                }

            }
        });


        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_1);
                builder.create().show();
            }
        });



    }

    @Override
    //Se le pregunta al usuario si quiere acceder a la URL escaneada
    public void lanzarLlamada(Barcode barCode)
    {

        //Se obtiene el numero de telefono del codigo
        String numTelefono = barCode.phone.number;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogLlamada)
                .setMessage(txtDialogLlamada+", "+numTelefono+"?")
                .setNegativeButton(R.string.btnNo, null);

        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+numTelefono));
                startActivity(intent);

                dialog.dismiss();
            }

        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_2);
                builder.create().show();
            }
        });

    }


    @Override
    //Se le pregunta al usuario si quiere ver la localizacion escaneada en el mapa
    public void lanzarMapa(Barcode barCode)
    {

        //Se obtiene las coordenadas del mapa
        Barcode.GeoPoint geoPoint = barCode.geoPoint;
        double lat = geoPoint.lat;
        double lng = geoPoint.lng;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogMapa)
                .setMessage(txtDialogMapa1+txtLatitud+" "+lat+txtLongitud+" "+lng)
                .setNegativeButton(R.string.btnNo, null);

        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Intent intent = new Intent(getApplicationContext(), PantallaMapa.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng", lng);
                startActivity(intent);

                dialog.dismiss();

            }
        });


        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_3);
                builder.create().show();
            }
        });

    }



    @Override
    //Se le pregunta al usuario si quiere enviar el SMS escaneado
    public void lanzarSms(Barcode barCode)
    {

        //Se obtienen los datos del SMS
        Barcode.Sms sms = barCode.sms;
        String telefono = sms.phoneNumber;
        String mensaje = sms.message;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this,  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogSms)
                .setMessage(txtDialogSms+txtDialogSms2+" "+telefono)
                .setNegativeButton(R.string.btnNo, null);

        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Se lanza la aplicacion del telefono que se encarga del enivo de SMS
                //*****Esto es para versiones antiguas de android****
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("address",telefono);
                intent.putExtra("sms_body",mensaje);
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);*/

                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
                Intent  intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+telefono));
                intent.putExtra("sms_body", mensaje);

                if (defaultSmsPackageName != null) // Can be null in case that there is no default, then the user would be able to choose any app that supports this intent.
                    intent.setPackage(defaultSmsPackageName);

                startActivity(intent);

                dialog.dismiss();
            }
        });


        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_4);
                builder.create().show();
            }
        });
    }



    @Override
    //Se le pregunta al usuario si quiere enviar el Email escaneado
    public void lanzarEmail(Barcode barCode)
    {

        //Se obtienen los datos del Email
        Barcode.Email emailBarcode = barCode.email;
        String direccion = emailBarcode.address;
        String asunto = emailBarcode.subject;
        String mensaje = emailBarcode.body;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogEmail)
                .setMessage(txtDialogEmail+" "+direccion)
                .setNegativeButton(R.string.btnNo, null);

        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String[] direcciones = new String[]{direccion};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, direcciones);
                intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Email"));//Se lanza la aplicacion de correo

                dialog.dismiss();
            }

        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_5);
                builder.create().show();
            }
        });

    }



    @Override
    //Se le pregunta al usuario si quiere guardar el contacto escaneado en la Agenda de su telefono
    public void lanzarAgenda(Barcode barCode)
    {

        String nombre = "";
        String telefono = "";

        //Se obtienen los datos del contacto
        Barcode.ContactInfo contactInfo = barCode.contactInfo;
        Barcode.PersonName personName = contactInfo.name;
        Barcode.Phone[] telefonos = contactInfo.phones;
        Barcode.Email[] emails = contactInfo.emails;

        if(personName != null)
            nombre = personName.first;

        if(telefonos != null && telefonos.length>0)
            telefono = telefonos[0].number;


        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogAgenda)
                .setMessage(txtDialogAgenda)
                .setNegativeButton(R.string.btnNo, null);

        String finalNombre = nombre;
        String finalTelefono = telefono;
        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                String email = "";

                if(emails != null && emails.length>0)
                    email = emails[0].address;

                //Se lanza la aplicacion del telefono para añadir un contacto
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra("finishActivityOnSaveCompleted", true); // Fix for 4.0.3 +
                intent.putExtra(ContactsContract.Intents.Insert.NAME, finalNombre);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, finalTelefono);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                startActivity(intent);

                dialog.dismiss();

            }

        });

        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_6);
                builder.create().show();
            }
        });

    }



    @Override
    public void lanzarTexto(Barcode barCode)
    {
        //Se obtiene el texto del BarCode
        String texto = txtDialogTexto+barCode.rawValue;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(R.string.titDialogTexto)
                .setMessage(texto)
                .setPositiveButton(R.string.btnAceptar, null);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                textViewTipoCodigoQR.setText(R.string.txtTipoCodigoQR_7);
                builder.create().show();
            }
        });
    }



    @Override
    public void lanzarWifi(Barcode barCode)
    {
        //Log.e("mainActivity", "Lanzar Wifi");

        //Se obtiene los datos de la Wifi
        Barcode.WiFi wifiCode = barCode.wifi;
        String ssid = wifiCode.ssid;
        String password = wifiCode.password;

        //Log.e("Wifi SSID", ssid);
        //Log.e("Wifi Password", password);

    }

    //**********************************************************************************************
        //Fin Metodos que llaman al Presentador y que son llamados desde el Presentador
    //**********************************************************************************************



    //Este metooo carga el fragment que corresponda en el layout, dicha caarga se realiza mostrando la animacion cardflip
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
