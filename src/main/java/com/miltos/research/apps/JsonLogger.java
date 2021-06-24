package com.miltos.research.apps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class JsonLogger {
	
	// Required Paths
	public static String BASE_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();
	public static String JSON_RESULT_PAGES_PATH = new File(BASE_DIR + "/Logging/JSON_Result_Pages").getAbsolutePath(); 
	
	/**
	 * This method is responsible for creating the folder in which the log files are 
	 * placed.
	 */
	public static void createDestFolder(String destPath) {
		new File(destPath).mkdirs();
	}
	
	/**
	 * This method is responsible for storing all the result JSON files in a  
	 * specific location.
	 *
	 * @param resultPages: An ArrayList containing the JSON Strings that need to be saved
	 * @param destPath: The path of the desired folder in which the JSON strings should be placed.
	 */
	public static void saveMultipleJsonFiles(ArrayList<String> resultPages, String destPath) {
		
		// Create the destination folder (if needed)
		createDestFolder(destPath);
		
		// For each result JSON String ...
		for(int i = 0; i < resultPages.size(); i++) {
			
			// Define the required parameters
			String name = "page-" + (i+1) + ".json";
			String jsonString = resultPages.get(i);
			
			// Save a single JSON file
			saveSingleJson(jsonString, destPath, name);
			
		}
		
	}
	
	/**
	 * This method is responsible for storing a single JSON String that corresponds to a single  
	 * result page retrieved for a given query in a specific path.
	 */
	public static void saveSingleJson(String jsonString, String destPath, String name) {
		
		// Create the destination folder (if needed)
		createDestFolder(destPath);
		
		// Save the complete list with the JSON Strings of the result pages
		try{ 
		    FileWriter writer = new FileWriter(destPath + "/" + name);
		    writer.write(jsonString);
		    writer.close();
		}catch(IOException e){
		    System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is responsible for storing the complete list with the JSON Strings of the 
	 * result pages retrieved for a given query in a specific path.
	 */
	public static void saveFullResultsList(ArrayList<String> resultPages, String destPath) {
		
		// Create the folder (if needed)
		createDestFolder(destPath);
		
		// Instantiate a Json Parser.
		Gson gson = new Gson();
		
		// Create the Json String of the projects.
		String json = gson.toJson(resultPages);
		
		// Save the results in the desired folder.
		try{ 
		    FileWriter writer = new FileWriter(destPath + "/results.json");
		    writer.write(json);
		    writer.close();
		}catch(IOException e){
		    System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is responsible for storing the complete list with the JSON Strings of the 
	 * result pages, which were retrieved for a given query, in the predefined folder.
	 * 
	 */
	public static void saveFullResultsList(ArrayList<String> resultPages) {
		
		// Create the folder (if needed)
		createDestFolder(JSON_RESULT_PAGES_PATH);
		
		// Instantiate a Json Parser.
		Gson gson = new Gson();
		
		// Create the Json String of the projects.
		String json = gson.toJson(resultPages);
		
		// Save the results in the desired folder.
		try{ 
		    FileWriter writer = new FileWriter(JSON_RESULT_PAGES_PATH + "/results.json");
		    writer.write(json);
		    writer.close();
		}catch(IOException e){
		    System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is responsible for storing the JSON Strings with the result pages
	 * in a predefined location.
	 * 
	 * @param resultPages
	 */
	public static void saveMultipleJsonFiles(ArrayList<String> resultPages) {
		
		// Create the destination folder (if needed)
		createDestFolder(JSON_RESULT_PAGES_PATH);
		
		// For each result JSON String ...
		for(int i = 0; i < resultPages.size(); i++) {
			
			// Define the required parameters
			String name = "page-" + (i+1) + ".json";
			String jsonString = resultPages.get(i);
			
			// 1. Call the single saver
			saveSingleJson(jsonString, JSON_RESULT_PAGES_PATH , name);
			
		}
		
	}

}
