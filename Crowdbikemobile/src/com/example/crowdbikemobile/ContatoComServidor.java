package com.example.crowdbikemobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import at.abraxas.amarino.Amarino;

public class ContatoComServidor extends AsyncTask <String, Void, String> {
	
	private static final String DEVICE_ADDRESS = "00:12:00:09:03:01";
	private Context contexto;
	
	public ContatoComServidor(Context ctx) {
		this.contexto = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		Amarino.connect(contexto, DEVICE_ADDRESS);
	}

	@Override
	protected String doInBackground(String... params) {
		
		String latitude  = params[0];
		String longitude = params[1];
		
		String line;
		String result = "false";
		String resultado = "";
		
		/*
		 * Esta linha deve ser modificada.
		 * Aqui deve ser informada a uri do serviço que recebe as coordenadas
		 * geográficas e retorna a situação do local: perigoso, seguro, ...
		 * 
		 */
		String uri = "http://10.0.2.2:8080/projects/contexto/coordenada.php";
		
		int responseCode = 0;
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			nameValuePairs.add(new BasicNameValuePair("latitude",  latitude ));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			int executeCount = 0;
			HttpResponse response;
			do {
				executeCount++;
				Log.v("TENTATIVA", "tentativa número:" + executeCount);

				// Execute HTTP Post Request
				response = client.execute(httppost);
				responseCode = response.getStatusLine().getStatusCode();						
				
			} while (executeCount < 5 && responseCode == 408);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((line = rd.readLine()) != null){
				result = line.trim();
			}

			//Neste ponto result já guarda o Json puro
			Log.v("STATUS", result);

		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}

		return result;
		
	}

	/**
	 * Após a execução assíncrona, o resultado deve ser enviado para o arduíno.
	 * O método sendInformation é chamado para realizar essa tarefa.
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		Log.v("SEND ARDUINO", result);
		
		sendInformation(result);
	}
	

	/**
	 * Após receber o resultado do servidor, este método faz a conexão bluetooth com 
	 * o arduíno e após isso encerra a conexão. 
	 * 
	 *  @param result	Resultado do servidor
	 */	
	public void sendInformation(String informationText){
		
        String text = null;
        text = informationText;
        
    	Amarino.sendDataToArduino(contexto, DEVICE_ADDRESS, 'A', text);
    	Amarino.disconnect(contexto, DEVICE_ADDRESS);
    }
	
}
