package com.ftc.invoice.script

import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.List

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

import com.ftc.services.invoice.model.Cabecera

class Script {

	static main(args) {

		def folderIn = "/tmp/"
		if(args.size()>0){
			folderIn = args[0] 
		}
		def folderOut = folderIn
		try {
			if (args.length >= 1) {
				folderIn = args[0]
				folderOut = folderIn
			} else if (folderIn.length() <= 0) {
				println("No se detecto una ruta para localizar documentos."
					+ " Usa loadInvoice <directorio_facturas> [directorio_salida]")
				System.exit(1)
			}
			if (args.length == 2) {
				folderOut = args[1]
			}
			File folder = new File(folderIn)
			printf("El directorio %s existe: %s%n", folderIn, folder.exists()?"Sí":"No")
			if (folder.isDirectory()) {
				def applyFilter = {directory, name -> name.endsWith(".xml")}
				def xfiles = folder.listFiles(applyFilter as FilenameFilter)
				List<Cabecera> registros = new ArrayList<>()
				for (xfile in xfiles) {
					Cabecera cabecera = procesaXML(xfile.getAbsolutePath())
					registros.add(cabecera)
				}

				def resultado = escribeSalida(folderOut, registros)
				if (resultado < 0) {
					println("Hubo un error durante la escritura de los registros. Revise el log de salida.")
					System.exit(1)
				}
			} else {
				println("La ruta especificada no es un directorio valido ["+ folderIn + "]")
				System.exit(1)
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace()
		}

		//Cabecera c = new Cabecera(serie:'A', folio:'12345')
		//println c.toString()
	}

	static def escribeSalida(folder, List<Cabecera> registros) {
		File file = new File(folder)
		int iregistros = 0
		if (file.exists()) {
			Calendar hoy = Calendar.getInstance()
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmm")

			String yfileName = folder + File.separator + format.format(hoy.getTime()) + ".csv"
			File yfile = new File(yfileName)
			if (yfile.exists()) {
				printf("El archivo %s existe, sera eliminado: ", yfileName)
				yfile.delete()
				println("[OK]")
			}
			printf("Archivo %s listo para escribir: ", yfileName)
			if (yfile.createNewFile()) {
				println("[OK]")
				FileOutputStream fos = new FileOutputStream(yfile)
				DataOutputStream dos = new DataOutputStream(fos)
				dos.writeChars(Cabecera.titulosCommaSeparateValues()
								+ System.lineSeparator())
				for (registro in registros) {
					dos.writeChars(registro.toCommaSeparateValues()
									+ System.lineSeparator())
					iregistros++
				}
				fos.close()
			}
			return iregistros
		} else {
			println("El directorio de salida no parece ser valido. Error en la ejecución.")
			return -1
		}
	}

	static Cabecera procesaXML(def file)  {
		
		
		def xml = new File(file).text
		def root = new XmlParser().parseText(xml)
		//println root		
		boolean esCFDI = root.'cfdi:Emisor'.'@rfc'.text().length()>0
		println "Comprobante es CFDI: ${esCFDI}"		
		String prefijo = (esCFDI?"cfdi:":"")		
		println "Emisor: "+root."${prefijo}Emisor".'@rfc'.text()
		
		println("----------------------------")
		Cabecera cabecera = new Cabecera()
		cabecera.serie = root."@serie";
		cabecera.folio = root."@folio";
		cabecera.fecha = root."@fecha";
		cabecera.formaDePago = root."@formaDePago";
		cabecera.subTotal = root."@subTotal";
		cabecera.descuento = root."@descuento"?root."@descuento":0;
		cabecera.tipoCambio = root."@TipoCambio";
		cabecera.moneda = root."@Moneda";
		cabecera.total = root."@total";
		cabecera.metodoDePago = root."@metodoDePago";
		cabecera.lugarExpedicion = root."@LugarExpedicion";
		
		cabecera.nombre = root."${prefijo}Emisor"."@nombre".text()
		cabecera.rfc = root."${prefijo}Emisor"."@rfc".text()
		cabecera.nombreReceptor = root."${prefijo}Receptor"."@nombre".text()
		cabecera.rfcReceptor = root."${prefijo}Receptor"."@rfc".text()
		cabecera.totalImpuestosTrasladados = root."${prefijo}Impuestos".'@totalImpuestosTrasladados'.text()
		
		def intImpuestos = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado".size()
		for (int x=0;x<intImpuestos;x++){
			def tipoImpuesto = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado"[x]."@impuesto"
			if(tipoImpuesto=="IEPS"){				
				cabecera.iepsTasa = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado"[x]."@tasa"
				cabecera.iepsImporte = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado"[x]."@tasa"
			}else if(tipoImpuesto=="IVA"){
				cabecera.ivaTasa = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado"[x]."@tasa"
				cabecera.ivaImporte = root."${prefijo}Impuestos"."${prefijo}Traslados"."${prefijo}Traslado"[x]."@importe"
			}else{
				println "No se reconoce el tipo de impuesto en la lista: "+tipoImpuesto
			}
		}

		if (esCFDI){
			cabecera.uuid = root."cfdi:Complemento"."tfd:TimbreFiscalDigital"."@UUID".text()
			cabecera.fechaTimbrado = root."cfdi:Complemento"."tfd:TimbreFiscalDigital"."@FechaTimbrado".text()
		}else{
			cabecera.uuid = "N/A"
			cabecera.fechaTimbrado = "N/A"
		}

		return cabecera
	}
}
