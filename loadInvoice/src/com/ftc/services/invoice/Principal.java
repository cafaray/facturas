package com.ftc.services.invoice;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ftc.services.invoice.modelo.Cabecera;

public class Principal {

	public static void main(String[] args) {
		String folderIn = "/tmp/";
		String folderOut = folderIn;
		try {
			if (args.length >= 1) {
				folderIn = args[0];
				folderOut = folderIn;
			} else if (folderIn.length() <= 0) {
				System.out
						.println("No se detecto una ruta para localizar documentos. Usa loadInvoice <directorio_facturas> [directorio_salida]");
				System.exit(1);
			}
			if (args.length == 2) {
				folderOut = args[1];
			}
			File folder = new File(folderIn);
			System.out.println("La ruta al archivo " + folderIn + " existe: "
					+ folder.exists());
			if (folder.isDirectory()) {
				FilenameFilter filter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".xml");
					}
				};
				File[] xfiles = folder.listFiles(filter);
				List<Cabecera> registros = new ArrayList<>();
				for (File xfile : xfiles) {
					Cabecera cabecera = procesaXML(xfile.getAbsolutePath());
					registros.add(cabecera);
				}
				int resultado = escribeSalida(folderOut, registros);
				if (resultado < 0) {
					System.out
							.println("Hubo un error durante la escritura de los registros. Revise el log de salida.");
					System.exit(1);
				}
			} else {
				System.out
						.println("La ruta especificada no es un directorio valido ["
								+ folderIn + "]");
				System.exit(1);
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	private static int escribeSalida(String folder, List<Cabecera> registros)
			throws IOException {
		File file = new File(folder);
		int iregistros = 0;
		if (file.exists()) {
			Calendar hoy = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyymmdd-hhmm");

			String yfileName = folder + File.separator
					+ format.format(hoy.getTime()) + ".csv";
			File yfile = new File(yfileName);
			if (yfile.exists()) {
				System.out.printf("El archivo %s existe, sera eliminado: ",
						yfileName);
				yfile.delete();
				System.out.println("[OK]");
			}
			System.out.printf("Archivo %s listo para escribir: ", yfileName);
			if (yfile.createNewFile()) {
				System.out.println("[OK]");
				FileOutputStream fos = new FileOutputStream(yfile);
				DataOutputStream dos = new DataOutputStream(fos);
				dos.writeChars(Cabecera.titulosCommaSeparateValues()
						+ System.lineSeparator());
				for (Cabecera registro : registros) {
					dos.writeChars(registro.toCommaSeparateValues()
							+ System.lineSeparator());
					iregistros++;
				}
				fos.close();
			}
			return iregistros;
		} else {
			System.out
					.println("El directorio de salida no parece ser valido. Error en la ejecución.");
			return -1;
		}
	}

	private static Cabecera procesaXML(String file) throws IOException,
			ParserConfigurationException, SAXException {
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		System.out.println("Root element :"
				+ doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("cfdi:Comprobante");
		System.out.println("----------------------------");
		Cabecera cabecera = new Cabecera();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				System.out.println("Serie : " + eElement.getAttribute("serie"));
				System.out.println("Folio : " + eElement.getAttribute("folio"));
				System.out.println("Fecha : " + eElement.getAttribute("fecha"));
				System.out.println("Forma de pago : "
						+ eElement.getAttribute("formaDePago"));
				System.out.println("Sub total : "
						+ eElement.getAttribute("subTotal"));
				System.out.println("Descuento : "
						+ eElement.getAttribute("descuento"));
				System.out.println("Tipo de cambio : "
						+ eElement.getAttribute("TipoCambio"));
				System.out.println("Moneda : "
						+ eElement.getAttribute("moneda"));
				System.out.println("Total : " + eElement.getAttribute("total"));
				System.out.println("Metodo de pago : "
						+ eElement.getAttribute("metodoDePago"));
				System.out.println("Lugar de expedición : "
						+ eElement.getAttribute("LugarExpedicion"));
				cabecera.setSerie(eElement.getAttribute("serie"));
				cabecera.setFolio(eElement.getAttribute("folio"));
				cabecera.setStrFecha(eElement.getAttribute("fecha"));
				cabecera.setFormaDePago(eElement.getAttribute("formaDePago"));
				cabecera.setStrSubTotal(eElement.getAttribute("subTotal"));
				cabecera.setStrDescuento(eElement.getAttribute("descuento"));
				cabecera.setTipoCambio(eElement.getAttribute("TipoCambio"));
				cabecera.setMoneda(eElement.getAttribute("moneda"));
				cabecera.setStrTotal(eElement.getAttribute("total"));
				cabecera.setMetodoDePago(eElement.getAttribute("metodoDePago"));
				cabecera.setLugarExpedicion(eElement
						.getAttribute("LugarExpedicion"));
			}
		}

		nList = doc.getElementsByTagName("cfdi:Emisor");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("RFC : " + eElement.getAttribute("rfc"));
				System.out.println("Nombre : "
						+ eElement.getAttribute("nombre"));
				cabecera.setRfc(eElement.getAttribute("rfc"));
				cabecera.setNombre(eElement.getAttribute("nombre"));
				// eElement.getElementsByTagName("firstname").item(0).getTextContent()
			}
		}

		nList = doc.getElementsByTagName("cfdi:Impuestos");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("Impuestos trasladados : "
						+ eElement.getAttribute("totalImpuestosTrasladados"));
				cabecera.setStrTotalImpuestosTrasladados(eElement
						.getAttribute("totalImpuestosTrasladados"));
				// eElement.getElementsByTagName("firstname").item(0).getTextContent()
			}
		}
		// System.out.println("Valor del objeto: "+cabecera.toString());
		return cabecera;
	}

}
