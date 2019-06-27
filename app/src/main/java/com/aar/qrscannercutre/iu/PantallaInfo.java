package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.aar.qrscannercutre.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PantallaInfo extends AppCompatActivity
{

    @BindView(R.id.toolbarInfo) Toolbar toolbar;
    @BindView(R.id.layout_contenido_info) LinearLayout layoutContenido;

    private AnimationDrawable animacionFondo;
    private boolean animacionFondoIniciada = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home: onBackPressed();
                                    return true;

            case R.id.menu_politicas:
                                    return true;
        }

        return false;
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
