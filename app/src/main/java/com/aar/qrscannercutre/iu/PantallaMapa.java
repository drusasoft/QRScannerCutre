package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.aar.qrscannercutre.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PantallaMapa extends AppCompatActivity
{

    @BindView(R.id.layoutParentPantallaMapa) CoordinatorLayout layoutParentPantallaMapa;
    @BindView(R.id.bottomBarMapa) BottomAppBar bottomAppBar;
    @BindView(R.id.floatingBtnBack) FloatingActionButton floatingBtnBack;

    private GoogleMap mapa;
    private double lat, lng;
    private BitmapDescriptor imgMarcador;
    private String direccion;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mapa);
        ButterKnife.bind(this);

        cambiarFuente();//Se cambia la fuente del texto del Menu de  la BottombBar

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        direccion = "";

        imgMarcador = BitmapDescriptorFactory.fromResource(R.drawable.punto_mapa);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaQR)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                mapa = googleMap;

                obtenerDireccion();
            }
        });


        //Se cambia el tipo de mapa segun la opcion seleccionada
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                switch (item.getItemId())
                {

                    case R.id.menu_mapa_normal: mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                                break;

                    case R.id.menu_mapa_hibrido: mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                                break;

                    case R.id.menu_mapa_satelite: mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                                break;

                    case R.id.menu_mapa_terreno: mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                                break;

                }

                return false;
            }
        });

        layoutParentPantallaMapa.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                layoutParentPantallaMapa.removeOnLayoutChangeListener(this);
                efecto_mostrar_circular(layoutParentPantallaMapa);
            }
        });


    }



    @OnClick(R.id.floatingBtnBack)
    public void volverPantallaPrincipal()
    {
        onBackPressed();
    }



    //Se obtiene la direccion del punto recibido (latitud, longitud)
    private void obtenerDireccion()
    {

        if(comprobarConexion())
        {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try
            {
                List<Address> listaDirecciones = geocoder.getFromLocation(lat, lng, 1);

                if(listaDirecciones.size()>0)
                {
                    Address address = listaDirecciones.get(0);

                    //Esto se hace por si la direccion tiene mas de una linea
                    if(address.getMaxAddressLineIndex()>0)
                    {

                        for(int i=0; i<address.getMaxAddressLineIndex(); i++)
                            direccion = direccion + address.getAddressLine(i)+"\n";

                    }else
                    {
                        direccion = address.getAddressLine(0);
                    }
                }

            } catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), R.string.txtErrorMapa_1, Toast.LENGTH_LONG).show();
            }

        }else
        {
            Toast.makeText(getApplicationContext(), R.string.txtErrorMapa_2, Toast.LENGTH_LONG).show();
        }

        mostrarLocalizacion();
    }



    //Se muestra un marcador en el mapa en la localizacion escaneada por el usuario
    private void mostrarLocalizacion()
    {

        LatLng latLng = new LatLng(lat, lng);

        //Se crea el marcador en el mapa
        Marker marcadorMapa = mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getResources().getString(R.string.txtDireccion))
                .snippet(direccion)
                .icon(imgMarcador)
        );

        //Nos posicionamos en la ubicacion del marcador
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, (float) 18.0);
        mapa.animateCamera(cameraUpdate);

        marcadorMapa.showInfoWindow();//Con esto hacemos que se muestre siempre la info del marcador;

    }



    //Se cambia el tipo de fuente del menu desplegable de la Bottombar
    private void cambiarFuente()
    {

        if(bottomAppBar != null)
        {
            Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(),"fonts/sabo_regular.otf");

            Menu menu = bottomAppBar.getMenu();

            MenuItem menuItemVistaNormal = menu.getItem(0);
            MenuItem menuItemVistaHibrida = menu.getItem(1);
            MenuItem menuItemVistaSatelite = menu.getItem(2);
            MenuItem menuItemVistaTerreno = menu.getItem(3);

            SpannableString stringNormal = new SpannableString(menuItemVistaNormal.getTitle());
            SpannableString stringHibrida = new SpannableString(menuItemVistaHibrida.getTitle());
            SpannableString stringSatelite = new SpannableString(menuItemVistaSatelite.getTitle());
            SpannableString stringTerreno = new SpannableString(menuItemVistaTerreno.getTitle());

            stringNormal.setSpan(new CustomTypefaceSpan("", fuente_sabo_regular),0, stringNormal.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            stringHibrida.setSpan(new CustomTypefaceSpan("", fuente_sabo_regular),0, stringHibrida.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            stringSatelite.setSpan(new CustomTypefaceSpan("", fuente_sabo_regular),0, stringSatelite.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            stringTerreno.setSpan(new CustomTypefaceSpan("", fuente_sabo_regular),0, stringTerreno.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            menuItemVistaNormal.setTitle(stringNormal);
            menuItemVistaHibrida.setTitle(stringHibrida);
            menuItemVistaSatelite.setTitle(stringSatelite);
            menuItemVistaTerreno.setTitle(stringTerreno);
        }

    }



    private boolean comprobarConexion()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null);
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

        if(view == layoutParentPantallaMapa)
        {
            anim.addListener(new AnimatorListenerAdapter()
            {
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
                    floatingBtnBack.show();
                }
            });
        }

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();

    }


}
