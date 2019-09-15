package com.aar.qrscannercutre.presentador;

import com.aar.qrscannercutre.R;
import com.aar.qrscannercutre.iu.VistaBase;
import com.aar.qrscannercutre.iu.VistaPantallaListaQR;
import com.aar.qrscannercutre.modelo.DataManagerDataBase;
import com.aar.qrscannercutre.pojos.Codigo;
import java.io.File;
import java.util.List;

public class PresentadorPantallaListaQR implements PresentadorMVPPantallaListaQR
{

    private VistaPantallaListaQR vista;
    private DataManagerDataBase dataManagerDataBase;

    public PresentadorPantallaListaQR(DataManagerDataBase dataManagerDataBase)
    {
        this.dataManagerDataBase = dataManagerDataBase;
    }

    @Override
    public void setVista(VistaBase vista)
    {
        this.vista = (VistaPantallaListaQR) vista;
    }


    @Override
    public List<Codigo> getListaCodigosQR()
    {
        return dataManagerDataBase.getCodigos();
    }

    @Override
    //Se eliminan los codigosQR seleccionados del sistema de archivos,
    // y tambien se elimina su informacion de la BD local
    public void eliminarCodigosQR(List<Codigo> listaCodigosSelec)
    {

        for(Codigo codigo:listaCodigosSelec)
        {
            try
            {
                File fileCodigo = new File(codigo.getPath());

                if(fileCodigo.exists())
                    fileCodigo.delete();

                dataManagerDataBase.delCodigo(codigo);//Se borra de la BD

            }catch (Exception exception)
            {
                vista.mostrarMensajeError(R.string.errorEliminarCodigo);
            }

        }

        //Se llama al metodo de la Activity que obtiene de la BD la informacion de los codigosQR guardados y los muestra en el Recycler
        vista.mostrarListaCodigosQR();
    }


}
