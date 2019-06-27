package com.aar.qrscannercutre.iu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.aar.qrscannercutre.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PantallaMapa extends AppCompatActivity implements OnMapReadyCallback
{

    @BindView(R.id.layoutParentPantallaMapa) CoordinatorLayout layoutParentPantallaMapa;
    @BindView(R.id.bottomBarMapa) BottomAppBar bottomAppBar;
    @BindView(R.id.floatingBtnBack) FloatingActionButton floatingBtnBack;

    private GoogleMap mapa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mapa);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaQR);

        mapFragment.getMapAsync(this);

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mapa = googleMap;

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
