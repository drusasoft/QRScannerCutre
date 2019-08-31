package com.aar.qrscannercutre.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.aar.qrscannercutre.pojos.Codigo;

import java.util.ArrayList;
import java.util.List;

public class DataManagerDataBase
{

    private final String crearTablaCodigos = "Create Table TablaCodigos(id_codigo Integer PRIMARY KEY AUTOINCREMENT, " +
            "nombre String, nombre_imagen String, tipo String, path String, fecha String)";
    private final String eliminarTablaCodigos = "DROP TABLE IF EXISTS TablaCodigos";
    private final String nombreBD = "codigosBD";
    private int versionBD = 1;

    private SQLiteDatabase db;
    private CodigosSQLiteHelper codigosSQLiteHelper;



    public DataManagerDataBase(Context context)
    {
        codigosSQLiteHelper = new CodigosSQLiteHelper(context, nombreBD, null, versionBD);
    }


    //Se inserta un codigo en la BD
    public void addCodigo(Codigo codigo)
    {
        abrirBDEscritura();

        ContentValues values = new ContentValues();
        values.put("nombre", codigo.getNombre());
        values.put("nombre_imagen", codigo.getNombre_imagen());
        values.put("tipo", codigo.getTipo());
        values.put("path", codigo.getPath());
        values.put("fecha", codigo.getFecha());

        db.insert("TablaCodigos", null, values);

        cerrarBD();
    }


    //Se obtiene la lista de codigos guardada en la BD
    public List<Codigo> getCodigos()
    {

        abrirBDLectura();

        List<Codigo> listaCodigos = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * from TablaCodigos", null);

        if(cursor.getCount()>0)
        {

            Codigo codigo;
            cursor.moveToFirst();

            do {

                codigo = new Codigo();
                codigo.setNombre(cursor.getString(1));
                codigo.setNombre_imagen(cursor.getString(2));
                codigo.setTipo(cursor.getString(3));
                codigo.setPath(cursor.getString(4));
                codigo.setFecha(cursor.getString(5));

                listaCodigos.add(codigo);

            }while (cursor.moveToNext());

        }

        cerrarBD();

        Log.e("ListaCodigos Size", String.valueOf(listaCodigos.size()));
        for(Codigo codigo:listaCodigos)
            Log.e("Codigo", "Nombre:"+codigo.getNombre()+"--NombreImagen:"+codigo.getNombre_imagen()+"--Tipo:"+codigo.getTipo()+
            "--Path:"+codigo.getPath()+"--Fecha:"+codigo.getFecha());

        return listaCodigos;

    }


    //Se elimina el codigo de la BD
    public void delCodigo(Codigo codigo)
    {

    }


    //Se abre la BD en modo Lectura
    private void abrirBDLectura()
    {
        db = codigosSQLiteHelper.getReadableDatabase();
    }


    //Se abre la BD en modo Escritura
    private void abrirBDEscritura()
    {
        db = codigosSQLiteHelper.getWritableDatabase();
    }

    private void cerrarBD()
    {
        db.close();
    }


    private class CodigosSQLiteHelper extends SQLiteOpenHelper
    {

        public CodigosSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(crearTablaCodigos);
            Log.e("CodigosSQLiteHelper","onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

        }
    }

}
