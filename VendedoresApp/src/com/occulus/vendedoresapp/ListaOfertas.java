package com.occulus.vendedoresapp;

import java.net.HttpURLConnection;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListaOfertas extends Activity {
	private int contOff;
	private String idFin = "";
	private int precioFin = 9999999;
	private ListView lstPedidos;
	private int i, j;
	private String[] campañas, titulos, lista;
	private String login;
	private String user_id;
	// private TareaWSLogOut descon;
	private String respStr;
	private String id;
	private boolean repe = false;
	// private TextView texto;
	private ArrayList<Pedidos> arrayPed;
	private ArrayList<String> marcas;
	private ArrayList<String> modelos;
	private ArrayList<String> listaTitulos;
	private ArrayList<Integer> precios;
	private Pedidos pedidos;
	private HttpURLConnection conn;
	private Bitmap imagen;
	private TextView tituloPedidos;
	private TareaWSListar tarea;
	// private Configuracion c;
	private Button proponCamp;
	private ArrayList<Integer> listaIds = new ArrayList<Integer>();
	public static final String PREFS_NAME = "MyPrefsFile";
	private int mes, dia, anno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedidos);

		Date date = new Date();

		mes = date.getMonth() + 1;
		dia = date.getDate();
		anno = date.getYear() + 1900;

		Log.v("Fecha", dia + "/" + mes + "/" + anno);

		Bundle b = getIntent().getExtras();
		login = b.getString("login");
		user_id = b.getString("user_id");

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("login", login);
		editor.putString("id", user_id);
		editor.commit();

		arrayPed = new ArrayList<Pedidos>();
		marcas = new ArrayList<String>();
		modelos = new ArrayList<String>();
		precios = new ArrayList<Integer>();
		listaTitulos = new ArrayList<String>();

		lstPedidos = (ListView) findViewById(R.id.lista);
		tituloPedidos = (TextView) findViewById(R.id.tituloPedidos);

		tituloPedidos.setText("Ofertas");

		tarea = new TareaWSListar();
		tarea.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_ofertas, menu);
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

	private class TareaWSListar extends AsyncTask<String, Integer, Boolean> {

		private String brand;
		private String fechaFin;

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ListaOfertas.this);
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

			HttpGet del = new HttpGet(
					"http://demo.biddus.com/api/v1/proposals/indexalluwb?auth_token="
							+ login);

			// HttpGet campa = new HttpGet(c.getMainListarCampa(user_id));

			del.setHeader("Content-type", "application/json");

			del.addHeader("Accept", "application/json");

			del.addHeader("Authorization", "Basic" + auth);

			// del.addHeader("Authorization", "Token token=\"" + "login" +
			// "\"");

			// campa.setHeader("Content-type", "application/json");
			//
			// campa.addHeader("Accept", "application/json");
			//
			// campa.addHeader("Authorization", "Token token=\"" + login +
			// "\"");

			try {
				// HttpResponse resp2 = httpClient.execute(campa);

				// String respStr2 = EntityUtils.toString(resp2.getEntity());

				HttpResponse resp = httpClient.execute(del);

				String respStr = EntityUtils.toString(resp.getEntity());

				// Log.v("respStr", respStr);
				// Log.v("respStr2", respStr2);

				// JSONObject objectCamp = new JSONObject(respStr2);
				// JSONObject auxObject2 = objectCamp.getJSONObject("data");
				// JSONArray respJSON2 = auxObject2.getJSONArray("orders");

				// Log.v("JSON2", respJSON2.toString());

				JSONObject object = new JSONObject(respStr);
				// Log.v("Object", object.toString());

				JSONObject auxObject = object.getJSONObject("data");
				// Log.v("JSON2", auxObject.toString());

				JSONArray respJSON = auxObject.getJSONArray("proposals");

				campañas = new String[respJSON.length()];
				titulos = new String[respJSON.length()];
				// lista = new String[respJSON2.length()];

				for (i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);

					// for (int p = 0; p < respJSON2.length(); p++) {
					// JSONObject obj2 = respJSON2.getJSONObject(p);
					// String id2 = obj2.getString("campaign_id");
					// lista[p] = id2;
					// Log.v("Lista", lista[p]);
					// }

					// String respStr2 = obj.getString("data");

					// String title = obj.getString("title");
					// Log.v("OBJ", respStr);

					// lista[j] = campaña;
					//
					// Log.v("Lista", lista[j]);

					// for (int w = 0; w < j; w++) {
					//
					// if (lista[w].equalsIgnoreCase(id)) {
					// titulos[i] = " " + title + " Apuntado";
					//
					// } else {
					// titulos[i] = " " + title;
					// }
					// }

					// active = obj.getString("active");
					id = obj.getString("id");
					// Log.v("ID", id);
					// listaIds.add(Integer.parseInt(id));

					// String marca = obj.getString("brand");
					// String modelo = obj.getString("model");
					String title = obj.getString("title");
					String categoria = obj.getString("main_category");
					String status = obj.getString("status");
					String ofertas = obj.getString("bids");

					JSONArray respJSONBids = obj.getJSONArray("bids");

					for (int j = 0; j < respJSONBids.length(); j++) {
						JSONObject objBids = respJSONBids.getJSONObject(j);

						// String title = objBids.getString("maxibid_title");
						String marcaOffer = objBids.getString("product_brand");
						String modeloOffer = objBids.getString("product_model");
						String precioOffer = objBids.getString("bid_price");
						String idOffer = objBids.getString("user_id");
						// Log.v("Offers2", marcaOffer);

						int aux = Integer.parseInt(precioOffer);
						// if (aux < precioFin) {
						if (user_id.equalsIgnoreCase(idOffer)) {
							listaTitulos.add(title);
							marcas.add(marcaOffer);
							modelos.add(modeloOffer);
							precios.add(Integer.parseInt(precioOffer));

							if (aux < precioFin) {
								precioFin = aux;
								idFin = idOffer;

								Log.v("VYYCTYX", "ENTRO");

								Log.v("ID", idFin);
								Log.v("ID User", user_id);
								// idFin = idOffer;

								contOff = j + 1;
							}
							// listaTitulos.add(idFin);
							// marcas.add(marcaOffer);
							// modelos.add(modeloOffer);
							// precios.add(Integer.parseInt(precioOffer));
						}

					}
					// Log.v("Offers", ofertas);
					fechaFin = obj.getString("close_date");
					String annoFin = fechaFin.substring(0, 4);
					String mesFin = fechaFin.substring(5, 7);
					String diaFin = fechaFin.substring(8, 10);

					// Log.v("Fecha", diaFin + "/" + mesFin + "/" + annoFin);

					// pedidos = new Pedidos(null, i + " " + listaTitulos.get(i)
					// + "\nMarca: " + marcas.get(i) + "\nModelo: "
					// + modelos.get(i) + "\nPrecio: " + precios.get(i)
					// + "€");

					pedidos = new Pedidos(null, listaTitulos.get(i)
							+ "\nMarca: " + marcas.get(i) + "\nModelo: "
							+ modelos.get(i) + "\nPrecio: " + precios.get(i)
							+ "€");
					arrayPed.add(pedidos);
					listaIds.add(Integer.parseInt(id));

					// Log.v("lista" + j, lista[j].toString());
					// Log.v("ID" + j, id);

					// Log.v("length lista", "" + lista.length);

					// directivo = new Directivo(getResources().getDrawable(
					// R.drawable.facebook), title);
					// arraydir.add(directivo);

					// for (int w = 0; w < lista.length; w++) {
					// if (lista[w].equalsIgnoreCase(id)) {
					//
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.ic_launcher), title);
					//
					// break;
					// } else {
					//
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.ic_launcher), title);
					// }
					//
					// }
					//
					// if (lista.length == 0) {
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.ic_launcher), title);
					//
					// }
					/**
					 * // Esto sería para hacer un filtro por categoría //
					 */
					// if (categoria.equalsIgnoreCase("sports") ||
					// categoria.equalsIgnoreCase("kids")) {

					Log.v("ID Fuera", idFin);
					Log.v("ID User Fuera", user_id);

					// if (Integer.parseInt(annoFin) < anno) {
					//
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.caducado), title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					//
					// } else if (Integer.parseInt(annoFin) == anno) {
					//
					// if (Integer.parseInt(mesFin) < mes) {
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.caducado), title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					//
					// } else if (Integer.parseInt(mesFin) == mes) {
					// if (Integer.parseInt(diaFin) < dia) {
					// pedidos = new Pedidos(getResources()
					// .getDrawable(R.drawable.caducado),
					// title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					//
					// } else {
					//
					// if (ofertas.equalsIgnoreCase("[]")) {
					// pedidos = new Pedidos(
					// getResources().getDrawable(
					// R.drawable.ic_launcher),
					// title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// } else {
					//
					// if (idFin.equalsIgnoreCase(user_id)) {
					//
					// pedidos = new Pedidos(
					// getResources().getDrawable(
					// R.drawable.ganador),
					// title + "\nOfertas: " + contOff
					// + "\nPrecio: "
					// + precioFin + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// } else {
					// pedidos = new Pedidos(
					// getResources().getDrawable(
					// R.drawable.ofertas),
					// title + "\nOfertas: " + contOff
					// + "\nPrecio: "
					// + precioFin + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// }
					// }
					// }
					// } else {
					//
					// if (ofertas.equalsIgnoreCase("[]")) {
					//
					// pedidos = new Pedidos(getResources()
					// .getDrawable(R.drawable.ic_launcher),
					// title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					//
					// } else {
					//
					// if (idFin.equalsIgnoreCase(user_id)) {
					//
					// pedidos = new Pedidos(getResources()
					// .getDrawable(R.drawable.ganador),
					// title + "\nOfertas: " + contOff
					// + "\nPrecio: " + precioFin
					// + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// } else {
					// pedidos = new Pedidos(getResources()
					// .getDrawable(R.drawable.ofertas),
					// title + "\nOfertas: " + contOff
					// + "\nPrecio: " + precioFin
					// + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// }
					// }
					// }
					//
					// } else {
					// if (ofertas.equalsIgnoreCase("[]")) {
					//
					// pedidos = new Pedidos(getResources().getDrawable(
					// R.drawable.ic_launcher), title);
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// } else {
					//
					// if (idFin.equalsIgnoreCase(user_id)) {
					//
					// pedidos = new Pedidos(getResources()
					// .getDrawable(R.drawable.ganador), title
					// + "\nOfertas: " + contOff
					// + "\nPrecio: " + precioFin + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// } else {
					// pedidos = new Pedidos(null, title
					// + "\nOfertas: " + marcaOffer
					// + "\nPrecio: " + precioFin + "€");
					//
					// arrayPed.add(pedidos);
					// listaIds.add(Integer.parseInt(id));
					// }
					// }
					// }

					/**
					 * ESTO SIRVE TOOMUCH para que enlace la lista con sus
					 * respectivas ID'sn
					 * 
					 */
					// listaIds.add(Integer.parseInt(id));

					// }

					// campañas[i] = " " + marca + " " + modelo + " ";
					// titulos[i] = " " + title;
					precioFin = 9999999;
				}

				// lstPedidos.setOnItemClickListener(new OnItemClickListener() {
				//
				// @Override
				// public void onItemClick(AdapterView<?> parent, View view,
				// int position, long id) {
				//
				// Intent intent = new Intent(ListaPedidos.this,
				// Activity1.class);
				// intent.putExtra("id", listaIds.get(position));
				// intent.putExtra("user_id", user_id);
				// intent.putExtra("login", login);
				//
				// startActivity(intent);
				//
				// }
				// });

			} catch (SocketException ex) {
				Log.e("ServicioRest", "Error!", ex);

				resul = false;
				finish();
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);

				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {
			try {
				if (result) {
					// PushService.setDefaultPushCallback(MainActivity.this,
					// MainActivity.class);
					// ParseInstallation.getCurrentInstallation()
					// .saveInBackground();
					// ParseInstallation.getCurrentInstallation()
					// .deleteInBackground();

					// proponCamp.setVisibility(1);

					// Rellenamos la lista con los resultados
					// ArrayAdapter<String> adaptador = new
					// ArrayAdapter<String>(
					// MainActivity.this,
					// android.R.layout.simple_list_item_1, titulos);

					tituloPedidos.setVisibility(1);

					AdapterPedidos adapter = new AdapterPedidos(
							ListaOfertas.this, arrayPed);

					lstPedidos.setAdapter(adapter);
					pDialog.dismiss();
				}
			} catch (Exception e) {
				Log.e("ServicioRest", "Error del PostExecute", e);

			}
		}
	}

}
