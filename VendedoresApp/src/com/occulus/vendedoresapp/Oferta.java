package com.occulus.vendedoresapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Oferta extends Activity {
	private int id;
	private String offerId;
	private String login;
	private String user_id;
	private ImageButton ib;
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	private String respStr;
	private EditText ETValida;
	private EditText ETMarca;
	private EditText ETModelo;
	private EditText ETPrecio;
	private EditText ETUnidades;
	private Spinner SPEnvio;
	private EditText ETNotas;
	private Button enviar;
	private CheckBox check;
	private TareaWSOfertar ofertar;
	private ArrayList<String> producto;
	private String valida, marca, modelo, precio, unidades, envio, notas,
			terminos;
	private String marca2, modelo2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oferta);

		Bundle b = getIntent().getExtras();
		marca2 = b.getString("marca");
		modelo2 = b.getString("modelo");
		id = b.getInt("id");
		user_id = b.getString("user_id");
		login = b.getString("login");

		Log.v("ID", "" + id);

		producto = new ArrayList<String>();
		ofertar = new TareaWSOfertar();

		// mDateButton = (Button) findViewById(R.id.date_button);
		ib = (ImageButton) findViewById(R.id.buttonCalendar);
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		ETValida = (EditText) findViewById(R.id.ofertaValida);
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);

			}
		});

		ETMarca = (EditText) findViewById(R.id.ofertaMarca);
		ETModelo = (EditText) findViewById(R.id.ofertaModelo);
		ETPrecio = (EditText) findViewById(R.id.ofertaPrecio);
		ETUnidades = (EditText) findViewById(R.id.ofertaUnidades);
		SPEnvio = (Spinner) findViewById(R.id.ofertaEnvio);
		ETNotas = (EditText) findViewById(R.id.ofertaNotas);
		check = (CheckBox) findViewById(R.id.check);

		if (marca2 != null) {
			ETMarca.setText(marca2);
		}
		if (modelo2 != null) {
			ETModelo.setText(modelo2);
		}

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.envio, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		SPEnvio.setAdapter(adapter);

		enviar = (Button) findViewById(R.id.ofertaEnviar);

		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				marca = ETMarca.getText().toString();
				modelo = ETModelo.getText().toString();
				precio = ETPrecio.getText().toString();
				unidades = ETUnidades.getText().toString();

				if (check.isChecked()) {
					terminos = "1";
				} else
					terminos = "0";

				int pos = SPEnvio.getSelectedItemPosition();
				switch (pos) {
				case 0:
					envio = "fast";
					break;
				case 1:
					envio = "normal";

					break;
				case 2:
					envio = "slow";

					break;
				case 3:
					envio = "very_slow";

					break;

				}
				valida = ETValida.getText().toString();
				notas = ETNotas.getText().toString();

				AlertDialog.Builder adb = new AlertDialog.Builder(Oferta.this);
				adb.setTitle("Datos");
				adb.setMessage("Marca: " + marca + "\n" + "Modelo: " + modelo
						+ "\n" + "Precio: " + precio + "\n" + "Unidades: "
						+ unidades + "\n" + "Envío: " + envio + "\n"
						+ "Fecha fin: " + valida + "\n" + "Notas: " + notas
						+ " \n\n" + "¿Desea continuar?");

				adb.setIcon(android.R.drawable.ic_menu_compass);

				adb.setPositiveButton("Si",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ofertar.execute();

								Intent intent = new Intent(Oferta.this,
										ListaPedidos.class);
								intent.putExtra("user_id", user_id);
								intent.putExtra("login", login);
//								intent.putExtra("offerId", offerId);
//								Log.v("ID envioooooooooooooo", offerId);

								startActivity(intent);

								finish();
							}
						});

				adb.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				adb.show();
			}
		});

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			if (selectedDay < 10)
				selectedDay = 0 + selectedDay;
			if ((selectedMonth + 1) < 10)
				selectedMonth = 0 + selectedMonth;

			ETValida.setText(selectedDay + "-" + (selectedMonth + 1) + "-"
					+ selectedYear);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oferta, menu);
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

	private class TareaWSOfertar extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			String auth = new String(Base64.encode(("mrmiyago" + ":"
					+ "piscolabis").getBytes(), Base64.URL_SAFE
					| Base64.NO_WRAP));

			HttpClient httpClient = new DefaultHttpClient();

			try {
				HttpPost post = new HttpPost(
						"http://demo.biddus.com/api/v1/proposals/" + id
								+ "/bids?auth_token=" + login);

				post.setHeader("content-type", "application/json");
				post.addHeader("accept", "application/json");
				// post.addHeader("Authorization", "Basic" + auth);
				// post.addHeader("Authorization", "Token token=\"" + "login"
				// + "\"");

				// Construimos el objeto cliente en formato JSON
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();
				// JSONArray dato2 = new JSONArray();
				// JSONObject dato3 = new JSONObject();

				dato.put("bid_price", Integer.parseInt(precio));
				dato.put("num_items", Integer.parseInt(unidades));
				dato.put("terms_of_service", terminos);
				dato.put("shipping", envio);
				dato.put("valid_until", valida);
				dato.put("product_id", "");
				dato.put("product_brand", marca);
				dato.put("product_model", modelo);
				dato.put("product_name", marca + " " + modelo);
				dato.put("product_description", notas);

				Log.v("TAG ", precio + " " + unidades + " " + "1 " + "normal "
						+ valida + marca + " " + modelo + " " + notas);

				// producto.add(precio);
				// producto.add(unidades);
				// producto.add("1");
				// producto.add("normal");
				// producto.add("02-08-2014");
				// producto.add("");
				// producto.add(marca);
				// producto.add(modelo);
				// producto.add(marca + " " + modelo);
				// producto.add(notas);
				//
				// for (int i = 0; i < producto.size(); i++) {
				// dato2.put(producto.get(i));
				//
				// Log.v("OFerta", dato2.toString());
				// }
				//
				// dato3.put("bid", dato);

				parentData.put("bid", dato);
				// parentData.put("user_category_list", dato2);

				StringEntity entity = new StringEntity(parentData.toString());
				post.setEntity(entity);
				Log.v("Entity", entity.toString());

				HttpResponse resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

//				JSONObject object = new JSONObject(respStr);
//				
//				JSONObject auxObject = object.getJSONObject("data");
//				// Log.v("JSON2", auxObject.toString());
//
//				JSONObject respJSON = auxObject.getJSONObject("bid");
//				
//				 offerId = respJSON.getString("id");
//
//				
//				Log.v("IDIDDIDIDID", offerId);



			
				// JSONObject object = new JSONObject(respStr);
				// JSONObject object2 = object.getJSONObject("data");
				// JSONObject object3 = object2.getJSONObject("bids");
				//
				// // Log.v("Bien un token", object2.toString());
				//
				// login = object2.getString("auth_token");
				// user_id = object3.getString("id");
				// Log.v("LOGIN", login);
				// Log.v("ID", user_id);

				if (!respStr.equals("true"))
					resul = false;
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;

		}

		protected void onPostExecute(Boolean result) {

//			 if(result){
//			 Toast t = Toast.makeText(Oferta.this,
//			 "Oferta realizada correctamente",
//			 Toast.LENGTH_SHORT);
//			 t.show();
//			 }
		}
	}
}