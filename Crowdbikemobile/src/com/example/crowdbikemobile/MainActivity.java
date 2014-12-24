package com.example.crowdbikemobile;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{
	
	private LocationManager locationManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }
    
    @Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		
		// Requisitando posi��o geogr�fica do GPS ou da rede
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}



    /*
     * Os m�todos a seguir ser�o usados para capturar a posi��o geogr�fica do aparelho 
     * 
     */
	
	/**
	 * Este m�todo � notificado sempre que a posi��o geogr�fica do aparelho
	 * for atualizada.
	 * 
	 * A ideia � que toda vez que esse m�todo for chamado pelo sistema, o 
	 * aparelho user uma asynctask para enviar a posi��o geogr�fica 
	 * para o servidor
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {
		
		//int latitude  = (int)(location.getLatitude() * 1E6);
		//int longitude = (int)(location.getLongitude() * 1E6);

		double latitude  = location.getLatitude();
		double longitude = location.getLongitude();
		
		// Exibindo as novas coordenadas na activity 
		TextView txtLatitude  = (TextView) findViewById(R.id.latitude);
		TextView txtLongitude = (TextView) findViewById(R.id.longitude);
        
        String latitudeString  = String.valueOf(latitude);
        String longitudeString = String.valueOf(longitude);
        
        txtLatitude.setText("latitude: "   + latitudeString);
        txtLongitude.setText("longitude: " + longitudeString);

        // Instanciando a asynctask para contato com o servidor e acesso ao ardu�no
        ContatoComServidor task = new ContatoComServidor(this);
        task.execute(latitudeString, longitudeString);
        
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}


	@Override
	public void onProviderEnabled(String provider) {
	}


	@Override
	public void onProviderDisabled(String provider) {
	}
        
}
