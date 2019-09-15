package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.aar.qrscannercutre.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class PantallaSeleccionarPosicion extends AppCompatActivity
{

    @BindView(R.id.layoutParentPantallaSeleccionarPosicion) CoordinatorLayout layoutPantallaSeleccionarPosicion;
    @BindView(R.id.bottomBarMapa) BottomAppBar bottomBar;
    @BindView(R.id.titPantallaMapaCoord) TextView titPantallaMapaCoord;
    @BindView(R.id.floatingBtnBack) FloatingActionButton btnBack;
    @BindString(R.string.txtPosicion) String titMarcador;
    @BindString(R.string.txtDialogCoordenadas) String txtDialogCoordenadas;

    private GoogleMap mapa;
    private BitmapDescriptor imgMarcador;
    private LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mapa_coord);
        ButterKnife.bind(this);

        //Muestra en Pantalla Completa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cambiarFuente();//Se cambia la fuente del texto del Menu de  la BottombBar

        imgMarcador = BitmapDescriptorFactory.fromResource(R.drawable.punto_mapa);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaQR)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                mapa = googleMap;

                efecto_mostrar_circular(layoutPantallaSeleccionarPosicion);

                //Si se hace una pulsacion larga sobre el mapa se añade un marcador
                mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng)
                    {
                        mostrar_marcador(latLng);
                    }
                });

                //Se ejecuta cuando se pulsa sobre un marcador
                mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        mostrarDialogCoords();

                        return false;
                    }
                });
            }

        });


        bottomBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        if(!comprobarConexion())
            Toast.makeText(this, R.string.txtErrorMapa, Toast.LENGTH_LONG).show();

    }


    @OnClick(R.id.floatingBtnBack)
    public void cerrarPantalla()
    {
        onBackPressed();
    }


    private void mostrar_marcador(LatLng latLng)
    {

        this.latLng = latLng;

        mapa.clear();

        String txtPosicion = latLng.latitude+"; "+latLng.longitude;

        Marker marcadorMapa = mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(imgMarcador)
                .title(titMarcador)
                .snippet(txtPosicion));

        marcadorMapa.showInfoWindow();

        mostrarDialogCoords();

    }


    //Se muestra el dialogo preguntando si quieres usar estas cooordenadas
    private void mostrarDialogCoords()
    {

        String stringLat = "Lat: "+latLng.latitude+"\n";
        String stringLng = "Lng: "+latLng.longitude+"\n";
        String txtDialogMessage = txtDialogCoordenadas + stringLat + stringLng;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(titMarcador)
                .setMessage(txtDialogMessage)
                .setNegativeButton(R.string.btnNo, null);

        builder.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent data = new Intent();
                data.putExtra("latitud", latLng.latitude);
                data.putExtra("longitud", latLng.longitude);

                setResult(RESULT_OK, data);
                onBackPressed();
            }

        });

        builder.create().show();

    }


    //Se cambia el tipo de fuente del menu desplegable de la Bottombar
    private void cambiarFuente()
    {

        if(bottomBar != null)
        {
            Typeface fuente_sabo_regular = Typeface.createFromAsset(getAssets(),"fonts/sabo_regular.otf");
            Typeface fuente_sabo_filled = Typeface.createFromAsset(getAssets(),"fonts/sabo_filled.otf");

            titPantallaMapaCoord.setTypeface(fuente_sabo_filled);

            Menu menu = bottomBar.getMenu();

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


    private void efecto_mostrar_circular(View view) {

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

                if(view == layoutPantallaSeleccionarPosicion)
                    efecto_mostrar_circular(bottomBar);

                if(view == bottomBar)
                    btnBack.show();
            }
        });


        view.setVisibility(View.VISIBLE);
        anim.start();

    }


    private boolean comprobarConexion()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null);

    }

}

