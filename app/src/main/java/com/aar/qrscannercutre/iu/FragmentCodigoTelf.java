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

public class FragmentCodigoTelf extends Fragment
{

    @Nullable @BindView(R.id.titCodigoTelf) TextView titCodigoTelf;
    @Nullable @BindView(R.id.editTextTelf) TextInputEditText editTextTelf;
    @Nullable @BindView(R.id.btnCrearCodigoTelf) MaterialButton btnCrearCodigoTelf;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View viewRoot = inflater.inflate(R.layout.layout_fragment_codigo_telf, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoTelf.setTypeface(fuente_sabo_regular);
        btnCrearCodigoTelf.setTypeface(fuente_sabo_regular);

        return viewRoot;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


   @OnClick(R.id.btnCrearCodigoTelf)
    public void crearCodigoTelf()
    {

        String telf = editTextTelf.getText().toString().trim();

        if(telf.length()>0)
        {
            String datosCodigo = "tel:"+telf;

            ((PantallaQRCreator) getActivity()).crearCodigoQR(datosCodigo);

        }else
        {
            Toast.makeText(getContext(), R.string.errorTelfCodigoQR, Toast.LENGTH_LONG).show();
        }

    }

}
