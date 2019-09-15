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

public class FragmentCodigoSms extends Fragment
{

    @Nullable @BindView(R.id.titCodigoSms) TextView titCodigoSms;
    @Nullable @BindView(R.id.editTextSms_telf)  TextInputEditText editTextSmsTelf;
    @Nullable @BindView(R.id.editTextSms_texto) TextInputEditText editTextSmsTexto;
    @Nullable @BindView(R.id.btnCrearCodigoSms) MaterialButton btnCrearCodigoSms;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.layout_fragment_codigo_sms, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoSms.setTypeface(fuente_sabo_regular);
        btnCrearCodigoSms.setTypeface(fuente_sabo_regular);

        return rootView;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    @OnClick(R.id.btnCrearCodigoSms)
    public void crearCodigoSms()
    {

        String telf = editTextSmsTelf.getText().toString().trim();
        String texto = editTextSmsTexto.getText().toString().trim();

        if(telf.length()>0 && texto.length()>0)
        {
            String datosCodigo = "smsto:"+telf+":"+texto;
            ((PantallaQRCreator) getActivity()).crearCodigoQR(datosCodigo);

        }else
        {
            Toast.makeText(getContext(),R.string.errorSmsCodigoQR, Toast.LENGTH_LONG).show();
        }

    }

}
