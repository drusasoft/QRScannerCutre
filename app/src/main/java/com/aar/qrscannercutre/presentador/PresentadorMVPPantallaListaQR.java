package com.aar.qrscannercutre.presentador;

import com.aar.qrscannercutre.pojos.Codigo;

import java.util.List;

public interface PresentadorMVPPantallaListaQR extends PresentadorBase
{
    List<Codigo> getListaCodigosQR();
    void eliminarCodigosQR(List<Codigo> listaCodigosSelec);
}
