package pem.tema4.vista;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

// TODO Extender la clase según lo explicado en teoría.
public class Adaptador extends RecyclerView.Adapter<Adaptador.FilaViewHolder>{

    private static Object[] items;
    private static float anguloActual;
    private static ImageView condicion, vDViento;
    private static Context contexto;
    private int numeroDePrevisiones;

    // En el array datos se almacena lo siguiente:
    // 0: String correspondiente al nombre del lugar de muestra
    // 1: double[] con las temperaturas actual + 5 días de previsión
    // 2: double[] con la presión actual + 5 días de previsión
    // 3: double[] con la humedad actual + 5 días de previsión
    // 4: double[] con la velocidad del viento actual + 5 días de previsión
    // 5: double[] con la dirección del viento actual + 5 días de previsión
    // 6: String[] con el tipo de tiempo actual + 5 días de previsión
    public Adaptador(Context contexto, Object datos) {
        items = (Object[])datos;
        Adaptador.contexto = contexto;
        anguloActual = 0;
        numeroDePrevisiones = ((double[])items[1]).length;
    }

    // TODO Infle el layout de la tarjeta y retorno el objeto oportuno, tal y como se explicó en teoría.
    @Override
    public FilaViewHolder onCreateViewHolder(ViewGroup padre, int viewType) {
        View view = LayoutInflater.from(padre.getContext()).inflate(R.layout.layout_vista_tarjeta, padre, false);
        return new FilaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilaViewHolder fvh, int posicion) {
        fvh.bindFila(posicion);
    }

    @Override
    public int getItemCount() {
        return numeroDePrevisiones; // número de días de previsión de tiempo
    }


    public static class FilaViewHolder extends RecyclerView.ViewHolder {

        private TextView lugar, fecha, vTemperatura, vPresion, vHumedad, vVelViento;

        // TODO Dentro del constructor, recupere los objetos a modificar dentro de su layout (su layout es
        // layout_vista_tarjeta).
        public FilaViewHolder(View view) {
            super(view);
            lugar = (TextView)view.findViewById(R.id.lugar);
            fecha = (TextView)view.findViewById(R.id.fecha);
            vTemperatura = (TextView)view.findViewById(R.id.vTemperatura);
            vPresion = (TextView)view.findViewById(R.id.vPresion);
            vHumedad = (TextView)view.findViewById(R.id.vHumedad);
            vVelViento = (TextView)view.findViewById(R.id.vVViento);
            vDViento = (ImageView)view.findViewById(R.id.vDViento);
            condicion = (ImageView)view.findViewById(R.id.imagen);
        }

        public void bindFila(int posicion) {
            String nombre = (String)items[0];
            double temperatura = ((double[])items[1])[posicion];
            double presion = ((double[])items[2])[posicion];
            double humedad = ((double[])items[3])[posicion];
            double velViento = ((double[])items[4])[posicion];
            double dirViento = ((double[])items[5])[posicion];
            String tipoTiempo = ((String[])items[6])[posicion];
            lugar.setText(nombre);
            Calendar calendar = Calendar.getInstance();
            String fechaInicial = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/" +
                    calendar.get(Calendar.YEAR);
            if (posicion == 0)
                fecha.setText(fechaInicial);
            else if (posicion == 1)
                fecha.setText(fechaInicial + " + 1 día");
            else
                fecha.setText(fechaInicial + " + " + posicion + " días");
            DecimalFormat decimales = new DecimalFormat("0.00");
            vTemperatura.setText(decimales.format(temperatura)+"");
            vPresion.setText(decimales.format(presion)+"");
            vHumedad.setText(decimales.format(humedad)+"");
            vVelViento.setText(decimales.format(velViento)+"");
            condicion.setImageDrawable(identificadorImagen(tipoTiempo));
            moverDViento((float)dirViento);
        }

        private void moverDViento(float angulo) {
            float degree = Math.round(angulo);
            RotateAnimation ra = new RotateAnimation(
                    anguloActual,
                    -angulo,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            ra.setDuration(210);
            ra.setFillAfter(true);
            vDViento.startAnimation(ra);
            anguloActual = -angulo;
        }

        private Drawable identificadorImagen(String condicion) {
            if (condicion == null)
                return null;
            switch (condicion) {
                case "Rain": return contexto.getResources().getDrawable(R.drawable.lluvia, null);
                case "Snow": return contexto.getResources().getDrawable(R.drawable.nieve, null);
                case "Clouds": return contexto.getResources().getDrawable(R.drawable.nuboso, null);
                case "Clear": return contexto.getResources().getDrawable(R.drawable.sol, null);
                case "Mist": return contexto.getResources().getDrawable(R.drawable.niebla, null);
            }
            return null;
        }
    }


}
