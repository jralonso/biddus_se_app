package com.occulus.vendedoresapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Pedidos {
	protected Drawable foto;
	protected String titulo;
//	protected String cantOffer;

	protected Bitmap foto2;
	protected String titulo2;
	protected Drawable img;

	public Pedidos(Drawable foto, String titulo) {
		super();
		this.foto = foto;
		this.titulo = titulo;
	}

//	public String getCantOffer() {
//		return cantOffer;
//	}
//
//	public void setCantOffer(String cantOffer) {
//		this.cantOffer = cantOffer;
//	}

	public Pedidos(Bitmap foto2, String titulo2, Drawable img) {
		super();
		this.foto2 = foto2;
		this.titulo2 = titulo2;
		this.img = img;
	}

	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	public Bitmap getFoto2() {
		return foto2;
	}

	public void setFoto2(Bitmap foto2) {
		this.foto2 = foto2;
	}

	public String getTitulo2() {
		return titulo2;
	}

	public void setTitulo2(String titulo2) {
		this.titulo2 = titulo2;
	}

	public Drawable getFoto() {
		return foto;
	}

	public void setFoto(Drawable foto) {
		this.foto = foto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
