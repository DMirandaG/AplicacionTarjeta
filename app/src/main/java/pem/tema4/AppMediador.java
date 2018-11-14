package pem.tema4;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import pem.tema4.presentador.IPresentadorTarjeta;
import pem.tema4.presentador.PresentadorTarjeta;
import pem.tema4.vista.IVistaTarjeta;

@SuppressWarnings("rawtypes") 
public class AppMediador extends Application {
	private static AppMediador singleton;
	private IPresentadorTarjeta presentadorTarjeta;
	private IVistaTarjeta vistaTarjeta;

	// variables correspondientes a los presentadores, vistas y modelo

	// constantes de comunicación, almacenamiento y petición
	public static final String UNIDAD = "metric"; // los grados de la temperatura se miden en Celsius
	public static final String CLAVE = "b7f61c141a936a171b60327be1c12fb5";  // clave de open weather (ésta es la mía)

	public static final String AVISO_NUEVA_INFORMACION = "pem.tema4.vista.AVISO_NUEVA_INFORMACION";

	public static final String CLAVE_INFORMACION = "CLAVE_INFORMACION";

	
	public static AppMediador getInstance(){
		return singleton;
	}

	// Métodos accessor de los presentadores, vistas y modelo

	public IPresentadorTarjeta getPresentadorTarjeta() {
		if (presentadorTarjeta == null)
			presentadorTarjeta = new PresentadorTarjeta();
		return presentadorTarjeta;
	}
	
	public void removePresentadorTarjeta() {
		presentadorTarjeta = null;
	}
	
	public IVistaTarjeta getVistaMapa() {
		return vistaTarjeta;
	}

	public void setVistaTarjeta(IVistaTarjeta vistaTarjeta) {
		this.vistaTarjeta = vistaTarjeta;
	}

	// Métodos destinados a la navegación en la aplicación y a la definición de servicios


	// Métodos de manejo de los componentes de Android
	public void launchActivity(Class actividadInvocada, Object invocador, Bundle extras) {
		Intent i = new Intent(this, actividadInvocada);
		if (extras != null)
			i.putExtras(extras);	
		if (!invocador.getClass().equals(Activity.class)) 
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	
	public void launchActivityForResult(Class actividadInvocada, 
			Activity actividadInvocadora, int requestCode, Bundle extras) {
		Intent i = new Intent(actividadInvocadora, actividadInvocada);
		if (extras != null)
			i.putExtras(extras);
		actividadInvocadora.startActivityForResult(i, requestCode);
	}
	
	public void launchService(Class servicioInvocado, Bundle extras) {
		Intent i = new Intent(this, servicioInvocado);
		if (extras != null)
			i.putExtras(extras);
        startService(i);
	}
	
	public void stopService(Class servicioInvocado) {
		Intent i = new Intent(this, servicioInvocado);
        stopService(i);
	}
	
	public void registerReceiver(BroadcastReceiver receptor, String accion) {
		LocalBroadcastManager.getInstance(this).registerReceiver(receptor, new IntentFilter(accion));
	}	
	
	public void unRegisterReceiver(BroadcastReceiver receptor) {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receptor);
	}
	
	public void sendBroadcast(String accion, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(accion);
		if (extras != null)
			intent.putExtras(extras);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		presentadorTarjeta = null;
		singleton = this;
	}
}
