package com.ftc.services.invoice;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
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
		String yfileName = "hashPipe";
		try {
			if (args.length > 0) {
				for (String arg:args){
					if(arg.startsWith("--dir=")){
						folderIn = arg.substring(arg.indexOf("=")+1);
						System.out.println("Estableciendo folder de trabajo en: "+folderIn);
					}else if(arg.startsWith("--file=")){
						yfileName = arg.substring(arg.indexOf("=")+1);
						System.out.println("Estableciendo nombre de archivo como: "+yfileName);
					}else{
						System.out.printf("No se reconoce el argumento %s. Usa --dir=[directorio] o --file=[nombre archivo]");
						System.exit(1);
					}
				}
			}
			folderOut = folderIn;
			
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

				int resultado = escribeSalida(folderOut, yfileName, registros);
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

	private static int escribeSalida(String folder, String yfileName, List<Cabecera> registros)
			throws IOException {
		File file = new File(folder);
		int iregistros = 0;
		if (file.exists()) {
			//Calendar hoy = Calendar.getInstance();
			//SimpleDateFormat format = new SimpleDateFormat("yyyymmdd-hhmm");

			//String yfileName = folder + File.separator
			//		+ format.format(hoy.getTime()) + ".csv";
			yfileName+=".csv";
			File yfile = new File(new String(folder+yfileName));
			if (yfile.exists()) {
				System.out.printf("El archivo %s existe, sera eliminado: ",
						yfileName);
				yfile.delete();
				System.out.println("[OK]");
			}
			System.out.printf("Archivo %s listo para escribir: ", yfileName);
			if (yfile.createNewFile()) {
				System.out.println("[OK]");
				try (FileOutputStream fos = new FileOutputStream(yfile);
						DataOutputStream dos = new DataOutputStream(fos)) {
					dos.writeChars(Cabecera.titulosCommaSeparateValues()
							+ System.lineSeparator());
					for (Cabecera registro : registros) {
						dos.writeChars(registro.toCommaSeparateValues()
								+ System.lineSeparator());
						iregistros++;
					}
					// fos.close();
				} catch (IOException ioe) {
					throw ioe;
				}
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
		boolean esCFDI = true;
		esCFDI = doc.getDocumentElement().getNodeName().toUpperCase()
				.startsWith("CFDI");
		System.out.println("El documento es CFDI? " + esCFDI);
		String prefijo = (esCFDI ? "cfdi:" : "");

		NodeList nList = doc.getElementsByTagName(prefijo + "Comprobante");

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
						+ eElement.getAttribute("Moneda"));
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
				cabecera.setMoneda(eElement.getAttribute("Moneda"));
				cabecera.setStrTotal(eElement.getAttribute("total"));
				cabecera.setMetodoDePago(eElement.getAttribute("metodoDePago"));
				cabecera.setLugarExpedicion(eElement
						.getAttribute("LugarExpedicion"));
			}
		}

		nList = doc.getElementsByTagName(prefijo + "Emisor");
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

		nList = doc.getElementsByTagName(prefijo + "Receptor");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("RFC : " + eElement.getAttribute("rfc"));
				System.out.println("Nombre : "
						+ eElement.getAttribute("nombre"));
				cabecera.setRfcReceptor(eElement.getAttribute("rfc"));
				cabecera.setNombreReceptor(eElement.getAttribute("nombre"));
			}
		}

		nList = doc.getElementsByTagName(prefijo + "Impuestos");
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

		nList = doc.getElementsByTagName(prefijo + "Traslados");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.printf("\nCurrent Element : %s-%s%n",
					nNode.getNodeType(), nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				// Element eElement = (Element) nNode;
				NodeList nodes = nNode.getChildNodes();
				System.out.println("Impuestos encontrados : "
						+ nodes.getLength());
				for (int ximpuesto = 0; ximpuesto < nodes.getLength(); ximpuesto++) {
					Node nNodeImpuesto = nodes.item(ximpuesto);

					if (nNodeImpuesto.getNodeType() == Node.ELEMENT_NODE) {
						System.out.printf("\nCurrent Element : %s-%s%n",
								nNodeImpuesto.getNodeType(),
								nNodeImpuesto.getNodeName());
						Element xElement = (Element) nNodeImpuesto;
						String impuesto, tasa, importe;
						impuesto = xElement.getAttribute("impuesto");
						tasa = xElement.getAttribute("tasa");
						importe = xElement.getAttribute("importe");
						System.out.printf(
								"impuesto = %s, tasa = %s, importe = %s %n",
								impuesto, tasa, importe);
						if (impuesto.equals(Cabecera.IMPUESTO_IVA)) {
							cabecera.setIva_strTasa(tasa);
							cabecera.setIva_strImporte(importe);
						} else if (impuesto.equals(Cabecera.IMPUESTO_IEPS)) {
							cabecera.setIeps_strTasa(tasa);
							cabecera.setIeps_strImporte(importe);
						} else {
							System.out
									.println("Impuesto no detectado en el objeto: "
											+ impuesto);
						}
					}
				}
			}
		}

		nList = doc.getElementsByTagName("tfd:TimbreFiscalDigital");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("UUID : " + eElement.getAttribute("UUID"));

				System.out.println("Fecha de timbrado: "
						+ eElement.getAttribute("FechaTimbrado"));

				cabecera.setUuid(eElement.getAttribute("UUID"));
				cabecera.setStrFechaTimbrado(eElement
						.getAttribute("FechaTimbrado"));
				// eElement.getElementsByTagName("firstname").item(0).getTextContent()
			}
		}

		// System.out.println("Valor del objeto: "+cabecera.toString());
		return cabecera;
	}

}
