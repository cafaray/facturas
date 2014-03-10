package com.ftc.services.invoice;

import java.io.File;
import java.io.FilenameFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Principal {

	private static String pathFolder="/tmp/";
	
	public static void main(String[] args) {
		try {
			
			if(args.length==1){
				pathFolder = args[0];
			}else if(pathFolder.length()<=0){
				System.out.println("No se detecto una ruta para localizar documentos.");				
				System.exit(1);
			}
			File folder = new File(pathFolder);
			System.out.println("La ruta al archivo "+pathFolder+" existe: "+folder.exists());
			if(folder.isDirectory()){				
				FilenameFilter filter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".xml");						
					}
				};
				File[] xfiles = folder.listFiles(filter);
				for (File xfile:xfiles){
					procesaXML(xfile.getAbsolutePath());
				}
			}else{
				System.out.println("La ruta especificada no es un directorio valido ["+pathFolder+"]");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void procesaXML(String file)throws Exception{
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("cfdi:Emisor"); //cfdi:Impuestos
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("RFC : "
						+ eElement.getAttribute("rfc"));
				System.out.println("Nombre : "
						+ eElement.getAttribute("nombre"));
				//eElement.getElementsByTagName("firstname").item(0).getTextContent()
			}
		}				
	}
	
}
