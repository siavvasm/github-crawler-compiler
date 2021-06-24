package com.miltos.research.apps;

import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

/**
 * This class is responsible for executing Maven commands.
 * @author Miltos
 *
 */
public class MavenInvoker {
	
	//public static String MAVEN_HOME = "C:\\Program Files (x86)\\apache-maven-3.3.9";
	public static String MAVEN_HOME = "C:\\Users\\siavvasm.ITI-THERMI.000\\apache-maven-3.3.9-bin\\apache-maven-3.3.9";
	
	private String projectPath;
	private String pomXmlPath;
	
	// Testing flags
	private static boolean printMessages = false;
	/*
	 * The constructor methods of the class...
	 */
	
	public MavenInvoker() {
		this.projectPath = "";
		this.pomXmlPath = "";
	}
	
	public MavenInvoker(String projectPath){
		this.projectPath = projectPath;
		this.pomXmlPath = new File(projectPath + "/pom.xml").getAbsolutePath();
	}
	
	/*
	 * Setters and Getters...
	 */
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getPomXmlPath() {
		return pomXmlPath;
	}

	public void setPomXmlPath(String pomXmlPath) {
		this.pomXmlPath = pomXmlPath;
	}
	
	
	/**
	 * Execute the compile command for the desired project.
	 */
	public void mavenCompileSimple() throws MavenInvocationException{
		
		// 1. Create the request
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( new File(pomXmlPath));
		request.setGoals(Collections.singletonList("compile -Dmaven.test.skip=true"));
		
		// 2. Execute the request
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File(MAVEN_HOME));//Set the Maven Home directory
		InvocationResult result = invoker.execute(request);
		
		// 3. Check for failure
		if(result.getExitCode() != 0){
			if (printMessages) {
				System.out.println("Build failed.");
			}
		}
	}
	
	/**
	 * Execute the compile command for the desired project and 
	 * return the result of the compilation.
	 */
	public InvocationResult mavenCompile() throws MavenInvocationException{
		
		// 1. Create the request
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( new File(pomXmlPath));
		request.setGoals(Collections.singletonList("compile -Dmaven.test.skip=true -Dcheckstyle.skip=true"));
		
		// 2. Execute the request
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File(MAVEN_HOME));//Set the Maven Home directory
		InvocationResult result = invoker.execute(request);
		
		// 3. Return the result of the compilation
		return result;
	}
	
	/*
	 * Test the Maven Compiler ...
	 */
	public static void main(String[] args) throws MavenInvocationException {
		
		// 0. Define the project path
		String path = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\workspace-neon-ee\\github-crawler-compiler\\Benchmark_Repo_small\\4.guava").getAbsolutePath();
		
		// 1. Construct the MavenInvoker object
		MavenInvoker invoker = new MavenInvoker(path);
		
		// 2. Compile the selected project
		InvocationResult result = invoker.mavenCompile();
		
		if(result.getExitCode() != 0){
			if (printMessages) {
				System.out.println("Build Failed :(");
			}
		} else {
			if (printMessages) {
				System.out.println("Build Successful");
			}
		}
		
		
	}

}