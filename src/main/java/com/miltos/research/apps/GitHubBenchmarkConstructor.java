package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.jdom2.JDOMException;

public class GitHubBenchmarkConstructor {
	
	// Required Paths
	public static String BASE_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();
	public static String GIT_REPO = new File(BASE_DIR + "/Benchmark_Repo").getAbsolutePath();
	public static String COMPILED_REPO = new File(BASE_DIR + "/Compiled_Repo").getAbsolutePath();
	public static String CKJM_REPO = new File(BASE_DIR + "/CKJM_Applicable").getAbsolutePath();
	public static String FINAL_REPO = new File(BASE_DIR + "/Final_Benchmark").getAbsolutePath();

	// GitHub-related required information
	private static String QUERY = "https://api.github.com/search/repositories?q=java+language:java&sort=stars&order=desc&per_page=100&page=1";
	private static String USERNAME = "githubCrawler";
	private static String PASSWORD = "12341234@@github";
	private static int totalNumProjects = 6;
	
	public static void main(String[] args) throws InterruptedException, IOException, MavenInvocationException, JDOMException {
		
		/*
		 * 1. GitHub Downloader
		 */
		
		System.out.println(BASE_DIR);
		
		//TODO: Remove these prints
		System.out.println("**** Benchmark Constructor ****");
		System.out.println("* ");
		System.out.println("* GitHub Downloader invoked...");
		System.out.println("* Downloading GitHub Projects ...");
		System.out.println("* Please wait...");
		
		// 1.1 Construct the GitHub downloader
		GithubDownloader gitDownloader = new GithubDownloader();
		gitDownloader.setQuery(QUERY);
		gitDownloader.setMaxProjects(totalNumProjects);
		
		// 1.2 Download the desired projects
		//gitDownloader.downloadBenchmarkRepo(GIT_REPO);
		gitDownloader.downloadBenchmarkRepo(GIT_REPO);
		
		//TODO: Remove these prints
		System.out.println("*");
		System.out.println("* GitHub Projects successfully downloaded!");
		
		/*
		 * 2. Maven Compiler
		 */
		
		//TODO: Remove these prints
		System.out.println("* Maven Compiler invoked...");
		System.out.println("* Compiling the downloaded GitHub Projects ...");
		System.out.println("* Please wait...");
		
		// 2.1 Construct, initialize, and invoke the MavenCompiler 
		MavenCompiler compiler = new MavenCompiler(GIT_REPO,COMPILED_REPO);
		//MavenCompiler compiler = new MavenCompiler("D:\\SAM_Paper_Exp\\Workspace\\0.Repo","D:\\SAM_Paper_Exp\\Workspace\\4.Injected_0.25");
		compiler.buildAndCopy();
		
		//TODO: Remove these prints
		System.out.println("*");
		System.out.println("* GitHub Projects successfully compiled!");
		
		/*
		 * 3. CKJM Analyzer and final Benchmark Constructor
		 */
		
		//TODO: Remove these prints
//		System.out.println("* CKJM Analyzer invoked...");
//		System.out.println("* Analyzing the compiled GitHub Projects ...");
//		System.out.println("* Please wait...");
//		
//		// 3.1 Create the folder where the CKJM results will be placed
//   		new File(CKJM_REPO).mkdirs();
//		
//		// 3.2 Analyze the compiled projects using CKJM
//		CKJMAnalyzer ckjm = new CKJMAnalyzer();
//		ckjm.analyzeBenchmark(COMPILED_REPO, CKJM_REPO);
//		
//		// 3.3 Find the projects that can be analyzed with CKJM and add them to the final repository
//		CKJMAnalyzer.copyApplicable(COMPILED_REPO, CKJM_REPO, FINAL_REPO);
//		
//		System.out.println("*");
//		System.out.println("* GitHub Projects successfully analyzed!");
//		System.out.println("* ");
//		System.out.println("* The final benchmark repository is located at: " + FINAL_REPO);
		
		/*
		 * 3. CCCC Analyzer and final Benchmark Constructor
		 */
		
//		//TODO: Remove these prints
//		System.out.println("* C/C++ Project Identifier...");
//		System.out.println("* Ensuring that the selected projects contain C/C++ files ...");
//		System.out.println("* Please wait...");
//		
//		// 3.1 Create the folder where the the selected projects will be placed
//		String downloadedRepo = "D:\\Benchmark_Repository_Cpp";
//		String finalRepo = "D:\\Cpp_Repo";
//   		new File("").mkdirs();
//		
//		// 3.3 Find the projects that can be analyzed with CKJM and add them to the final repository
//		CppChecker.copyApplicable(downloadedRepo, finalRepo);
//		
//		System.out.println("*");
//		System.out.println("* GitHub Projects successfully analyzed!");
//		System.out.println("* ");
//		System.out.println("* The final");
		
		/*
		 * 3. CCCC Analyzer and final Benchmark Constructor
		 */
		
//		//TODO: Remove these prints
//		System.out.println("* CCCC Analyzer invoked...");
//		System.out.println("* Analyzing the compiled GitHub Projects ...");
//		System.out.println("* Please wait...");
//		
//		// 3.1 Create the folder where the CKJM results will be placed
//		String downloadedRepo = "D:\\Benchmark_Repository_C_2";
//		String ccccRepo = "D:\\CCCC_Repo_C_3";
//		String finalRepo = "D:\\Final_Repo_C_3";
//
//		
//		// 3.2 aNALYSE the compiled projects using CCCC
//		CCCCAnalyzer cccc = new CCCCAnalyzer();
//		cccc.analyzeBenchmark(downloadedRepo, ccccRepo);
//		
//		// 3.3 Find the projects that can be analyzed with CKJM and add them to the final repository
//		CCCCAnalyzer.copyApplicable(downloadedRepo, ccccRepo, finalRepo);
//		
//		System.out.println("*");
//		System.out.println("* GitHub Projects successfully analyzed!");
//		System.out.println("* ");
//		System.out.println("* The final benchmark repository is located at: " + FINAL_REPO);
		

	}

}
