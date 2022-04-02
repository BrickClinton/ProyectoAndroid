package com.example.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.proyecto.entities.Producto;

public class DBAccess extends SQLiteOpenHelper {

    // CONSTANTES
    private static final String NAME_DB = "PROYECTO";
    private static final int VERSION_DB = 1;
    private static final String TABLA_PRODUCTOS = "CREATE TABLE productos (idproducto INTEGER NOT NULL, nombre TEXT NOT NULL, marca TEXT NOT NULL, modelo TEXT NOT NULL, descripcion TEXT, stock INTEGER NOT NULL, precio REAL NOT NULL, descuento REAL, PRIMARY KEY(idproducto AUTOINCREMENT))";

    // CONSTRUCTOR
    public DBAccess(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    // CREACIÓN
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA_PRODUCTOS);
    }

    // ACTUALIZACIÓN
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS productos");
        sqLiteDatabase.execSQL(TABLA_PRODUCTOS);
    }

    // MÉTODO PARA INSERTAR DATOS
    public long registerProduct(Producto producto){
        // Objeto para permitir la escritura en la BD
        SQLiteDatabase db = getWritableDatabase();

        // Almacenrá el id geenrado al realizar el registro
        long idproducto = 0;

        // Validación del acceso
        if(db != null){

            // Objeto que contiene los datos a enviar a la TABLA
            ContentValues parameters = new ContentValues();

            // Colocar los datos dentro del objeto (CLAVE : VALOR)
            parameters.put("nombre", producto.getNombre());
            parameters.put("marca", producto.getMarca());
            parameters.put("modelo", producto.getModelo());
            parameters.put("descripcion", producto.getDescripcion());
            parameters.put("stock", producto.getStock());
            parameters.put("precio", producto.getPrecio());
            parameters.put("descuento", producto.getDescuento());

            // Opcionalmente obtengo el id (PK) generado
            idproducto = db.insert("productos", "idproducto", parameters);

            // Enviando pruebas a la consola
            Log.i("info", "Idproducto generado: " + String.valueOf(idproducto));

            // Cerramos la conexión con la base de datos
            db.close();
        }

        return idproducto;
    }
}
