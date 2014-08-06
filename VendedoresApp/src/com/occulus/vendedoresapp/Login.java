package com.occulus.vendedoresapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	private EditText user;
	private EditText pass;
	private TextView registro;
	private Button enviar;
	private String usuario;
	private String password;
	// private HttpEntity entity;
	// private String respStr;
	private TareaWSLogin tarea;
	private String login;
	private String user_id;
	private String respStr;
	// private Configuracion c;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onStart() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String prueba = settings.getString("login", null);
		String prueba2 = settings.getString("id", null);

		
		
		Log.v("PRB", prueba + " " + prueba2);

		if (prueba != null) {
			Intent intent = new Intent(Login.this, ListaPedidos.class);
			// Log.v("Login final", login);

			intent.putExtra("login", prueba);
			intent.putExtra("user_id", prueba2);

			// c.setUsuario(user_id);
			// c.setToken(login);

			startActivity(intent);

		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// c = new Configuracion();

		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pass);
		enviar = (Button) findViewById(R.id.boton);
		registro = (TextView) findViewById(R.id.registro);

		registro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Login.this, Registro.class);
				startActivity(intent);

			}
		});

		// enviar.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// usuario = user.getText().toString();
		// password = pass.getText().toString();
		//
		// Intent intent = new Intent(Login.this, MainActivity.class);
		// intent.putExtra("user", usuario);
		// intent.putExtra("pass", password);
		// startActivity(intent);
		//
		// Toast t = Toast.makeText(Login.this, usuario + password,
		// Toast.LENGTH_SHORT);
		//
		// t.show();
		//
		// }
		// });

		// tarea = new TareaWSInsertar();

		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				usuario = user.getText().toString();
				password = pass.getText().toString();
				tarea = new TareaWSLogin();

				tarea.execute();

			}

		});

	}

	private class TareaWSLogin extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost(
					"http://demo.biddus.com/api/v1/sessions");

			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");

			try {
				// Construimos el objeto cliente en formato JSON
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();

				dato.put("email", usuario);
				dato.put("password", password);
				parentData.put("user", dato);

				StringEntity entity = new StringEntity(parentData.toString());
				post.setEntity(entity);

				HttpResponse resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

				if (respStr.contains("error")) {

					Log.v("ERROR", "NUBU");
					login = "error";

				} else {

					JSONObject object = new JSONObject(respStr);
					JSONObject object2 = object.getJSONObject("data");

					Log.v("Bien un token", object2.toString());

					login = object2.getString("auth_token");
					user_id = object2.getString("user_id");
					Log.v("LOGIN", login);
					Log.v("ID", user_id);

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

			if (login.contains("error")) {

				Toast t = Toast.makeText(Login.this, "Error en el login",
						Toast.LENGTH_SHORT);

				t.show();

				pass.setText("");
			} else {

				Toast t = Toast.makeText(Login.this, "Bienvenido",
						Toast.LENGTH_SHORT);

				t.show();

				Intent intent = new Intent(Login.this, ListaPedidos.class);
				// Log.v("Login final", login);

				intent.putExtra("login", login);
				intent.putExtra("user_id", user_id);

				// c.setUsuario(user_id);
				// c.setToken(login);

				startActivity(intent);

				user.setText("");
				pass.setText("");

			}

		}
	}

	// private void onRestartApuntado() {
	// finish();
	// }

}