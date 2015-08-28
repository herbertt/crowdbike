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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class AsyncTempo extends AsyncTask <String, Void, Tempo> {
	
	private Context contexto;
	
	public AsyncTempo(Context ctx) {
		this.contexto = ctx;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Tempo doInBackground(String... params) {
		
		/* 
		 * As duas linhas seguintes recebem a atual coordenada geográfica da bike 
		 *  
		 */
		String latitude  = params[0];
		String longitude = params[1];
		
		String line;
		String result = "false";
		String resultado = "";
		
		/*
		 * Aqui está o endereço do serviço de tempo
		 * 
		 */
		String uri = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric";
		
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

		return parseJson(result);
		
	}

	
	@Override
	protected void onPostExecute(Tempo result) {
		((MainActivity) contexto).setTempoMain(result);
	}

	/**
	 * Este método recebe o resultado do tempo em JSON.
	 * Seu objetivo é fazer um parse do tempo em JSON para um objeto Tempo.
	 * 
	 * @param jsonString	Resultado do webservice em JSON
	 * @return	Resultado do webservice em objeto Tempo
	 */
	private Tempo parseJson(String jsonString) {
		
		Log.v("JSON", jsonString);
		Tempo tempo = new Tempo();
		
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			
			//Tratando a temperatura
				//Buscando a temperatura no json
				JSONObject jsonMain = jsonObject.getJSONObject("main");
				
				//Setando a temperatura no objeto
				tempo.setTemperatura(jsonMain.getString("temp"));
			
			//Tratando a descrição
				//Buscando a descrição no json
				JSONArray jsonArray    = jsonObject.getJSONArray("weather");
				JSONObject jsonWeather = jsonArray.getJSONObject(0);
				
				//Setando a descrição no objeto
				tempo.setDescricao(jsonWeather.getString("description"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return tempo;
	}
	
}
