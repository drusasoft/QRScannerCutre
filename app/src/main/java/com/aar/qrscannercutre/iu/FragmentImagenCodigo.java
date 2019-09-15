package com.aar.qrscannercutre.iu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aar.qrscannercutre.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentImagenCodigo extends Fragment
{

    @Nullable @BindView(R.id.imgCodigoQR) ImageView imgCodigoQR;
    @Nullable @BindView(R.id.barraNavegacionCodigoQR) BottomNavigationView barraNavegacionCodigoQR;

    private Unbinder unbinder;
    private Bitmap codigoBmp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.layout_fragment_imagen_codigo, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        mostrarCodigoQR();

        barraNavegacionCodigoQR.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.menu_qr_guadar:
                        guardarCodigoQR();
                        break;

                    case R.id.menu_qr_compartir:
                        compartirCodigoQR();
                        break;

                    case R.id.menu_qr_borrar: ((PantallaQRCreator) getActivity()).descartarCodigoQR();
                        break;
                }

                return true;
            }

        });

        return viewRoot;

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    //Se carga el codigoQR creado en el ImageView para mostarlo al usuario
    public void mostrarCodigoQR()
    {
        codigoBmp = ((PantallaQRCreator) getActivity()).getCodigoBmp();

        if(codigoBmp != null)
            imgCodigoQR.setImageBitmap(codigoBmp);
        else
            Toast.makeText(getContext(), R.string.errorCrearCodigoQR, Toast.LENGTH_LONG).show();
    }


    private void compartirCodigoQR()
    {
        ((PantallaQRCreator) getActivity()).compartirCodigoQR(codigoBmp);
    }


    //Se guarda el la imagen del codigoQR en el sistema de archivos del telf y los datos del codigo en la Base de Datos
    private void guardarCodigoQR()
    {
        if(codigoBmp != null)
            ((PantallaQRCreator) getActivity()).guardarCodigoQRBD(codigoBmp);

    }

}
