package com.occulus.vendedoresapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aeat.valida.Validador;

public class Registro extends Activity {

	private EditText ETemail;
	private String email;
	private String[] office, electronics, appliances, sports, kids, housing;
	private EditText ETrazonSocial;
	private String razonSocial;
	private EditText ETnifCif;
	private String nifCif;
	private EditText ETnombreUser;
	private String nombreUser;
	private Spinner SPcategoria;
	private List<String> categoria;

	private EditText ETpass;
	private String pass;
	private EditText ETrePass;
	private String rePass;
	private Button enviar;
	private TareaWSRegistro reg;
	private TareaWSSpinner spn;
	private String login;
	private String user_id;
	private String respStr;
	private ArrayList<String> categorias;
	private ArrayList<String> categoriasDelUsuario;
	private ArrayAdapter spinner_adapter;
	private MultiSelectionSpinner spinner;
	private Validador validador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);

		validador = new Validador();
		categoriasDelUsuario = new ArrayList<String>();
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
		ETemail = (EditText) findViewById(R.id.email);
		ETrazonSocial = (EditText) findViewById(R.id.razonSocial);
		ETnifCif = (EditText) findViewById(R.id.nifCif);
		ETnombreUser = (EditText) findViewById(R.id.nombreUser);
		ETpass = (EditText) findViewById(R.id.pass);
		ETrePass = (EditText) findViewById(R.id.confirmPass);

		// SPcategoria = (Spinner) findViewById(R.id.categoria);

		enviar = (Button) findViewById(R.id.boton);

		reg = new TareaWSRegistro();
		spn = new TareaWSSpinner();
		spn.execute();

		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				razonSocial = ETrazonSocial.getText().toString();
				nifCif = ETnifCif.getText().toString().toUpperCase();
				
				Log.v("NIF", nifCif);
				 int e = validador.checkNif(nifCif);
				
				 if (e > 0)
				 Log.v("VAL", "OK");
				 else
				 Log.v("VAL", "MAL");

				nombreUser = ETnombreUser.getText().toString();
				categoria = spinner.getSelectedStrings();
				Log.v("CAT", categoria.toString());
				// categoriasDelUsuario.add(categoria);
				email = ETemail.getText().toString();
				pass = ETpass.getText().toString();
				rePass = ETrePass.getText().toString();

				if (email.length() == 0 || !email.contains("@")
						|| !email.contains(".")) {
					Toast t = Toast
							.makeText(
									Registro.this,
									"El email no puede estar vacío y debe ser un email valido",
									Toast.LENGTH_SHORT);
					t.show();
				} else if (razonSocial.length() == 0) {
					Toast t = Toast.makeText(Registro.this,
							"La razón social no puede estar en blanco",
							Toast.LENGTH_SHORT);
					t.show();
				} else if (nifCif.length() == 0 || e<=0) {
					Toast t = Toast.makeText(Registro.this,
							"El NIF / CIF no puede estar en blanco y debe ser valido",
							Toast.LENGTH_SHORT);
					t.show();
				} else if (nombreUser.length() == 0) {
					Toast t = Toast.makeText(Registro.this,
							"El nombre de usuario no puede estar en blanco",
							Toast.LENGTH_SHORT);
					t.show();
				} else if (!pass.equalsIgnoreCase(rePass) || pass.length() < 8) {
					Toast t = Toast
							.makeText(
									Registro.this,
									"Las contraseñas no coinciden o son demasiado cortas (8 caracteres mínimo)",
									Toast.LENGTH_SHORT);
					t.show();
				} else {

					reg.execute();
				}

			}
		});

	}

	private class TareaWSRegistro extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();

			try {
				HttpPost post = new HttpPost(
						"http://demo.biddus.com/api/v1/registrations");

				post.setHeader("content-type", "application/json");
				post.setHeader("accept", "application/json");

				// Construimos el objeto cliente en formato JSON
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();
				JSONArray dato2 = new JSONArray();

				dato.put("business_name", razonSocial);
				dato.put("nifcif", nifCif.toUpperCase());
				dato.put("email", email);
				dato.put("biddus_name", nombreUser);
				dato.put("password", pass);
				dato.put("password_confirmation", rePass);
				dato.put("seller", true);

				for (int i = 0; i < categoria.size(); i++) {
					dato2.put(categoria.get(i));
				}

				dato.put("category", dato2);

				parentData.put("user", dato);
				// parentData.put("user_category_list", dato2);

				StringEntity entity = new StringEntity(parentData.toString());
				post.setEntity(entity);
				Log.v("Entity", entity.toString());

				HttpResponse resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

				JSONObject object = new JSONObject(respStr);
				JSONObject object2 = object.getJSONObject("data");
				JSONObject object3 = object2.getJSONObject("user");

				// Log.v("Bien un token", object2.toString());

				login = object2.getString("auth_token");
				user_id = object3.getString("id");
				Log.v("LOGIN", login);
				Log.v("ID", user_id);

				if (!respStr.equals("true"))
					resul = false;
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;

		}

		protected void onPostExecute(Boolean result) {

			if (respStr.contains("Registered")) {

				Toast t = Toast.makeText(Registro.this,
						"Registrado correctamente", Toast.LENGTH_SHORT);

				t.show();

				Intent intent = new Intent(Registro.this, ListaPedidos.class);
				// Log.v("Login final", login);

				intent.putExtra("login", login);
				intent.putExtra("user_id", user_id);

				startActivity(intent);

			} else {
				Toast t = Toast.makeText(Registro.this, "Error en el registro",
						Toast.LENGTH_SHORT);

				t.show();

				finish();
			}

		}
	}

	private class TareaWSSpinner extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient2 = new DefaultHttpClient();

			String auth = new String(Base64.encode(("mrmiyago" + ":"
					+ "piscolabis").getBytes(), Base64.URL_SAFE
					| Base64.NO_WRAP));

			HttpGet del = new HttpGet(
					"http://demo.biddus.com/api/v1/categories");
			del.setHeader("Content-type", "application/json");

			del.addHeader("Accept", "application/json");

			// del.addHeader("Authorization", "Token token=\"" + "login" +
			// "\"");

			del.addHeader("Authorization", "Basic" + auth);

			try {

				HttpResponse resp2 = httpClient2.execute(del);

				String respStr = EntityUtils.toString(resp2.getEntity());

				Log.v("respStr", respStr);

				JSONObject objectAux = new JSONObject(respStr);
				Log.v("Object", objectAux.toString());

				JSONObject auxObject = objectAux.getJSONObject("data");
				Log.v("JSON2", auxObject.toString());

				JSONObject respJSON = auxObject.getJSONObject("subcategories");
				Log.v("SubcategoriesJSON", respJSON.toString());

				JSONArray listOffice = respJSON.getJSONArray("office");
				JSONArray listElectronics = respJSON
						.getJSONArray("electronics");
				JSONArray listAppliances = respJSON.getJSONArray("appliances");
				JSONArray listSports = respJSON.getJSONArray("sports");
				JSONArray listHousing = respJSON.getJSONArray("housing");
				JSONArray listKids = respJSON.getJSONArray("kids");

				categorias = new ArrayList<String>();
				// Log.v("ArrayJSON", listOffice.toString());
				// resp = new ArrayList<JSONArray>();
				// // resp.add(respJSON2);

				office = new String[listOffice.length()];
				electronics = new String[listElectronics.length()];
				appliances = new String[listAppliances.length()];
				sports = new String[listSports.length()];
				housing = new String[listHousing.length()];
				kids = new String[listKids.length()];

				for (int i = 0; i < listOffice.length(); i++) {
					office[i] = listOffice.getString(i);
					categorias.add(office[i]);
				}

				for (int i = 0; i < listElectronics.length(); i++) {
					electronics[i] = listElectronics.getString(i);
					categorias.add(electronics[i]);

				}

				for (int i = 0; i < listAppliances.length(); i++) {
					appliances[i] = listAppliances.getString(i);
					categorias.add(appliances[i]);

				}

				for (int i = 0; i < listSports.length(); i++) {
					sports[i] = listSports.getString(i);
					categorias.add(sports[i]);

				}

				for (int i = 0; i < listHousing.length(); i++) {
					housing[i] = listHousing.getString(i);
					categorias.add(housing[i]);

				}

				for (int i = 0; i < listKids.length(); i++) {
					kids[i] = listKids.getString(i);
					categorias.add(kids[i]);

				}

			} catch (Exception e) {
			}
			return resul;
		}

		protected void onPostExecute(Boolean result) {
			try {
				if (result) {

					// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					// getApplicationContext(), R.layout.textview_spinner,
					// categorias);
					//
					// //
					// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// SPcategoria.setAdapter(adapter);

					spinner.setItems(categorias);
				}
			} catch (Exception e) {
				Log.e("ServicioRest", "Error del PostExecute", e);

			}
		}
	}

	@Override
	protected void onRestart() {
		finish();
		super.onRestart();
	}

}