package com.example.proyecto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Busqueda extends AppCompatActivity implements View.OnClickListener {

    // Definiendo los objetos
    private EditText txtBuscar, txtNombre, txtMarca, txtModelo, txtDescripcion, txtStock, txtPrecio, txtDescuento;
    private Button btnActualizar, btnEliminar, btnBuscar;
    private DBAccess acceso;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        // Creando el objeto  para la conexiòn con la bd
        acceso = new DBAccess(context);

        // Ejecutar el método que carga las interfaces
        loadUI();

        // Los botones estaràn en escucha del evento click
        btnBuscar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
    }

    // Este método carga todas las referencias con el widget (VIEW)
    private void loadUI(){
        // Referenciando con los elementos del view
        txtNombre = findViewById(R.id.etNombreConsulta);
        txtMarca = findViewById(R.id.etMarcaConsulta);
        txtModelo = findViewById(R.id.etModeloConsulta);
        txtDescripcion = findViewById(R.id.etDescripcionConsulta);
        txtStock = findViewById(R.id.etStockConsulta);
        txtPrecio = findViewById(R.id.etPrecioConsulta);
        txtDescuento = findViewById(R.id.etDescuentoConsulta);
        txtBuscar = findViewById(R.id.etIdBuscar);

        btnActualizar = findViewById(R.id.btnActualizarProducto);
        btnEliminar = findViewById(R.id.btnEliminarproducto);
        btnBuscar = findViewById(R.id.btnBuscar);
    }

    @Override
    public void onClick(View view) {
        // Se hizo clic en el btnBuscar
        if(view.getId() == btnBuscar.getId()){
            searchProductById();
        }

        // Se hizo clic en eliminar
        if(view.getId() == btnEliminar.getId()){
            validateDeletion();
        }

        // Actualizar registro
        if(view.getId() == btnActualizar.getId()){
            validateUpdate();
        }
    }

    public void searchProductById(){
        // Objeto que nos brinda acceso a la bd
        SQLiteDatabase db = acceso.getReadableDatabase();

        // Campos usados para la busqueda
        String[] paramConsult = {txtBuscar.getText().toString()};

        // Campos a obtener (segun el orden indicado)
        String[] getFields = {"nombre", "marca", "modelo", "descripcion", "stock", "precio", "descuento"};

        // Control de excepciones
        try{
            // Ejecutar y obtener los datos
            Cursor cursor = db.query("productos", getFields, "idproducto=?", paramConsult, null, null, null);

            // Se encontró registro coincidente
            if(cursor.moveToFirst()){
                // Enviando los datos a las cajas de textos
                txtNombre.setText(cursor.getString(0));
                txtMarca.setText(cursor.getString(1));
                txtModelo.setText(cursor.getString(2));
                txtDescripcion.setText(cursor.getString(3));
                txtStock.setText(cursor.getString(4));
                txtPrecio.setText(cursor.getString(5));
                txtDescuento.setText(cursor.getString(6));
            }
            else{
                // No se encontrarón registros
                resetControlsUI();
                Toast.makeText(context, "No existe el registro buscado", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión
            cursor.close();
        }
        catch(Exception error){
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Este método valida si existe datos en el activity Busqueda.
     * Elimina en caso exista datos de lo contrario Muestra un mensaje de aviso
     */
    private void validateDeletion(){
        if(activityHasNoContent()){
            Toast.makeText(context, "No existen registro por eliminar", Toast.LENGTH_LONG).show();
        }
        else{
            // Crear instancia del dialogo
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            // Configurar contenido del dialogo
            dialog.setTitle("PRODUCTOS")
                    .setMessage("¿Estas seguro de eliminar el registro?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteRow(); // Eliminar
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Por definir
                        }
                    });

            // Mostrar dialogo
            dialog.show();
        }
    }

    /**
     * Este método contiene la logica para eliminar un registro
     */
    private void deleteRow(){
        // Obteniendo acceso de escritura
        SQLiteDatabase db = acceso.getWritableDatabase();

        // Definir array con los parametros de entrada
        String[] parameters = {txtBuscar.getText().toString()};

        // Ejecutar proceso
        int value = db.delete("productos", "idproducto=?", parameters);

        // Confirmar eliminación
        if(value > 0){
            Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            resetControlsUI();
        }else{
            Toast.makeText(context, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión
        db.close();
    }

    /**
     * Este método permite resetear los datos que se muestran (EdiText) en el activity de Busqueda
     */
    private void resetControlsUI(){
        txtBuscar.setText(null);
        txtNombre.setText(null);
        txtMarca.setText(null);
        txtModelo.setText(null);
        txtDescripcion.setText(null);
        txtStock.setText(null);
        txtPrecio.setText(null);
        txtDescuento.setText(null);
    }

    /**
     * Este método valida si existe datos en el activity Busqueda.
     * Actualiza en caso exista datos de lo contrario Muestra un mensaje de aviso
     */
    private void validateUpdate(){
        if(activityHasNoContent()){
            Toast.makeText(context, "No existen datos por actualizar o están incompleto", Toast.LENGTH_LONG).show();
        }
        else{
            // Creando el objeto de dialogo
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            // Configurar los datos del dialog
            dialog.setTitle("PRODUCTOS")
                    .setMessage("¿Estas seguro de actualizar el registro?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateRow(); // Actualizar
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            // Mostrar dialogo
            dialog.show();
        }
    }

    /**
     * Este método permite actualizar los datos del registro buscado
     */
    private void updateRow(){
        // Objeto que permite la esritura de datos
        SQLiteDatabase db = acceso.getWritableDatabase();

        // Parametros
        String[] parameters = {txtBuscar.getText().toString()};

        // Contenido de los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("nombre", txtNombre.getText().toString());
        values.put("marca", txtMarca.getText().toString());
        values.put("modelo", txtModelo.getText().toString());
        values.put("descripcion", txtDescripcion.getText().toString());
        values.put("precio", txtPrecio.getText().toString());
        values.put("descuento", txtDescuento.getText().toString());

        int value = db.update("productos", values, "idproducto=?", parameters);

        // Validar si se pudo actualizar el registro
        if(value > 0){
            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión
        db.close();
    }

    // Verifica si los editext tienen datos

    /**
     * Este método verifica si los EdiText del activity Busqueda contiene datos.
     * @return True si los ediText no tienen datos, caso contrario False
     */
    public boolean activityHasNoContent(){
        return txtNombre.getText().toString().isEmpty() || txtMarca.getText().toString().isEmpty()  || txtModelo.getText().toString().isEmpty() || txtStock.getText().toString().isEmpty() || txtPrecio.getText().toString().isEmpty();
    }
}