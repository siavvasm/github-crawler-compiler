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

public class CKJMAnalyzer {
	
	// Testing flags
	private static boolean printMessages = true;
	
	/**
	 * This method is used to analyze a single project with the CKJM static analysis 
	 * tool.
	 * 
	 * ATTENTION: 
	 *  - The appropriate build.xml ant file should be placed inside the eclipse folder.
	 *  - TODO: Check if you can provide the path of the build.xml.
	 * 
	 * @param src      : The path of the folder that contains the class files of the project.
	 * @param dest     : The path where the XML file that contains the results will be placed.
	 * 
	 */
	public void analyze(String src, String dest){

		// 1. Retrieve the source and destination folders
		src = new File(src).getAbsolutePath();
		dest = new File(dest).getAbsolutePath();

		//TODO: Remove these prints
		if (printMessages) {
			System.out.println("ant  -Dsrc.dir="+ src +" -Ddest.dir="+ dest);
		}
		
		// 2. Configure the command that should be executed
		ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c","ant  -Dsrc.dir="+ src +" -Ddest.dir="+ dest);
		builder.redirectErrorStream(true);

		// 3. Execute the command
		try{
				Process p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

				// Print the console output for debugging purposes
				String line;
				while (true) {
					line = r.readLine();
					//System.out.println(line);
					if (line == null) { break; }
				}

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

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
		if (printMessages) {
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
	 * that contains software applications that can be successfully analyzed using the CKJM 
	 * Extended tool.
	 */
	public static void copyApplicable(String src, String ckjm, String dest) throws JDOMException, IOException {
		
		// 0. Initialize the required folders
		File srcFolder = new File(src);
		File ckjmFolder = new File(ckjm);
		File destFolder = new File(dest);
		destFolder.mkdirs();
		
		// 1. Get the projects
		File[] projects = srcFolder.listFiles();
		
		// 2. For each project in the source file ...
		int k = 0;
		for (File project : projects) {
			
			// 2.1 Get the project name
			String projectName = project.getName();
			
			try {
				
				// 2.2 Read the XML file 
				SAXBuilder builder = new SAXBuilder();
				Document doc = builder.build(new File(ckjmFolder.getAbsolutePath() + "/" + projectName + "/ckjmResults.xml"));
				Element root = (Element) doc.getRootElement();
				
				// 2.3 Get the child nodes of the xml document
				List<Element> children = root.getChildren();
				
				// 2.4 Check if it contains any child nodes
				if (children.size() != 0) {
					
					//TODO: Remove this print
					if (printMessages) {
						System.out.println("Project: " + projectName + " CKJM: YES");
					}
					
					// Increment the counter
					k++;
					
					// Create the folder that corresponds to this project
					File folder = new File(destFolder.getAbsolutePath() + "/" + project.getName());
					folder.mkdirs();
					
					// Copy the contents of the source folder to the destination directory
					FileUtils.copyDirectory(project, folder);
					
				} else {
					if (printMessages) {
						System.out.println("Project: " + projectName + " CKJM: NO");
					}
				}
				
				} catch (Exception e) {
					if (printMessages) {
						System.out.println("Project: " + projectName + " CKJM: NO");
					}
				}
			
			
		}
		if (printMessages) {
			System.out.println("Applicable: " + k);
		}
		
	}
	
	/**
	 * This main() method should be used only for testing the present class.
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		
		// 1. Source and destination paths
		String srcPath = GitCloner.BASE_DIR+"/compiled";
		String ckjmPath = GitCloner.BASE_DIR+"/ckjm_analyzable";
		String destPath = GitCloner.BASE_DIR+"/final_repo";
		
		// 2. Create the destination folder
		new File(ckjmPath).mkdirs();
		
		// 3. Analyze the projects
		CKJMAnalyzer ckjm = new CKJMAnalyzer();
		ckjm.analyzeBenchmark(srcPath, ckjmPath);
		
		// 4. Check CKJM Applicability and Final Benchmark Construction
		copyApplicable(srcPath, ckjmPath, destPath);
	}
}