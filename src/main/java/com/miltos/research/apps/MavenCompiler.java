package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenCompiler {
	
	//Member variables
	private String repositoryPath;
	private String destPath;
	
	// Testing flags
	private static boolean printMessages = false;
	
	/*
	 * Constructors...
	 */
	public MavenCompiler(String destPath) {
		this.destPath = destPath;
		new File(this.destPath).mkdirs();
	}
	
	public MavenCompiler(String repositoryPath, String destPath) {
		this.repositoryPath = repositoryPath;
		this.destPath = destPath;
		new File(this.destPath).mkdirs();
	}
	
	/*
	 * Setters and Getters...
	 */
	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	/*
	 * Main functionality
	 */
	
	/**
	 * This method is responsible for compiling the software products which are located in a 
	 * specific folder. Subsequently, the successfully compiled products are copied to a desired 
	 * location.
	 */
	public void buildAndCopy() throws MavenInvocationException, IOException {
		
		// 1. Get the names of the projects
		File benchmarkRepo = new File(repositoryPath);
		File[] projects = benchmarkRepo.listFiles();
		
		// 2. For each each project found in the benchmark repository ...
		int k = 0;
		for (File project: projects) {
			
			//TODO: Remove this print
			if (printMessages) {
				System.out.println("Compiling " + project.getName() + " ...");
			}
			
			// 2.1. Construct the MavenInvoker object
			MavenInvoker invoker = new MavenInvoker(project.getAbsolutePath());
			
			// 2.2. Compile the selected project
			InvocationResult result = invoker.mavenCompile();
			
			// 2.3. Check if it was successfully compiled
			if(result.getExitCode() != 0){
				if (printMessages) {
					System.out.println("Build Failed!");
				}
			} else {
				if (printMessages) {
					System.out.println("Build Successful!");
				}
				
				// Increment the counter of the successfully compiled software products
				k++;
				
				// 3.1 Create the folder that corresponds to this software product
				String folderName = project.getName();
				File folder = new File( destPath + "/" + folderName);
				folder.mkdirs();
				
				// 3.2 Copy the contents of the successfully compiled software product to the new destination
				FileUtils.copyDirectory(project, folder);

			}
		}
	}
	/**
	 * This main() method should be used only for testing the present class.
	 */
	public static void main(String[] args) throws MavenInvocationException, IOException {
		
		// Construct, initialize the MavenCompiler 
		MavenCompiler compiler = new MavenCompiler(GitCloner.BASE_DIR+"/downloaded",GitCloner.BASE_DIR+"/compiled");
		
		// Invoke the compiler
		compiler.buildAndCopy();
	}

}
