package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.aar.qrscannercutre.R;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;



public class PantallaInfo extends AppCompatActivity
{

    @BindView(R.id.toolbarInfo) Toolbar toolbar;
    @BindView(R.id.layout_contenido_info) LinearLayout layoutContenido;
    @BindView(R.id.titToolBarInfo) TextView titToolBarInfo;
    @BindView(R.id.textViewCreador) TextView textViewCreador;

    private AnimationDrawable animacionFondo;
    private boolean animacionFondoIniciada = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_info);
        ButterKnife.bind(this);

        //Muestra en Pantalla Completa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        //Se cambia la fuente de los TextViews de esta pantalla
        Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(), "fonts/sabo_filled.otf");
        titToolBarInfo.setTypeface(fuente_sabo_filled);
        textViewCreador.setTypeface(fuente_sabo_filled);

        setupEnterTransition();

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_pantalla_info, menu);

        //Se cambia el tipo de fuente del menu popup
        Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(), "fonts/sabo_regular.otf");
        MenuItem menuItemPoliticas = menu.getItem(0);
        SpannableString stringPoliticas = new SpannableString(menuItemPoliticas.getTitle());

        stringPoliticas.setSpan(new CustomTypefaceSpan("", fuente_sabo_regular), 0, stringPoliticas.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItemPoliticas.setTitle(stringPoliticas);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home: onBackPressed();
                                    return true;

            case R.id.menu_politicas: //Se lanza la url donde se muestran la politicas de privacidad de la App
                                      verPoliticasPrivacidad();
                                      return true;
        }

        return false;
    }



    private void verPoliticasPrivacidad()
    {
        Intent intent;

       if(Locale.getDefault().getLanguage().equals("es"))
       {
           intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://utilities.000webhostapp.com/qrscanner_esp.html"));
       }else
       {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://utilities.000webhostapp.com/qrscanner_eng.html"));
       }

       startActivity(intent);

    }



    private void setupEnterTransition()
    {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition)
            {

            }

            @Override
            public void onTransitionEnd(Transition transition)
            {
                transition.removeListener(this);
                efecto_mostrar_circular(toolbar);
                efecto_mostrar_circular(layoutContenido);
                animacion_fondo();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }



    //Se inicia la animacion (AnimationDrawable) de la imagen de fondo de la pantalla principal
    private void animacion_fondo() {

        layoutContenido.setBackgroundResource(R.drawable.animacion_fondo_info);

        layoutContenido.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                animacionFondo = (AnimationDrawable) layoutContenido.getBackground();
                animacionFondo.start();
                animacionFondoIniciada = true;
            }

        });
    }



    private void efecto_mostrar_circular(View view)
    {

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight())/2;
        int cy = (view.getTop() + view.getBottom())/2;

        // get the final radius for the clipping circle
        int finalRadious = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadious);
        anim.setDuration(800);//Establezco una duracion mayor al la animacion para ver mejor el efecto

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();

    }

}
