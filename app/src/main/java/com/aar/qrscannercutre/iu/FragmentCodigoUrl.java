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

public class FragmentCodigoUrl extends Fragment
{

    @Nullable @BindView(R.id.titCodigoUrl) TextView titCodigoUrl;
    @Nullable @BindView(R.id.editText_Url)  TextInputEditText editTexUrl;
    @Nullable @BindView(R.id.btnCrearCodigoUrl) MaterialButton btnCrearCodigoUrl;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View viewRoot = inflater.inflate(R.layout.layout_fragment_codigo_url, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        Typeface fuente_sabo_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/sabo_regular.otf");
        titCodigoUrl.setTypeface(fuente_sabo_regular);
        btnCrearCodigoUrl.setTypeface(fuente_sabo_regular);

        return viewRoot;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();
    }


    @OnClick(R.id.btnCrearCodigoUrl)
    public void crearCodigoUrl()
    {
        String url = editTexUrl.getText().toString().trim();

        if(url.length()>0)
        {
            ((PantallaQRCreator) getActivity()).crearCodigoQR(url);
        }else
        {
            Toast.makeText(getContext(),R.string.errorUrlCodigoQR, Toast.LENGTH_LONG).show();
        }

    }


}
