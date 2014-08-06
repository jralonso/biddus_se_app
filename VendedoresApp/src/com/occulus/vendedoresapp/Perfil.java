package com.occulus.vendedoresapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Perfil extends Activity {
	private String login;
	private String user_id;
	private int i, j;
	private String respStr;
	private TextView mail;
	private TextView nombreBiddus;
	private TextView cif;
	private TextView nombre;
	private TextView apellidos;
	private TextView listaCat;
	private String auxNombreBid;
	private String auxNombre;
	private String auxApellidos;
	private String auxCif;
	private String auxMail;
	private String auxCat;
	private String catFin = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		Bundle b = getIntent().getExtras();
		login = b.getString("login");
		user_id = b.getString("user_id");

		TareaWSPerfilar perfil = new TareaWSPerfilar();
		perfil.execute();

		nombreBiddus = (TextView) findViewById(R.id.textoNom);
		mail = (TextView) findViewById(R.id.textoMail);
		nombre = (TextView) findViewById(R.id.textoNombre);
		apellidos = (TextView) findViewById(R.id.textoApellidos);
		cif = (TextView) findViewById(R.id.textoCif);
		listaCat = (TextView) findViewById(R.id.textoCat);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class TareaWSPerfilar extends AsyncTask<String, Integer, Boolean> {

		private String brand;
		private String fechaFin;

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Perfil.this);
			pDialog.setMessage("Cargando");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		@Override
		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			// Cuando tengamos la url de la imagen en el JSON
			// simplemente esto habría que moverlo al bucle de lectura
			// y cargar la URL con un getString de la URL en el JSON
			// Log.i("doInBackground", "Entra en doInBackground");
			// String url =
			// "http://biddusavatar.s3.amazonaws.com/5bdf1ca694cc9932c273f63a0876ba39a9a32299.png?1402655684";
			// imagen = descargarImagen(url);
			// imagen = Bitmap.createScaledBitmap(imagen, 150, 150, true);

			/**
			 * 
			 * Sirve para obtener un cliente concreto
			 * 
			 * HttpClient httpClient = new DefaultHttpClient();
			 * 
			 * String id = params[0];
			 * 
			 * HttpGet del = new HttpGet(
			 * "http://occulus-stg.herokuapp.com/campaigns/" + id);
			 * 
			 * del.setHeader("content-type", "application/json");
			 * 
			 * try { HttpResponse resp = httpClient.execute(del); String respStr
			 * = EntityUtils.toString(resp.getEntity());
			 * 
			 * JSONObject respJSON = new JSONObject(respStr);
			 * 
			 * brand = respJSON.getString("brand");
			 * 
			 * } catch (Exception ex) { Log.e("ServicioRest", "Error!", ex);
			 * ex.printStackTrace(); resul = false;
			 * 
			 * }
			 */

			//
			String auth = new String(Base64.encode(("mrmiyago" + ":"
					+ "piscolabis").getBytes(), Base64.URL_SAFE
					| Base64.NO_WRAP));

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet("http://demo.biddus.com/api/v1/users/"
					+ user_id + "?auth_token=" + login);

			// HttpGet campa = new HttpGet(c.getMainListarCampa(user_id));

			del.setHeader("Content-type", "application/json");

			del.addHeader("Accept", "application/json");

			// del.addHeader("Authorization", "Token token=\"" + "login" +
			// "\"");

			del.addHeader("Authorization", "Basic" + auth);

			// campa.setHeader("Content-type", "application/json");
			//
			// campa.addHeader("Accept", "application/json");
			//
			// campa.addHeader("Authorization", "Token token=\"" + login +
			// "\"");

			try {
				// Construimos el objeto cliente en formato JSON
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();

				HttpResponse resp = httpClient.execute(del);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

				if (respStr.contains("error")) {

					Log.v("ERROR", "NUBU");
				} else {

					JSONObject object = new JSONObject(respStr);
					JSONObject object2 = object.getJSONObject("data");
					JSONObject object3 = object2.getJSONObject("user");
					JSONArray object4 = object2
							.getJSONArray("user_category_list");

					auxNombreBid = object3.getString("biddus_name");
					auxApellidos = object3.getString("surname");
					auxCif = object3.getString("nifcif");
					auxNombre = object3.getString("name");
					auxMail = object3.getString("email");

					for (int i = 0; i < object4.length(); i++) {
						auxCat = object4.getString(i).toString();
						Log.v("PRUEBA", auxCat);
						catFin += auxCat + "\n";

					}

					if (!respStr.equals("true"))
						resul = false;
				}
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {
			try {

				if (auxNombre == null) {
					nombre.setText(auxNombre);
				} else {
					nombre.setText("");
				}
				
				if (auxApellidos == null) {
					apellidos.setText(auxApellidos);
				} else {
					apellidos.setText("");
				}
				nombreBiddus.setText(auxNombreBid);
				mail.setText(auxMail);
				cif.setText(auxCif);
//				apellidos.setText(auxApellidos);
//				nombre.setText(auxNombre);
				listaCat.setText(catFin);
				pDialog.dismiss();
			} catch (Exception e) {
				Log.e("ServicioRest", "Error del PostExecute", e);

			}
		}
	}
}
