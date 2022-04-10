package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Definiendo objetos
    private Button btnBusqueda, btnRegistrar, btnListar;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cargar las vistas
        loadUI();

        // Los botones estaran en escucha del evento clic
        btnRegistrar.setOnClickListener(this);
        btnBusqueda.setOnClickListener(this);
        btnListar.setOnClickListener(this);
    }

    // Cargar las interfaces referenciados
    private void loadUI(){
        // Referenciando con los elementos del view
        btnBusqueda = findViewById(R.id.btnActivityBusqueda);
        btnRegistrar = findViewById(R.id.btnActivityRegistro);
        btnListar = findViewById(R.id.btnActivityLista);
    }

    @Override
    public void onClick(View view) {

        // Mostrar activity de busqueda
        if(view.getId() == btnBusqueda.getId()){
            Intent intent = new Intent(context, Busqueda.class);
            startActivity(intent);
        }

        // Mostrar activity de registro
        if(view.getId() == btnRegistrar.getId()){
            Intent intent = new Intent(context, Registro.class);
            startActivity(intent);
        }

        // Mostrar activity de Listado
        if(view.getId() == btnListar.getId()){
            Intent intent = new Intent(context, Lista.class);
            startActivity(intent);
        }
    }
}