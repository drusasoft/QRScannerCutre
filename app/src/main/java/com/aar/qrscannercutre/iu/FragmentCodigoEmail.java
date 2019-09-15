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

public class FragmentCodigoEmail extends Fragment
{

    @Nullable @BindView(R.id.titCodigoEmail) TextView titCodigoEmail;
    @Nullable @BindView(R.id.editTextEmail_to) TextInputEditText editTextEmailTo;
    @Nullable @BindView(R.id.editTextEmail_subject) TextInputEditText editTextEmailSubject;
    @Nullable @BindView(R.id.editTextEmail_body) TextInputEditText editTextEmailBody;
    @Nullable @BindView(R.id.btnCrearCodigoEmail) MaterialButton btnCrearCodigoEmail;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View viewRoot = inflater.inflate(R.layout.layout_fragment_codigo_email, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoEmail.setTypeface(fuente_sabo_regular);
        btnCrearCodigoEmail.setTypeface(fuente_sabo_regular);

        return viewRoot;

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    @OnClick(R.id.btnCrearCodigoEmail)
    public void crearCodigoEmail()
    {

        String textTo = editTextEmailTo.getText().toString().trim();
        String textSubject = editTextEmailSubject.getText().toString().trim();
        String textBody = editTextEmailBody.getText().toString().trim();

        if(textTo.length()>0)
        {
            String datosCodigo = "mailto:"+textTo+"?subject="+textSubject+"&body="+textBody;

            ((PantallaQRCreator) getActivity()).crearCodigoQR(datosCodigo);

        }else
        {
            Toast.makeText(getContext(), R.string.errorEmailCodigoQR, Toast.LENGTH_LONG).show();
        }

    }


}
