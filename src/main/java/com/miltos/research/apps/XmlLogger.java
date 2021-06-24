package com.miltos.research.apps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlLogger {
		
		public static String BASE_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();
		public static String PATH = new File(BASE_DIR + "/Logging/XML_Downloaded_Projects").getAbsolutePath(); 
		
				

		public static void exportXML(Element root, String dest) throws IOException {
				
				// Create the folder
				File destDir = new File(dest);
				destDir.mkdirs();
				
				// Create an XML Outputter
				XMLOutputter outputter = new XMLOutputter();
				
				// Set the format of the outputed XML File
				Format format = Format.getPrettyFormat();
				outputter.setFormat(format);
				
				// Output the XML File to standard output and the desired file
				FileWriter filew = new FileWriter(dest + "/projects.xml");
				outputter.output(root, filew);
				
		}

}
