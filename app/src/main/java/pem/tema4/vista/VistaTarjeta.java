package pem.tema4.vista;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pem.tema4.AppMediador;

public class VistaTarjeta extends AppCompatActivity implements IVistaTarjeta {

    private AppMediador appMediador;
    private FloatingActionButton fab;
    private RecyclerView reciclador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);
        appMediador = (AppMediador)getApplication();
        appMediador.setVistaTarjeta(this);

        // TODO Cree el reciclador, fijar el tamaño y añadirle un linear layout manager
        reciclador = (RecyclerView)findViewById(R.id.reciclador);
        reciclador.setHasFixedSize(true);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);
        reciclador.setLayoutManager(llm);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMediador.getPresentadorTarjeta().actualizarTarjetas();
            }
        });
        appMediador.getPresentadorTarjeta().actualizarTarjetas();
    }



    // TODO Actulizar la información (datos) en la interfaz de usuario. El parámetro es un array de Object que almacena:
    // 0: nombre del lugar
    // 1: temperatura del lugar (actual + 5 días de previsión)
    // 2: presión del lugar (actual + 5 días de previsión)
    // 3: humedad del lugar (actual + 5 días de previsión)
    // 4: velocidad del viento del lugar (actual + 5 días de previsión)
    // 5: dirección del viento del lugar (actual + 5 días de previsión)
    // 6: tipoTiempo del clima (tipo del clima) del lugar (actual + 5 días de previsión)

    @Override
    public void actualizarInformacion(Object datos) {
        Adaptador adaptador = new Adaptador(appMediador.getApplicationContext(), datos);
        reciclador.setAdapter(adaptador);
    }

}
