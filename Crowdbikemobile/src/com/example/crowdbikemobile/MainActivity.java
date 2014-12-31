package com.example.crowdbikemobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	private String latitudeString  = "";
	private String longitudeString = "";
	private Tempo tempoLocal = new Tempo();
	GPSTracker gps;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        //Captando a posi��o geogr�fica
        posicaoGeografica();
        
        //Executando tarefas paralelas
        tarefasParalelas();
        
        //Setando a cor de fundo. Padr�o: verde
        setarCorDeFundo(R.color.verde);
    }

    @Override
	protected void onPause() {
		super.onPause();
	}
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
    /**
     * Este m�todo seta a cor de fundo do aplicativo
     * @param cor	C�digo inteiro da cor. A lista de cores dispon�veis est� em res/calues/colors.xml
     */
    public void setarCorDeFundo(int cor){
    	View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(cor));
    }
    
    /**
     * Este m�todo identifica a posi��o geogr�fica do aparelho
     * A posi��o � armazenada nos atributos privados latitudeString e longitudeString,
     * 
     */
    public void posicaoGeografica(){
    	
        //Instancia um objeto GPSTracker
        gps = new GPSTracker(MainActivity.this);

        // Checa se o GPS est� habilitado
 		if(gps.canGetLocation()){

 			latitudeString  = String.valueOf(gps.getLatitude());
 			longitudeString = String.valueOf(gps.getLongitude());
 			
 			Toast.makeText(getApplicationContext(), "Sua localiza��o � - \nLat: " + latitudeString + "\nLong: " + longitudeString, Toast.LENGTH_LONG).show();
 			tarefasParalelas();
 			
 		}else{
 			/* 
 			 * N�o pode pegar a localiza��o
 			 * GPS or Network est� desabilitado
 			 * Pede para o usu�rio habilitar
 			 */
 			gps.showSettingsAlert();
 		}
    }
    
    /**
     * Este m�todo executa as tarefas paralelas para:
     *  - Acesso ao servidor
     *  - Receber informa��es de tempo
     */
    public void tarefasParalelas(){

        //Instanciando a asynctask para contato com o servidor e acesso ao ardu�no
        AsyncServidor task = new AsyncServidor(this);
        //task.execute(latitudeString, longitudeString);
        
        //Instanciando a asynktask para contato com o servi�o de tempo
        AsyncTempo tempo = new AsyncTempo(this);
        tempo.execute(latitudeString, longitudeString);
    }
    
	/**
	 * Este m�todo seta o atributo tempoMain.
	 * Este atributo armazena as informa��es de tempo
	 * 
	 * @param tempoMain	objeto Tempo setado com as informa��es coletadas do tempo
	 */

	public void setTempoMain(Tempo tempoMain) {
		this.tempoLocal = tempoMain;
		
		TextView txtTemp = (TextView) findViewById(R.id.temperatura);
		TextView txtDesc = (TextView) findViewById(R.id.previsao);
        
		int temperatura = Double.valueOf(tempoLocal.getTemperatura()).intValue();
		
		txtTemp.setText(temperatura + "�c");
		txtDesc.setText(tempoLocal.getDescricao());
	}
    
}
