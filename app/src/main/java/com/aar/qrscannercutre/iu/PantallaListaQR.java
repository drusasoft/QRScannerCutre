package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aar.qrscannercutre.Adaptadores.AdaptadorRecyclerQR;
import com.aar.qrscannercutre.QRScannerCutre;
import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.pojos.Codigo;
import com.aar.qrscannercutre.presentador.PresentadorPantallaListaQR;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PantallaListaQR extends AppCompatActivity implements VistaPantallaListaQR
{

    @BindView(R.id.layoutParentPantallaLista) CoordinatorLayout layoutParentPantalla;
    @BindView(R.id.cardInfoListaVacia) MaterialCardView cardInfoListaVacia;
    @BindView(R.id.toolbarPantallaLista) BottomAppBar toolbarPantallaLista;
    @BindView(R.id.titPantallaLista) TextView titPantallaLista;
    @BindView(R.id.floatingBtnBack) FloatingActionButton btnBack;
    @BindView(R.id.recyclerQR) RecyclerView recyclerQR;

    @BindString(R.string.titPantallaListaQR) String txtTituloPantalla;

    private AnimationDrawable animacionFondo;
    private boolean animacionIniciada = false;
    private PresentadorPantallaListaQR presentadorPantallaListaQR;
    private AdaptadorRecyclerQR arqr;
    private Activity activity;

    private List<Codigo> listaCodigos = new ArrayList<Codigo>();
    private List<Codigo> listaCodigosSelec = new ArrayList<Codigo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_listaqr);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbarPantallaLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        activity = this;

        //Se crea el Presentador
        QRScannerCutre qrScannerCutre = (QRScannerCutre) getApplicationContext();
        presentadorPantallaListaQR = new PresentadorPantallaListaQR(qrScannerCutre.getDataManagerDataBase());
        presentadorPantallaListaQR.setVista(this);

        //Se crea el Adaptador del RecyclerView
        arqr = new AdaptadorRecyclerQR(listaCodigos);
        recyclerQR.setHasFixedSize(true);
        recyclerQR.setItemAnimator(new DefaultItemAnimator());
        recyclerQR.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerQR.setAdapter(arqr);

        //Se cambia la fuente del titulo de la pantalla
        cambiarFuente();

        animacion_fondo();

        layoutParentPantalla.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                layoutParentPantalla.removeOnLayoutChangeListener(this);
                efecto_mostrar_circular(toolbarPantallaLista);

                mostrarListaCodigosQR();
            }
        });

        //Se ejecuta cuando se pulsa sobre un elemento del Recycler
        arqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(listaCodigosSelec.size()>0)
                {
                    //Se borra la lista de codigos seleccionados
                    listaCodigosSelec.clear();
                    btnBack.setTag("btnBack");
                    btnBack.setImageResource(android.R.drawable.ic_menu_revert);
                    titPantallaLista.setText(txtTituloPantalla);

                    //Se marcana como deseleccionados todos los codigos de la lista
                    for(Codigo codigo:listaCodigos)
                        codigo.setSeleccionado(false);

                    //Se muestran los cambios en el recycler
                    arqr.notifyDataSetChanged();

                }else
                {

                    int position = recyclerQR.getChildAdapterPosition(view);
                    ImageView imgCodigoQR =  view.findViewById(R.id.imgViewRecyclerQR);

                    //Si la imagen del codigoQR se ha cargado OK se permite ir a la pantalla que muestea el CodigoQR en grande
                    if(imgCodigoQR.getTag().equals("load_OK"))
                    {
                        String nombre = listaCodigos.get(position).getNombre();
                        String path = listaCodigos.get(position).getPath();
                        String tipoCodigo = listaCodigos.get(position).getTipo();
                        String fecha = listaCodigos.get(position).getFecha();

                        //Se va a la pantalla que muestra el codigo en pantalla completa
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, imgCodigoQR, "codigoQR");
                        Intent intent = new Intent(getApplicationContext(), PantallaCodigoQR.class);
                        intent.putExtra("nombreCodigo", nombre);
                        intent.putExtra("pathCodigo", path);
                        intent.putExtra("tipoCodigo", tipoCodigo);
                        intent.putExtra("fechaCodigo", fecha);
                        startActivity(intent, activityOptions.toBundle());

                    }else
                    {
                        Toast.makeText(getApplicationContext(), R.string.errorMostrarCodigo, Toast.LENGTH_LONG).show();
                    }

                }

            }

        });

        //Se ejecuta cuando se hace una pulsacion larga sobre un elemento del Recycler
        arqr.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                int posicion = recyclerQR.getChildAdapterPosition(view);

                listaCodigos.get(posicion).setSeleccionado(true);
                listaCodigosSelec.add(listaCodigos.get(posicion));

                btnBack.setImageResource(android.R.drawable.ic_menu_delete);
                btnBack.setTag("btnDelete");
                titPantallaLista.setText(String.valueOf(listaCodigosSelec.size()));

                //Se muestran los cambios en el recycler
                arqr.notifyDataSetChanged();

                return true;
            }
        });

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
    protected void onStop()
    {
        super.onStop();

        if(animacionIniciada && animacionFondo.isRunning())
            animacionFondo.stop();
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        if(animacionIniciada && !animacionFondo.isRunning())
            animacionFondo.start();
    }


    @OnClick(R.id.floatingBtnBack)
    public void  clicBtnVolver()
    {
        if(btnBack.getTag().equals("btnBack"))
        {
            onBackPressed();
        }else
        {
            presentadorPantallaListaQR.eliminarCodigosQR(listaCodigosSelec);
        }
    }


    @Override
    //Se muestra la lista de codigosQR guardados en la BD
    public void mostrarListaCodigosQR()
    {
        btnBack.setTag("btnBack");
        btnBack.setImageResource(android.R.drawable.ic_menu_revert);
        titPantallaLista.setText(txtTituloPantalla);

        listaCodigos.clear();

        listaCodigos.addAll(presentadorPantallaListaQR.getListaCodigosQR());

        arqr.notifyDataSetChanged();

        if(listaCodigos.size()==0)
            efecto_mostrar_circular(cardInfoListaVacia);
    }


    @Override
    public void mostrarMensajeError(int textError) {
        Toast.makeText(this, textError, Toast.LENGTH_LONG).show();
    }


    private void cambiarFuente()
    {
        Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(), "fonts/sabo_filled.otf");
        titPantallaLista.setTypeface(fuente_sabo_filled);
    }


    private void animacion_fondo()
    {

        layoutParentPantalla.setBackgroundResource(R.drawable.animacion_fondo_principal);

        layoutParentPantalla.post(new Runnable() {
            @Override
            public void run()
            {
                animacionFondo = (AnimationDrawable) layoutParentPantalla.getBackground();
                animacionFondo.start();
                animacionIniciada = true;
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

        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                btnBack.show();
            }

        });

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();

    }


}
