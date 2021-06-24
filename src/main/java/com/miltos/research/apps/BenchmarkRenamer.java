package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jgit.util.FileUtils;

public class BenchmarkRenamer {
	
	// Necessary fields
	private static String BENCH_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\Repo").getAbsolutePath(); 
	
	public static void renameDirectories(String path) throws IOException {
		
		// 0. List all the files contained in the given folder
		File srcFolder = new File(path);
		File[] folders = srcFolder.listFiles();
		
		// 1. Sort them based on their name
		Arrays.sort(folders, new Comparator<File>() {
		    public int compare(File a, File b ) {
		    	String[] aName = a.getName().split("\\.");
		    	String[] bName = b.getName().split("\\.");
		        return Integer.parseInt(aName[0]) - Integer.parseInt(bName[0]);
		    }
		});
		
		// 2. Iterate through the folders and rename them
		int k = 0;
		for (File folder : folders) {
			// 2.1 Increment the counter
			k++;
			
			// 2.2 Construct the new name
			String[] oldName = folder.getName().split("\\.");
			String newName = k + "." + oldName[1];
			
			// 2.3 Rename the folders accordingly
			FileUtils.rename(folder, new File(path + "/" + newName));
		}
	}
	public static void main(String[] args) throws IOException {
		// Rename the projects of the final benchmark repository
		renameDirectories(BENCH_PATH);
	}

}
