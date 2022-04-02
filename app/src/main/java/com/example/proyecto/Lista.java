package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.proyecto.entities.Producto;

import java.util.ArrayList;

public class Lista extends AppCompatActivity {

    // Definiendo objetos
    private ListView listViewProducto;
    private ArrayList<Producto> listProductos;
    private ArrayList<String> listDataShow;
    Context context = this;
    DBAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        // Iniciar el acceso con lqa base de datos
        access = new DBAccess(context);

        // Referenciando con el view
        loadUI();

        // cargar los datos en el ArrayList
        queryData();

        // Haciendo uso del arrayAdapter
        ArrayAdapter adaptaer = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listDataShow);

        // Agregar contenido al listView
        listViewProducto.setAdapter(adaptaer);

        // Añadiendo evento ClickLister al listView
        listViewProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String message = "";
                message += "Marca: " + listProductos.get(position).getMarca() + "\n";
                message += "Modelo: " + listProductos.get(position).getModelo() + "\n";
                message += "Precio: " + listProductos.get(position).getPrecio();

                // Mostrar datos en un Toast
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Se encarga de cargar todas las referencias con los widgets (interfaz)
     */
    private void loadUI(){
        // Referenciando con el view
        listViewProducto = findViewById(R.id.lvProductos);
    }

    /**
     * Trae los registros de la tabla productos y
     * los almacena como objetos dentro del
     * ArrayList<Producto>
     */
    private void queryData(){
        // Solicitar acceso de tipo lectura
        SQLiteDatabase db = access.getReadableDatabase();

        // Definiendo espacio en memoria para la entidad - Producto
        Producto producto;

        //Instanciando el ArrayList de tipo Producto
        listProductos = new ArrayList<>();

        // Desencadenar la consulta y traer los resultados
        Cursor cursor = db.rawQuery("SELECT * FROM productos", null);

        // Leer los datos obtenidos por la consulta anterior
        while (cursor.moveToNext()){
            // Instanciando la entidad
            producto = new Producto();

            // Envuiar los datos del cursor a la ENTIDAD
            producto.setIdproducto(cursor.getInt(0));
            producto.setNombre(cursor.getString(1));
            producto.setMarca(cursor.getString(2));
            producto.setModelo(cursor.getString(3));
            producto.setDescripcion(cursor.getString(4));
            producto.setStock(cursor.getInt(5));
            producto.setPrecio(cursor.getDouble(6));
            producto.setDescuento(cursor.getDouble(7));

            // Añadiendo los objetos a la lista de productos
            listProductos.add(producto);
        }

        // Obtenr la lista de registros en formato Strings
        getStringsList();
    }

    /**
     * Este método obtiene los datos del ArrayList<Producto>
     * y los pasa al ArrayList<String> [necesario para el adaptador]
     */
    private void getStringsList(){
        // Instanciar el arrayList de String
        listDataShow = new ArrayList<>();

        // Pasar los datos de un array a otro
        for (int i = 0; i < listProductos.size(); i++){
            listDataShow.add(listProductos.get(i).getIdproducto() + " - " + listProductos.get(i).getNombre() + ", " + listProductos.get(i).getMarca());
        }
    }

}