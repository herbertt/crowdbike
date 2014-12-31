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
        
        //Captando a posição geográfica
        posicaoGeografica();
        
        //Executando tarefas paralelas
        tarefasParalelas();
        
        //Setando a cor de fundo. Padrão: verde
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
     * Este método seta a cor de fundo do aplicativo
     * @param cor	Código inteiro da cor. A lista de cores disponíveis está em res/calues/colors.xml
     */
    public void setarCorDeFundo(int cor){
    	View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(cor));
    }
    
    /**
     * Este método identifica a posição geográfica do aparelho
     * A posição é armazenada nos atributos privados latitudeString e longitudeString,
     * 
     */
    public void posicaoGeografica(){
    	
        //Instancia um objeto GPSTracker
        gps = new GPSTracker(MainActivity.this);

        // Checa se o GPS está habilitado
 		if(gps.canGetLocation()){

 			latitudeString  = String.valueOf(gps.getLatitude());
 			longitudeString = String.valueOf(gps.getLongitude());
 			
 			Toast.makeText(getApplicationContext(), "Sua localização é - \nLat: " + latitudeString + "\nLong: " + longitudeString, Toast.LENGTH_LONG).show();
 			tarefasParalelas();
 			
 		}else{
 			/* 
 			 * Não pode pegar a localização
 			 * GPS or Network está desabilitado
 			 * Pede para o usuário habilitar
 			 */
 			gps.showSettingsAlert();
 		}
    }
    
    /**
     * Este método executa as tarefas paralelas para:
     *  - Acesso ao servidor
     *  - Receber informações de tempo
     */
    public void tarefasParalelas(){

        //Instanciando a asynctask para contato com o servidor e acesso ao arduíno
        AsyncServidor task = new AsyncServidor(this);
        //task.execute(latitudeString, longitudeString);
        
        //Instanciando a asynktask para contato com o serviço de tempo
        AsyncTempo tempo = new AsyncTempo(this);
        tempo.execute(latitudeString, longitudeString);
    }
    
	/**
	 * Este método seta o atributo tempoMain.
	 * Este atributo armazena as informações de tempo
	 * 
	 * @param tempoMain	objeto Tempo setado com as informações coletadas do tempo
	 */

	public void setTempoMain(Tempo tempoMain) {
		this.tempoLocal = tempoMain;
		
		TextView txtTemp = (TextView) findViewById(R.id.temperatura);
		TextView txtDesc = (TextView) findViewById(R.id.previsao);
        
		int temperatura = Double.valueOf(tempoLocal.getTemperatura()).intValue();
		
		txtTemp.setText(temperatura + "ºc");
		txtDesc.setText(tempoLocal.getDescricao());
	}
    
}
