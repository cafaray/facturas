package com.ftc.services.invoice.model

class Cabecera implements Serializable {

	private static final long serialVersionUID = 8372722219280582677L;

	def serie;
	def folio;
	def fecha;
	def formaDePago;
	def subTotal;
	def descuento;
	def tipoCambio;
	def moneda;
	def total;
	def metodoDePago;
	def lugarExpedicion;
	def rfc;
	def nombre;
	def totalImpuestosTrasladados;
	def uuid;
	def fechaTimbrado;
	def rfcReceptor;
	def nombreReceptor;
	
	def ivaTasa;
	def ivaImporte;
	
	def iepsTasa = 0;
	def iepsImporte = 0;
	
	public Cabecera() {}

	public static String titulosCommaSeparateValues(){
		StringBuilder builder = new StringBuilder();
		builder.append("\"").append("RFC").append("\",");
		builder.append("\"").append("Nombre").append("\",");
		builder.append("\"").append("Serie").append("\",");
		builder.append("\"").append("Folio").append("\",");
		builder.append("\"").append("Fecha").append("\",");
		builder.append("\"").append("Lugar de expedicion").append("\",");
		builder.append("\"").append("UUID").append("\",");
		builder.append("\"").append("Fecha de timbrado").append("\",");
		builder.append("\"").append("Forma de pago").append("\",");
		builder.append("\"").append("Moneda").append("\",");
		builder.append("\"").append("RFC-Receptor").append("\",");
		builder.append("\"").append("Receptor").append("\",");
		builder.append("\"").append("Subtotal").append("\",");
		builder.append("\"").append("Descuento").append("\",");
		builder.append("\"").append("Impuestos trasladados").append("\",");
		builder.append("\"").append("Tasa IVA").append("\",");
		builder.append("\"").append("IVA").append("\",");
		builder.append("\"").append("Tasa IEPS").append("\",");
		builder.append("\"").append("IEPS").append("\",");
		builder.append("\"").append("Total").append("\"");
		return builder.toString();
	}
	
	public String toCommaSeparateValues(){
		StringBuilder builder = new StringBuilder();
		builder.append("\"").append(rfc).append("\",");
		builder.append("\"").append(nombre).append("\",");
		builder.append("\"").append(serie).append("\",");
		builder.append("\"").append(folio).append("\",");
		builder.append("\"").append(fecha).append("\",");
		builder.append("\"").append(lugarExpedicion).append("\",");
		builder.append("\"").append(uuid).append("\",");
		builder.append("\"").append(fechaTimbrado).append("\",");
		builder.append("\"").append(formaDePago).append("\",");
		builder.append("\"").append(moneda).append("\",");
		builder.append("\"").append(rfcReceptor).append("\",");
		builder.append("\"").append(nombreReceptor).append("\",");
		builder.append(subTotal).append(",");
		builder.append(descuento).append(",");
		builder.append(totalImpuestosTrasladados).append(",");
		builder.append(ivaTasa).append(",");
		builder.append(ivaImporte).append(",");
		builder.append(iepsTasa).append(",");
		builder.append(iepsImporte).append(",");
		builder.append(total);
		return builder.toString();
	}

}
