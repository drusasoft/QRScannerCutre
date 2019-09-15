package com.aar.qrscannercutre.iu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aar.qrscannercutre.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentCodigoGeo extends Fragment
{

    @Nullable @BindView(R.id.titCodigoGeo) TextView titCodigoGeo;
    @Nullable @BindView(R.id.editTextLatitud) TextInputEditText editTextLatitud;
    @Nullable @BindView(R.id.editTextLongitud) TextInputEditText editTextLongitud;
    @Nullable @BindView(R.id.btnCrearCodigoGeo) MaterialButton btnCrearCodigoGeo;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.layout_fragment_codigo_geo, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoGeo.setTypeface(fuente_sabo_regular);
        btnCrearCodigoGeo.setTypeface(fuente_sabo_regular);

        return rootView;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    @OnClick(R.id.btnCrearCodigoGeo)
    public void crearCodigoGeo()
    {

        String latitud = editTextLatitud.getText().toString().trim();
        String longitud = editTextLongitud.getText().toString().trim();

        if(latitud.length()>0 && longitud.length()>0)
        {
            String datosCodigo = "geo:"+latitud+","+longitud;

            ((PantallaQRCreator) getActivity()).crearCodigoQR(datosCodigo);

        }else
        {
            Toast.makeText(getContext(), R.string.errorGeoCodigoQR, Toast.LENGTH_LONG).show();
        }

    }


    @OnClick(R.id.titIrMapa)
    public void irPantallaMapa()
    {
        editTextLatitud.setText("");
        editTextLongitud.setText("");
        ((PantallaQRCreator) getActivity()).irPantallaMapa();
    }


    //Este metodo es llamado desde la pantalla QRCreator cuando el usaurio ha seleccionado un punto en el mapa
    //pone las coordenadas de dicho punto en los TextInputEditText
    public void setLatLng(String lat, String lng)
    {
        editTextLatitud.setText(lat);
        editTextLongitud.setText(lng);
    }


}
