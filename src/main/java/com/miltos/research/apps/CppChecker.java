package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DirectoryScanner;

public class CppChecker {
	
	public static void copyApplicable(String downloadedRepo, String finalRepo) throws IOException {
		
		// 1. Get the projects 
		File[] projects = new File(downloadedRepo).listFiles();
		
		// Create a scanner for this project
		DirectoryScanner javaFileScanner = new DirectoryScanner();
		String[] javaFiles = null;
		
		//Check each file if it contains the appropriate files for the analysis
		boolean problem = false;
		
		for (File project : projects) {
			
			//If this file is not a directory continue to the next one
			if(!project.isDirectory()){
				continue;
			}
			
			//System.out.print("* Progress : " + (int) (progress/projects.length * 100) + " %\r");
			//Scan the current directory for the files needed for the analysis
			javaFileScanner.setIncludes(new String[]{"**/*.cpp","**/*.c","**/*.h","**/*.cc"});
			javaFileScanner.setBasedir(project.getAbsolutePath());
			javaFileScanner.setCaseSensitive(false);
			javaFileScanner.scan();
			javaFiles = javaFileScanner.getIncludedFiles();
			
			//Check if this directory contains java and class or jar files...
			if(javaFiles.length == 0){
				System.out.println(project.getName()+ " C/C++ Files: NO ");
			} else {
				System.out.println(project.getName()+ " C/C++ Files: YES ");
				// Create the folder that corresponds to this project
				File folder = new File(new File(finalRepo).getAbsolutePath() + "/" + project.getName());
				folder.mkdirs();
				
				// Copy the contents of the source folder to the destination directory
				FileUtils.copyDirectory(new File(downloadedRepo + "/" + project.getName()), folder);
			}

			
			
		}
		
	}

}
