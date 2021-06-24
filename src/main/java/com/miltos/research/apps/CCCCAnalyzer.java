package com.miltos.research.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This class is responsible for analyzing a single project 
 * by invoking the CCCC tool.
 * 
 * This can be done by using the first and the second method
 * of this class respectively. 
 * 
 * @author Miltiadis Siavvas
 *
 */
public class CCCCAnalyzer {
	
	// Define the official name of the tool
	public static final String TOOL_NAME = "CCCC";
	public static final boolean DEBUG = false;
	
	/**
	 * This method is used to analyze a single project with the CCCC static analysis 
	 * tool.
	 * 
	 * ATTENTION: 
	 *  - The CCCC static analysis tool does not support ANT
	 * 
	 * @param src      : The path of the folder that contains the class files of the project.
	 * @param dest     : The path where the XML file that contains the results will be placed.
	 * 
	 */
	public void analyze(String src, String dest){

		// Get the source and the destination paths
		src = new File(src).getAbsolutePath();
		dest = new File(dest).getAbsolutePath();

		// Construct the parts of the command		
		String dumpFolder = "./temp";
		String destFile = dest +"/ccccResults.xml";
		
		// Quick Fix - Add the '%' character in order to solve a CCCC bug
//		src = src.replaceAll(" ", "%");
//		destFile = destFile.replaceAll(" ", "%");
		
		// Construct the command that should be executed
		String command = "D: & cd \"" + src + "\" & dir /b/s | cccc --outdir=\"" + dumpFolder + "\" --xml_outfile=\"" + destFile + "\" -";
		
		//TODO: Remove this print
		if (DEBUG) {
			System.out.println(command);
		}

		// Check if the output folder exists. If not, create a new folder.
		File destF = new File(dest);
		if (!destF.exists()) {
			destF.mkdir();
		}
		
		// Create a ProcessBuilder to execute the command
		ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c", command);
		builder.redirectErrorStream(true);

		// Execute the command
		try{
				Process p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

				// Print the console output for debugging purposes
				String line;
				while (true) {
					line = r.readLine();
					if (line == null) { break;}
					// TODO: Remove this print
					if (DEBUG) {
						System.out.println(line);
					}
					
				}

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is responsible for analyzing a single project against a set of 
	 * properties by using the CCCC Tool.
	 * 
	 * @param src      : The path of the folder that contains the class files of the desired project.
	 * @param dest     : The path where the XML file that contains the results should be placed.
	 *
	 * Typically this method does the following:
	 * 		
	 * 		1. Iterates through the PropertySet.
	 * 		2. If it finds at least one property that uses the CKJM tool then it calls the 
	 * 			simple analyze() method.
	 * 
	 * IDEA:
	 *   - All the metrics are calculated for the project and then loaded by the program.
	 *   - After that we decide which metrics to keep by iterating through the PropertySet of
	 *     the Quality Model.
	 *     
	 * It has this form in order to look the same with the PMDAnalyzer.
	 */
//		public void analyze(String src, String dest, PropertySet properties){
//
//		// Iterate through the properties of the desired PropertySet object
//		Iterator<Property> iterator = properties.iterator();
//		Property p = null;
//
//		// For each property found in the desired PropertySet do...
//		while(iterator.hasNext()){
//			
//			// Get the current property
//			p = iterator.next();
//			
//			//TODO: Remove these prints
//			if (DEBUG) {
//				System.out.println("* Property Type: " + p.getMeasure().getTool());
//			}
//
//			//Check if it is a CCCC property
//			//TODO: Check this outside this function
//			if(p.getMeasure().getTool().equals(CCCCAnalyzer.TOOL_NAME) && p.getMeasure().getType() == Measure.METRIC){//Redundant condition!!!
//				
//				//TODO: Remove this print 
//				if (DEBUG) {
//					System.out.println("Analyzing the code...");
//				}
//				
//				//Analyze this project
//				analyze(src, dest);
//				
//				//Found at least one ckjm property. Process finished.
//				break;
//				
//			}else{
//				//Print some messages for debugging purposes
//				//System.out.println("* Property : " + p.getName() + " is not a CKJM Property!!");
//			}
//		}
//	}
//	
//	/**
//	 * The following main method is for testing purposes only.
//	 */
//	public static void main(String[] args) {
//	
//		// 1. Construct a CCCCAnalyzer
//		CCCCAnalyzer cccc = new CCCCAnalyzer();
//		
//		// 2. Set the desired paths
//		//String src = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\RemoteSystemsTempFiles").getAbsolutePath();
//		//String src = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\C++_applications\\dijkstra").getAbsolutePath();
//		String src = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\Compiled10\\22.webmagic").getAbsolutePath();
//		
//		String dest = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\ResultsCCCC").getAbsolutePath();
//		
//		//src = src.replaceAll(" ", "%");
//		//dest = dest.replaceAll(" ", "%");
//		
//		// 3. Analyze the selected software application
//		//cccc.analyze(src, dest);
//		
//		// 4. Create a set of Properties
//		PropertySet propertySet = new PropertySet();
//		
//		Property p1 = new Property();
//		Property p2 = new Property();
//		Property p3 = new Property();
//		Property p4 = new Property();
//		
//		p1.setMeasure(new Measure().setTool(CKJMAnalyzer.TOOL_NAME));
//		p2.setMeasure(new Measure().setTool(PMDAnalyzer.TOOL_NAME));
//		p3.setMeasure(new Measure().setTool(CCCCAnalyzer.TOOL_NAME));
//		p4.setMeasure(new Measure().setTool(CCCCAnalyzer.TOOL_NAME));
//		
//		propertySet.addProperty(p1);
//		propertySet.addProperty(p2);
//		propertySet.addProperty(p3);
//		propertySet.addProperty(p4);
//		
//		// 5. Analyze Properties
//		cccc.analyze(src, dest, propertySet);
//		
//
//	}
	
	/**
	 * This method is responsible for analyzing a group of projects with the CKJM analyzer.
	 */
	public void analyzeBenchmark(String src, String dest){

	// 1. Get all the projects
	File srcFolder = new File(src);
	File destFolder = new File(dest);
	File[] projects = srcFolder.listFiles();
	
	// 2. For each project found in the source folder
	for (File project : projects) {
		
		//TODO: Remove these prints
		if (DEBUG) {
			System.out.println("Project :" + project.getName());
			System.out.println(" Source Path: " + project.getAbsolutePath());
			System.out.println(" Dest Path: " + destFolder.getAbsolutePath() + "/" + project.getName());
		}
		
		// Invoke the analysis for the present software project
		analyze(project.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + project.getName());
	}
}
	
	/**
	 * This method is responsible for constructing the final repository, i.e., the repository
	 * that contains software applications that can be successfully analyzed using the CCCC 
	 * Extended tool.
	 */
	public static void copyApplicable(String src, String ckjm, String dest) throws JDOMException, IOException {
		
		// 0. Initialize the required folders
		File srcFolder = new File(src);
		File ckjmFolder = new File(ckjm);
		File destFolder = new File(dest);
		destFolder.mkdirs();
		
		// 1. Get the projects
		File[] projects = ckjmFolder.listFiles();
		
		// 2. For each project in the source file ...
		int k = 0;
		for (File project : projects) {
			
			// 2.1 Get the project name
			String projectName = project.getName();
			
			// 2.2 Get the containing files
			File[] resultFile = project.listFiles();
			
			if (resultFile.length != 0) {
				System.out.println("Project: " + projectName + " YES");
				// Increment the counter
				k++;
				
				// Create the folder that corresponds to this project
				File folder = new File(destFolder.getAbsolutePath() + "/" + project.getName());
				folder.mkdirs();
				
				// Copy the contents of the source folder to the destination directory
				FileUtils.copyDirectory(new File(src + "/" + projectName), folder);
			} else {
				System.out.println("Project: " + projectName + " NO");
			}
				
			
			
			
			// 2.4 Check if it contains any child nodes
//			if (children.size() != 0) {
//				
//				//TODO: Remove this print
//				if (printMessages) {
//					System.out.println("Project: " + projectName + " CKJM: YES");
//				}
//				
//				// Increment the counter
//				k++;
//				
//				// Create the folder that corresponds to this project
//				File folder = new File(destFolder.getAbsolutePath() + "/" + project.getName());
//				folder.mkdirs();
//				
//				// Copy the contents of the source folder to the destination directory
//				FileUtils.copyDirectory(project, folder);
//				
//			} else {
//				if (printMessages) {
//					System.out.println("Project: " + projectName + " CKJM: NO");
//				}
//			}
				

			
		}
//		if (printMessages) {
//			System.out.println("Applicable: " + k);
//		}
		
	}

}
