package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.entities.Producto;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    // Definiendo objetos
    private EditText txtNombre, txtMarca, txtModelo, txtDescripcion, txtStock, txtPrecio, txtDescuento;
    private Button btnRegistrar;
    private Context context = this;
    private DBAccess acceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Instancia del acceso a la base de datos
        acceso = new DBAccess(context);

        // cargar la vista referenciados
        loadUI();

        // Los botones estarán en escucha del evento clic
        btnRegistrar.setOnClickListener(this);
    }

    // Cargar la vista
    private void loadUI(){
        // Referenciando con los componentes del view
        txtNombre = findViewById(R.id.etNombre);
        txtMarca = findViewById(R.id.etMarca);
        txtModelo = findViewById(R.id.etModelo);
        txtDescripcion = findViewById(R.id.etDescripcion);
        txtStock = findViewById(R.id.etStock);
        txtPrecio = findViewById(R.id.etPrecio);
        txtDescuento = findViewById(R.id.etDescuento);

        btnRegistrar = findViewById(R.id.btnRegistrarProducto);
    }

    @Override
    public void onClick(@NonNull View view) {
        // Se pulso el btnRegistrar
        if(view.getId() == btnRegistrar.getId()){
            validateRegister();
        }

    }

    // Este método valida si existen datos en el activity de registro
    public void validateRegister(){
        // validar los campos
        if(activityHasNoContent()){
            // No existen datos en el activity
            Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            // Objeto duialogo
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("PRODUCTOS")
                    .setMessage("¿Estas seguro de guardar los datos?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            registerProduct(); // Registrar
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

    // Este método permite enviar los datos a la base de datos
    public void registerProduct(){
        // Instanciando a la entidad de producto
        Producto producto = new Producto();

        // obteniendo los datos de las cajas de texto
        producto.setNombre(txtNombre.getText().toString());
        producto.setMarca(txtMarca.getText().toString());
        producto.setModelo(txtModelo.getText().toString());
        producto.setDescripcion(txtDescripcion.getText().toString());
        producto.setStock(Integer.parseInt(txtStock.getText().toString()));
        producto.setPrecio(Double.parseDouble(txtPrecio.getText().toString()));
        producto.setDescuento(Double.parseDouble(txtDescuento.getText().toString()));

        // procediendo a registrar
        long value = acceso.registerProduct(producto);
        resetControlsUI();

        if(value > 0){
            Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Ocurrio un problema al registrar", Toast.LENGTH_SHORT).show();
        }

    }

    // Verifica si los editext tienen datos
    public boolean activityHasNoContent(){
        return txtNombre.getText().toString().isEmpty() || txtMarca.getText().toString().isEmpty()  || txtModelo.getText().toString().isEmpty() || txtStock.getText().toString().isEmpty() || txtPrecio.getText().toString().isEmpty();
    }

    // Resetea los datos de la interfaz
    public void resetControlsUI(){
        txtNombre.setText(null);
        txtMarca.setText(null);
        txtModelo.setText(null);
        txtDescripcion.setText(null);
        txtStock.setText(null);
        txtPrecio.setText(null);
        txtDescuento.setText(null);
    }
}