package com.ftc.services.invoice.modelo;

import java.io.Serializable;

public class Cabecera implements Serializable {

	private static final long serialVersionUID = 8372722219280582677L;

	private String serie;
	private String folio;
	private String strFecha;
	private String formaDePago;
	private String strSubTotal;
	private double subTotal;
	private String strDescuento;
	private double descuento;
	private String TipoCambio;
	private String moneda;
	private String strTotal;
	private double total;
	private String metodoDePago;
	private String lugarExpedicion;
	private String rfc;
	private String nombre;
	private String strTotalImpuestosTrasladados;
	private double totalImpuestosTrasladados;
	
	public Cabecera() {
		System.out.println("Se genero la cabecera de factura.");
	}

	public Cabecera(String serie, String folio, String fecha, String rfc, String nombre, String formaDePago){
		this.serie = serie;
		this.folio = folio;
		this.strFecha = fecha;
		this.rfc = rfc;
		this.nombre = nombre;
		this.formaDePago = formaDePago;
	}
	
	public void asignaValores(String subTotal, String descuento, String total, String impuestosTrasladados){
		this.setStrSubTotal(subTotal);
		this.setStrDescuento(descuento);
		this.setStrTotal(total);
		this.setStrTotalImpuestosTrasladados(impuestosTrasladados);
	}
	
	public void asignaValores(double subTotal, double descuento, double total, double impuestosTrasladados){
		this.setSubTotal(subTotal);
		this.setDescuento(descuento);
		this.setTotal(total);
		this.setTotalImpuestosTrasladados(impuestosTrasladados);
	}

	public void asignaAdicionales(String formaDePago, String metodoDePago, String tipoCambio, String moneda, String lugarExpedicion){
		this.setFormaDePago(formaDePago);
		this.setMetodoDePago(metodoDePago);
		this.setTipoCambio(tipoCambio);
		this.setMoneda(moneda);
		this.setLugarExpedicion(lugarExpedicion);
	}	
	
	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getStrFecha() {
		return strFecha;
	}

	public void setStrFecha(String strFecha) {
		this.strFecha = strFecha;
	}
	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getStrTotalImpuestosTrasladados() {
		return strTotalImpuestosTrasladados;
	}

	public void setStrTotalImpuestosTrasladados(String strTotalImpuestosTrasladados) {
		try{
			this.totalImpuestosTrasladados = Double.parseDouble(strTotalImpuestosTrasladados);
			this.strTotalImpuestosTrasladados = strTotalImpuestosTrasladados;
		}catch(NumberFormatException e){
			this.strTotalImpuestosTrasladados = "0";
			this.totalImpuestosTrasladados = 0;
			System.out.println("El valor asignado a total de impuestos trasladados no parece ser un numero ["+strTotalImpuestosTrasladados+"].");
		}
	}

	public double getTotalImpuestosTrasladados() {
		return totalImpuestosTrasladados;
	}

	public void setTotalImpuestosTrasladados(double totalImpuestosTrasladados) {
		this.totalImpuestosTrasladados = totalImpuestosTrasladados;
		this.strTotalImpuestosTrasladados = String.valueOf(totalImpuestosTrasladados);
	}

	public String getStrSubTotal() {
		return strSubTotal;
	}

	public void setStrSubTotal(String strSubTotal) {
		try{
			this.subTotal = Double.parseDouble(strSubTotal);
			this.strSubTotal = strSubTotal;			
		}catch(NumberFormatException e){
			this.subTotal = 0;
			this.strSubTotal = "0";
			System.out.println("El valor asignado a subtotal no parece ser un numero ["+strSubTotal+"].");
		}
	}
	
	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
		this.strSubTotal = String.valueOf(subTotal);
	}

	public String getStrDescuento() {
		return strDescuento;
	}

	public void setStrDescuento(String strDescuento) {
		try{
			this.descuento = Double.parseDouble(strDescuento);
			this.strDescuento = strDescuento;
		}catch (NumberFormatException e){
			this.descuento = 0;
			this.strDescuento = "0";
			System.out.println("El valor asignado a descuento no parece ser un numero ["+strDescuento+"].");
		}
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
		this.strDescuento=String.valueOf(descuento);
	}

	public String getStrTotal() {
		return strTotal;
	}

	public void setStrTotal(String strTotal) {
		try{
			this.total = Double.parseDouble(strTotal);
			this.strTotal = strTotal;
		}catch(NumberFormatException e){
			this.total = 0;
			this.strTotal = "0";
			System.out.println("El valor asignado a total no parece ser un numero ["+strTotal+"].");
		}
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
		this.strTotal = String.valueOf(total);
	}
	
	public String getFormaDePago() {
		return formaDePago;
	}

	public void setFormaDePago(String formaDePago) {
		this.formaDePago = formaDePago;
	}
	
	public String getMetodoDePago() {
		return metodoDePago;
	}
	
	public void setMetodoDePago(String metodoDePago) {
		this.metodoDePago = metodoDePago;
	}
	
	public String getTipoCambio() {
		return TipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		TipoCambio = tipoCambio;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getLugarExpedicion() {
		return lugarExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}


	public static String titulosCommaSeparateValues(){
		StringBuilder builder = new StringBuilder();
		builder.append("\"").append("RFC").append("\",");
		builder.append("\"").append("Nombre").append("\",");
		builder.append("\"").append("Serie").append("\",");
		builder.append("\"").append("Folio").append("\",");
		builder.append("\"").append("Fecha").append("\",");
		builder.append("\"").append("Lugar de expedicion").append("\",");
		builder.append("\"").append("Forma de pago").append("\",");
		builder.append("\"").append("Moneda").append("\",");
		builder.append("\"").append("Subtotal").append("\",");
		builder.append("\"").append("Descuento").append("\",");
		builder.append("\"").append("Impuestos trasladados").append("\",");
		builder.append("\"").append("Total").append("\"");
		return builder.toString();
	}
	
	public String toCommaSeparateValues(){
		StringBuilder builder = new StringBuilder();
		builder.append("\"").append(rfc).append("\",");
		builder.append("\"").append(nombre).append("\",");
		builder.append("\"").append(serie).append("\",");
		builder.append("\"").append(folio).append("\",");
		builder.append("\"").append(strFecha).append("\",");
		builder.append("\"").append(lugarExpedicion).append("\",");
		builder.append("\"").append(formaDePago).append("\",");
		builder.append("\"").append(moneda).append("\",");
		builder.append(subTotal).append(",");
		builder.append(descuento).append(",");
		builder.append(totalImpuestosTrasladados).append(",");
		builder.append(total);
		return builder.toString();
	}
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cabecera [serie=");
		builder.append(serie);
		builder.append(", folio=");
		builder.append(folio);
		builder.append(", strFecha=");
		builder.append(strFecha);
		builder.append(", formaDePago=");
		builder.append(formaDePago);
		builder.append(", subTotal=");
		builder.append(subTotal);
		builder.append(", descuento=");
		builder.append(descuento);
		builder.append(", TipoCambio=");
		builder.append(TipoCambio);
		builder.append(", moneda=");
		builder.append(moneda);
		builder.append(", total=");
		builder.append(total);
		builder.append(", metodoDePago=");
		builder.append(metodoDePago);
		builder.append(", lugarExpedicion=");
		builder.append(lugarExpedicion);
		builder.append(", rfc=");
		builder.append(rfc);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", totalImpuestosTrasladados=");
		builder.append(totalImpuestosTrasladados);
		builder.append("]");
		return builder.toString();
	}

}
