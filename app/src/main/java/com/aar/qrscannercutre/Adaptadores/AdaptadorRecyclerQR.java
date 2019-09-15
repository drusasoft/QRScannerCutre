package com.aar.qrscannercutre.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.pojos.Codigo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdaptadorRecyclerQR extends RecyclerView.Adapter<AdaptadorRecyclerQR.QRViewHolder>
{

    private List<Codigo> listaCodigos;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;


    public AdaptadorRecyclerQR(List<Codigo> listaCodigos)
    {
        this.listaCodigos = listaCodigos;
    }


    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_listaqr, parent, false);

        itemView.setOnClickListener(clickListener);
        itemView.setOnLongClickListener(longClickListener);

        QRViewHolder qrViewHolder = new QRViewHolder(itemView, parent.getContext());

        return qrViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position)
    {
        holder.bind(listaCodigos.get(position));
    }

    @Override
    public int getItemCount()
    {
        return listaCodigos.size();
    }

    public void setOnClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener)
    {
        this.longClickListener = longClickListener;
    }

    public static class QRViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.layoutParentRecycler) LinearLayout layoutParentRecycler;
        @BindView(R.id.layoutImageRecycler) FrameLayout layoutImageRecycler;
        @BindView(R.id.txtRecyclerNombreQR) TextView txtRecyclerNombreQR;
        @BindView(R.id.txtRecyclerTipoQR) TextView txtRecyclerTipoQR;
        @BindView(R.id.imgViewRecyclerQR) ImageView imgViewRecyclerQR;
        @BindView(R.id.progressRecycler) ProgressBar progressRecycler;

        @BindColor(R.color.colorHint) int colorAmarillo;
        @BindColor(R.color.transparente) int colorTransparente;

        private Context context;
        private String path;

        public QRViewHolder(@NonNull View itemView, Context context)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.context = context;
        }

        public void bind(Codigo codigo)
        {
            txtRecyclerNombreQR.setText(codigo.getNombre());
            txtRecyclerTipoQR.setText(codigo.getTipo());

            path = "file://"+codigo.getPath();

            //Se carga la imagen con picasso
            Picasso.get()
                    .load(path)
                    .error(R.drawable.img_error)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .into(imgViewRecyclerQR, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressRecycler.setVisibility(View.INVISIBLE);
                            imgViewRecyclerQR.setTag("load_OK");
                        }

                        @Override
                        public void onError(Exception e) {
                            progressRecycler.setVisibility(View.INVISIBLE);
                            imgViewRecyclerQR.setTag("load_KO");
                        }
                    });

            if(codigo.isSeleccionado())
            {
                layoutParentRecycler.setBackgroundResource(R.drawable.borde_recycler_qr_selecc);
                txtRecyclerNombreQR.setTextColor(colorAmarillo);
                txtRecyclerTipoQR.setTextColor(colorAmarillo);
                layoutImageRecycler.setBackgroundColor(colorAmarillo);
            }
            else
            {
                layoutParentRecycler.setBackgroundResource(R.drawable.borde_recycler_qr);
                txtRecyclerNombreQR.setTextColor(context.getResources().getColorStateList(R.color.color_texto_recycler, null));
                txtRecyclerTipoQR.setTextColor(context.getResources().getColorStateList(R.color.color_texto_recycler, null));
                layoutImageRecycler.setBackgroundColor(colorTransparente);
            }

        }

    }

}
