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



public class FragmentCodigoTexto extends Fragment
{

    @Nullable @BindView(R.id.editText_texto) TextInputEditText editTexto;
    @Nullable @BindView(R.id.titCodigoTexto) TextView titCodigoTexto;
    @Nullable @BindView(R.id.btnCrearCodigoTexto) MaterialButton btnCrearCodigoTexto;

    private Unbinder unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View viewRoot = inflater.inflate(R.layout.layout_fragment_codigo_texto, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoTexto.setTypeface(fuente_sabo_regular);
        btnCrearCodigoTexto.setTypeface(fuente_sabo_regular);

        return viewRoot;

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    @OnClick(R.id.btnCrearCodigoTexto)
    public void crearCodigoTexto()
    {
        String texto = editTexto.getText().toString().trim();

        if(texto.length()>0)
        {
            ((PantallaQRCreator) getActivity()).crearCodigoQR(texto);
        }else
        {
            Toast.makeText(getContext(), R.string.errorTextoCodigoQR, Toast.LENGTH_LONG).show();
        }

    }

}
