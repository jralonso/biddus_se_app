package com.occulus.vendedoresapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity1 extends Activity {
	private String modelo;
	private String marca;
	private ArrayList <Comentarios> comentarios= new ArrayList<Comentarios>();
	private int id;
	private String login;
	private String user_id;
	// private TextView txt;
	private Button boton, botonShare,botonComentar;
	private String respStr;
	private TextView lblResultado;
	private TextView titulo;
	private TextView lblFechaFin;
	private TextView fecha;
	private TextView TVmodelo;
	private TextView TVmarca;
	private TextView TVprecio;
	private TextView TVfechaFin;
	private TextView TXTMarca, TXTModelo, TXTPrecio, TXTFechaFin;
	private TextView TVPreComent;
	private TextView TXTComent;
	private ImageButton imB1, imB2;
	private Bitmap imagen;
	private ImageView imagenDetalle;
	private int mes, dia, anno;
	private TareaWSComentar comentar;
	private EditText comentarioEnvio;

	// private Configuracion c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);

		Date date = new Date();

		mes = date.getMonth() + 1;
		dia = date.getDate();
		anno = date.getYear() + 1900;

		// c = new Configuracion();

		// if (!MainActivity.verificaConexion(Activity1.this)) {
		// Toast.makeText(getBaseContext(),
		// "Comprueba tu conexi�n a Internet.", Toast.LENGTH_SHORT)
		// .show();
		// }

		Bundle b = getIntent().getExtras();
		id = b.getInt("id");
		user_id = b.getString("user_id");
		login = b.getString("login");

		// txt = (TextView) findViewById(R.id.texto);
		boton = (Button) findViewById(R.id.botonApuntar);
		botonComentar = (Button)findViewById(R.id.botonComentar);
		// botonShare = (Button) findViewById(R.id.botonShare);
		//
		//
		// botonShare.setOnClickListener(new OnClickListener() {
		//
		//
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(Intent.ACTION_SEND);
		//
		// intent.setType("text/plain");
		// intent.putExtra(android.content.Intent.EXTRA_TEXT,
		// "Mira que campa�a colectiva acabo de encontrar.");
		// startActivity(Intent.createChooser(intent, "Share"));
		//
		// }
		// });

		// imB1 = (ImageButton) findViewById(R.id.botonImagen1);
		// imB2 = (ImageButton) findViewById(R.id.botonImagen2);
		//
		// imB1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// try {
		//
		// AddThis.shareItem(Activity1.this, "facebook",
		// c.getUrlShare(), "Biddus Campa�as Colectivas",
		// "Mira que campa�a colectiva acabo de encontrar");
		// } catch (ATDatabaseException e) {
		// e.printStackTrace();
		// } catch (ATSharerException e) {
		// e.printStackTrace();
		// }
		// }
		// });
		//
		// imB2.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// try {
		// Config.configObject().setTwitterViaText("biddus");
		//
		// AddThis.shareItem(Activity1.this, "twitter",
		// c.getUrlShare(), "Biddus Campa�as Colectivas",
		// "Mira que campa�a colectiva acabo de encontrar");
		//
		// } catch (ATDatabaseException e) {
		// e.printStackTrace();
		// } catch (ATSharerException e) {
		// e.printStackTrace();
		// }
		// }
		// });

		// switch (id) {
		//
		// case (1):
		// txt.setText("ID 1");
		// break;
		// case 2:
		// txt.setText("ID 2");
		//
		// break;
		// }

		// lblResultado = (TextView) findViewById(R.id.detalles);
		TVmarca = (TextView) findViewById(R.id.marca);
		TVmodelo = (TextView) findViewById(R.id.modelo);
		TVprecio = (TextView) findViewById(R.id.precio);
		TVfechaFin = (TextView) findViewById(R.id.fechaFin);
		titulo = (TextView) findViewById(R.id.tituloDetalles);

		TXTMarca = (TextView) findViewById(R.id.textoMarca);
		TXTModelo = (TextView) findViewById(R.id.textoModelo);
		TXTPrecio = (TextView) findViewById(R.id.textoPrecio);
		TXTFechaFin = (TextView) findViewById(R.id.textoFechaFin);
		
		TVPreComent = (TextView)findViewById(R.id.PreComentarios);
		TXTComent = (TextView)findViewById(R.id.comentarios);

		// lblFechaFin = (TextView) findViewById(R.id.lblfechaFin);
		// fecha = (TextView) findViewById(R.id.fechaFin);

		// imagenDetalle = (ImageView) findViewById(R.id.imagenDetalle);
		// lstClientes = (ListView) findViewById(R.id.lista);

		// Button obtener = (Button) findViewById(R.id.button1);

		// obtener.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {

		TareaWSObtener tarea = new TareaWSObtener();
		tarea.execute();

		// }
		// });

		boton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TareaWSInsertar insert = new TareaWSInsertar();
				// // Id de la campa�a e Id de usuario
				// insert.execute("" + id, user_id);
				//
				Intent intent = new Intent(Activity1.this, Oferta.class);
				intent.putExtra("id", id);
				intent.putExtra("marca", marca);
				intent.putExtra("modelo", modelo);
				intent.putExtra("user_id", user_id);
				intent.putExtra("login", login);
				startActivity(intent);
				finish();
				//
				// onRestartApuntado();

				// Toast t = Toast.makeText(Activity1.this,
				// "Apuntado correctamente a la campa�a ",
				// Toast.LENGTH_SHORT);
				// t.show();
			}

		});
		
		comentar = new TareaWSComentar();
		botonComentar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder adb = new AlertDialog.Builder(Activity1.this);
				adb.setTitle("Datos");
				LayoutInflater inflater=Activity1.this.getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialogo, null);
				adb.setView(layout);
				comentarioEnvio=(EditText)layout.findViewById(R.id.envioComentario);
				adb.setMessage("�Desea comentar?");

				adb.setIcon(android.R.drawable.ic_menu_compass);

				adb.setPositiveButton("Si",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								comentar.execute();

								Intent intent = new Intent(Activity1.this,
										Activity1.class);
								intent.putExtra("id", id);
								intent.putExtra("user_id", user_id);
								intent.putExtra("login", login);
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

	private class TareaWSObtener extends AsyncTask<String, Integer, Boolean> {

		private String brand;
		private String title;
		private String precio;
	
		private String desc;
		private String fechaFin;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Activity1.this);
			pDialog.setMessage("Cargando");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			// String url =
			// "http://biddusavatar.s3.amazonaws.com/5bdf1ca694cc9932c273f63a0876ba39a9a32299.png?1402655684";
			// imagen = descargarImagen(url);
			// imagen = Bitmap.createScaledBitmap(imagen, 150, 150, true);

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet(
					"http://demo.biddus.com/api/v1/proposals/" + id);

			del.setHeader("content-type", "application/json");

			del.addHeader("Authorization", "Token token=\"" + login + "\"");
			
			
			HttpClient httpClient2 = new DefaultHttpClient();

			HttpGet getComent = new HttpGet(
					"http://demo.biddus.com/api/v1/proposals/" + id+"/comments");

			getComent.setHeader("content-type", "application/json");

			getComent.addHeader("Authorization", "Token token=\"" + login + "\"");

			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());

				JSONObject object = new JSONObject(respStr);
				// Log.v("Object", object.toString());

				JSONObject auxObject = object.getJSONObject("data");
				// Log.v("JSON2", auxObject.toString());

				JSONObject respJSON = auxObject.getJSONObject("proposal");
				JSONArray respJSON2 = respJSON.getJSONArray("products");

				title = respJSON.getString("title");
				precio = respJSON.getString("max_price");

				for (int i = 0; i < respJSON2.length(); i++) {
					JSONObject obj = respJSON2.getJSONObject(i);
					marca = obj.getString("brand");
					modelo = obj.getString("model");
				}

				fechaFin = respJSON.getString("close_date");
				fechaFin = fechaFin.substring(0, 10);

				String annoFin = fechaFin.substring(0, 4);
				String mesFin = fechaFin.substring(5, 7);
				String diaFin = fechaFin.substring(8, 10);

				// brand = respJSON.getString("brand");
				// modelo = respJSON.getString("model");

				// Aqu� ir�a la fecha de fin de la campa�a que hasta ahora
				// no la tenemos en el JSON
				// fechaFin = respJSON.getString("created_at");
				// fechaFin = (String) fechaFin.subSequence(0, 10);
				// desc = respJSON.getString("description");

				if (Integer.parseInt(annoFin) < anno) {

					boton.setEnabled(false);

				} else if (Integer.parseInt(annoFin) == anno) {

					if (Integer.parseInt(mesFin) < mes) {
						boton.setEnabled(false);

					} else if (Integer.parseInt(mesFin) == mes) {
						if (Integer.parseInt(diaFin) < dia) {
							boton.setEnabled(false);

						} else {

						}
					} else {

					}
				} else {

				}
				
				
				/*
				 * A partir de aqu� para los comentarios
				 */
				HttpResponse respComent = httpClient2.execute(getComent);
				String respStrComent = EntityUtils.toString(respComent.getEntity());
				
				JSONObject objComent = new JSONObject(respStrComent);
				
				JSONObject objDataComent = objComent.getJSONObject("data");
				
				JSONArray arrayComent = objDataComent.getJSONArray("comments");
				
				for (int i = 0; i < arrayComent.length(); i++) {
					JSONObject obj = arrayComent.getJSONObject(i);
					Comentarios com = new Comentarios();
					com.contentComent=obj.getString("content");
					com.usuarioComent=obj.getString("user_biddus_name");
					com.id=obj.getString("id");
					com.idRespuesta=obj.getString("ancestry");
					comentarios.add(com);
				}

			}  catch (JSONException e) {
				Log.v("ServicioRest", "No hay comentarios!");

			}catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				ex.printStackTrace();
				resul = false;

			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {

			if (result) {
				boton.setVisibility(1);
				botonComentar.setVisibility(1);
				TXTMarca.setVisibility(1);
				TXTModelo.setVisibility(1);
				TXTPrecio.setVisibility(1);
				TXTFechaFin.setVisibility(1);
				TVPreComent.setVisibility(1);

				titulo.setText(title + "\n");
				// titulo.setText(brand + " " + modelo + "\n");
				// imagenDetalle.setImageBitmap(imagen);
				// lblFechaFin.setText("\t" + fechaFin);
				// lblResultado.setText("\n\n " + title + "\n" + "Precio:"+
				// precio);

				TVmarca.setText("" + marca);
				TVmodelo.setText("" + modelo);
				TVprecio.setText("" + precio);
				TVfechaFin.setText("" + fechaFin);
				String pruebaSt= new String();
				for(int i=0;i<comentarios.size();i++)
				{
					pruebaSt+=comentarios.get(i).usuarioComent+":";
					pruebaSt+="\n"+comentarios.get(i).contentComent+"\n";
					for(int j=0;j<comentarios.size();j++)
					{
						if(comentarios.get(i).id.equals(comentarios.get(j).idRespuesta))
						{
							pruebaSt+="Respuesta a "+comentarios.get(i).usuarioComent+" de "+comentarios.get(j).usuarioComent+":";
							pruebaSt+=comentarios.get(j).contentComent;
							for(int w=0;w<comentarios.size();w++)
							{
								if(comentarios.get(w).idRespuesta.contains(comentarios.get(j).id))
								{
									pruebaSt+="Respuesta a "+comentarios.get(j).usuarioComent+" de "+comentarios.get(w).usuarioComent+":";
									pruebaSt+=comentarios.get(w).contentComent+"\n";
									for(int p=0;p<comentarios.size();p++)
									{
										if(comentarios.get(p).idRespuesta.contains(comentarios.get(w).id))
										{
											pruebaSt+="Respuesta a "+comentarios.get(w).usuarioComent+" de "+comentarios.get(p).usuarioComent+":";
											pruebaSt+=comentarios.get(p).contentComent+"\n";
											comentarios.remove(p);
										}
									}
									comentarios.remove(w);
								}
							}
							comentarios.remove(j);
						}
						else{}
					}
				}
				TXTComent.setText(""+ pruebaSt);
				pDialog.dismiss();

			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity1, menu);
		return true;
	}
	
	private class TareaWSComentar extends AsyncTask<String, Integer, Boolean> {

		private String brand;
		private String title;
		private String precio;
	
		private String desc;
		private String fechaFin;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Activity1.this);
			pDialog.setMessage("Enviando comentario...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			// String url =
			// "http://biddusavatar.s3.amazonaws.com/5bdf1ca694cc9932c273f63a0876ba39a9a32299.png?1402655684";
			// imagen = descargarImagen(url);
			// imagen = Bitmap.createScaledBitmap(imagen, 150, 150, true);

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost del = new HttpPost(
					"http://demo.biddus.com/api/v1/proposals/" + id+"/comments");

			del.setHeader("content-type", "application/json");

			del.addHeader("Authorization", "Token token=\"" + login + "\"");
			try {
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();

				dato.put("content", comentarioEnvio.getText());
				parentData.put("comment", dato);

				StringEntity entity = new StringEntity(parentData.toString());
				del.setEntity(entity);

				HttpResponse resp = httpClient.execute(del);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

				if (respStr.contains("error")) {

					Log.v("ERROR", "NUBU");
					login = "error";

				} else {

					if (!respStr.equals("true"))
						resul = false;
				}

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				ex.printStackTrace();
				resul = false;

			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {
				pDialog.dismiss();

			

		}
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

	// private class TareaWSInsertar extends AsyncTask<String, Integer, Boolean>
	// {
	//
	// protected Boolean doInBackground(String... params) {
	//
	// boolean resul = true;
	//
	// HttpClient httpClient = new DefaultHttpClient();
	//
	// HttpPost post = new HttpPost(c.getActivity1Insertar(id));
	//
	// post.setHeader("content-type", "application/json");
	// post.addHeader("Authorization", "Token token=\"" + login + "\"");
	//
	// try {
	// // Construimos el objeto cliente en formato JSON
	// JSONObject dato = new JSONObject();
	//
	// dato.put("campaign_id", params[1]);
	// dato.put("user_id", params[1]);
	//
	// StringEntity entity = new StringEntity(dato.toString());
	// post.setEntity(entity);
	//
	// HttpResponse resp = httpClient.execute(post);
	// respStr = EntityUtils.toString(resp.getEntity());
	//
	// Log.v("COSA", respStr);
	//
	// if (!respStr.equals("true"))
	// resul = false;
	// } catch (Exception ex) {
	// Log.e("ServicioRest", "Error!", ex);
	// resul = false;
	// }
	//
	// return resul;
	// }
	//
	// protected void onPostExecute(Boolean result) {
	//
	// if (respStr.contains("false")) {
	// Toast t = Toast.makeText(Activity1.this,
	// "El usuario ya est� apuntado en esta campa�a",
	// Toast.LENGTH_SHORT);
	// t.show();
	// } else {
	// Toast t = Toast.makeText(Activity1.this,
	// "Apuntado correctamente a la campa�a ",
	// Toast.LENGTH_SHORT);
	// t.show();
	// }
	// }
	//
	// }

	private void onRestartApuntado() {
		finish();
	}
	
	
	
	public Bitmap descargarImagen(String imageHttpAddress) {
		URL imageUrl = null;
		Bitmap imagen = null;
		try {
			imageUrl = new URL(imageHttpAddress);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.connect();
			imagen = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return imagen;
	}

}
