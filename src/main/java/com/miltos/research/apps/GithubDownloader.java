package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.jdom2.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GithubDownloader {
	
	// Member variables
	private ArrayList<String> resultPages;
	private String query;
	private int maxProjects;
	private String destPath;
	
	// Test variables
	private static String QUERY = "https://api.github.com/search/repositories?q=Java+language:Java&sort=stars&order=desc&per_page=100&page=1";
	private static String USERNAME = "githubCrawler";
	private static String PASSWORD = "12341234@@github";
	
	// Testing flags
	private static boolean printMessages = true;
	
	/*
	 *  Constructors
	 */
	public GithubDownloader() {
		resultPages = new ArrayList<>();
	}
	
	public GithubDownloader(String query, String destPath, int maxProjects) {
		resultPages = new ArrayList<>();
		this.query = query;
		this.destPath = destPath;
		this.maxProjects = maxProjects;
	}

	
	/*
	 *  Getters and Setters
	 */
	public ArrayList<String> getResultPages() {
		return resultPages;
	}

	public void setResultPages(ArrayList<String> resultPages) {
		this.resultPages = resultPages;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getMaxProjects() {
		return maxProjects;
	}

	public void setMaxProjects(int maxProjects) {
		this.maxProjects = maxProjects;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	/**
	 * This method downloads all the GitHub repositories that are relevant to a given query
	 * and stores them in the predifined folder that is located at the path defined by 
	 * the GIT_REPO static variable of the present class.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void downloadBenchmarkRepo() throws InterruptedException, IOException {
		
		// 0. Create a JAX-RS Client for submitting requests
		Client client = ClientBuilder.newClient();
		
		// 1. Set the target URL of the client to which the requests will be submitted
		Response response = client.target(query)
			  .request().get();
		
		// 2. Get the first JSON result page 
		String jsonString = response.readEntity(String.class);
		
		//TODO: Remove these prints
		if (printMessages) {
			System.out.println(jsonString);
			System.out.println(response.getHeaderString("Link"));
		}

		// 3. Get the total number of pages
		int totalPages = retrieveTotalPagesNumber(response);
		
		//TODO: Remove this print
		if (printMessages) {
			System.out.println("TOTAL PAGES NUMBER: " + totalPages);
		}
		
		// 4. Store the first JSON String in the ArrayList
		resultPages.add(jsonString);
		
		// 5.Retrieve and store all the other JSON result pages
		retrieveJsonPages(totalPages);
		
		//TODO: Remove these prints
		if (printMessages) {
			System.out.println("****** Github Pages ****");
			for (int i = 0; i < resultPages.size(); i++) {
				System.out.println(resultPages.get(i));
			}
		}
		
		// 6. Log the result pages
		JsonLogger.saveFullResultsList(resultPages);
		JsonLogger.saveMultipleJsonFiles(resultPages);
		
		// 7. Create a GitCloner object and an ArrayList for storing the Maven Projects of each page
		GitCloner gitCloner = new GitCloner();
		ArrayList<Integer> mavenProjectsPerPage = new ArrayList<>();
		int index = 0;
		
		// 8. Create the root Element of the XML file
		Element root = new Element("projects");
		root.setName("projects");
		
		// 9. For each result page...
		for (int i = 0; i < resultPages.size(); i++) {
		
			// 9.1 Get the JSON with the i-th result page
			JSONObject page = new JSONObject(resultPages.get(i));
			JSONArray repos = page.getJSONArray("items");
			
			// 9.2 For each project of this result page...
			int k = 0; // Counter of Maven Projects
			for (int j = 0; j < repos.length(); j++){

				// Get the most important attributes of the present project
				String name = repos.getJSONObject(j).getString("name");
				String cloneURL = repos.getJSONObject(j).getString("clone_url");
				String apiURL = repos.getJSONObject(j).getString("url");
				int stars = repos.getJSONObject(j).getInt("stargazers_count");
				

				// Check if it contains a pom.xml file
				Client pomClient = ClientBuilder.newClient();
				
				// Construct the Authorization header (Base64 credentials encoding)
				String encoded = getBasicAuthenticationCode(USERNAME, PASSWORD);
				
				// Set the target URL of the client to which the requests will be submitted
				Response pomResponse = client.target(apiURL + "/contents/pom.xml")
					  .request().header("Authorization", "Basic " + encoded).get();
				
				// Parse the response
				JSONObject pomResponseJSON = new JSONObject(pomResponse.readEntity(String.class));
				
				// Check if it is a Maven Project and clone it locally
				boolean isMaven = false;
				try {
					
					// Is it a Maven Project?
					if(pomResponseJSON.getString("name") != null) {
						
						// If yes, print a message to the console
						if (printMessages) {
							System.out.println("Maven!");
						}
						
						// Increment the counters
						k++;
						index++;
						
						// Download the project
						GitCloner.setRemotePath(cloneURL);
						
						try {
							gitCloner.init(index);
						} catch (IOException e1) {
							System.out.println("Init Failed!!!");
						}
						
						try {
							gitCloner.testClone();
						} catch (IOException | GitAPIException e) {
							System.out.println("Clone Failed!!!");
						}
						
						
						isMaven = true;
					} else {
						
						if(printMessages) {
							System.out.println("Not Maven!");
						}
						
						isMaven = false;
					}
					
				} catch (JSONException e) {
					
					if (printMessages) {
						System.out.println("Not Maven!");
					}
					
					
				}
				
				//TODO: Remove these prints
				if (printMessages) {
					System.out.println("********** Name: " + name + " *************");
					System.out.println(" * Clone URL: " + cloneURL);
					System.out.println(" * API URL  : " + apiURL);
					System.out.println(" * Stars    : " + stars);
					System.out.println("*******************************************");
				}
				
				
				// Add a corresponding entry in the XML file
				Element nameNode = new Element("name");
				nameNode.setText(name);
				
				Element cloneUrlNode = new Element("cloneURL");
				cloneUrlNode.setText(cloneURL);
				
				Element apiUrlNode = new Element("apiURL");
				apiUrlNode.setText(apiURL);
				
				Element starsNode = new Element("stars");
				starsNode.setText(stars + "");
				
				Element isMavenNode = new Element("isMaven");
				if (isMaven) {
					isMavenNode.setText("yes");
				} else {
					isMavenNode.setText("no");
				}
				
				Element proj = new Element("project");
				proj.addContent(nameNode);
				proj.addContent(cloneUrlNode);
				proj.addContent(apiUrlNode);
				proj.addContent(starsNode);
				proj.addContent(isMavenNode);
				
				root.addContent(proj);

			}
			//TODO: Remove this print
			if (printMessages) {
				System.out.println("Total Number : " + k);
			}
			
			mavenProjectsPerPage.add(k);
		}
		
		XmlLogger.exportXML(root, XmlLogger.PATH);
		
		if (printMessages) {
			for (int i = 0; i < mavenProjectsPerPage.size(); i++) {
				System.out.println("Page " + i + ": " + mavenProjectsPerPage.get(i));
			}
		}
	}
	
	/**
	 * This method downloads the top N GitHub repositories that are relevant to a given query
	 * and stores them in a folder that is located at a path defined by the user through the 
	 * method parameter named gitPath. The value of N is defined by the value of the maxProjects 
	 * member variable of the present class class.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void downloadBenchmarkRepo(String gitPath) throws InterruptedException, IOException {
		
		// 0. Create a JAX-RS Client for submitting requests
		Client client = ClientBuilder.newClient();
		
		// 1. Set the target URL of the client to which the requests will be submitted
		Response response = client.target(query)
			  .request().get();
		
		// 2. Get the first JSON results page
		String jsonString = response.readEntity(String.class);
		if (printMessages) {
			System.out.println(jsonString);
			System.out.println(response.getHeaderString("Link"));
		}
		
		// 3. Get the total number of pages
		int totalPages = retrieveTotalPagesNumber(response);
		
		//TODO: Remove this print
		if (printMessages) {
			System.out.println("TOTAL PAGES NUMBER: " + totalPages);
		}
		
		// 4. Store the first JSON String in the ArrayList
		resultPages.add(jsonString);
		
		// 5. Store all the other JSON pages
		retrieveJsonPages(totalPages);
		
		//TODO: Remove this prints
		if (printMessages) {
			System.out.println("****** Github Pages ****");
			for (int i = 0; i < resultPages.size(); i++) {
				System.out.println(resultPages.get(i));
			}
		}

		// 6. Log the result pages
		JsonLogger.saveFullResultsList(resultPages);
		JsonLogger.saveMultipleJsonFiles(resultPages);

		// 7. Construct the GitCloner object and the ArrayList with the projects
		GitCloner gitCloner = new GitCloner(gitPath, "");
		ArrayList<Integer> mavenProjectsPerPage = new ArrayList<>();
		int index = 0; // A counter required for setting the name of the projects
		
		// 8. Create the root Element of the XML file
		Element root = new Element("projects");
		root.setName("projects");
		
		// 9. For each result page...
		for (int i = 0; i < resultPages.size(); i++) {
		
			// 9.1 Get the JSON with the i-th result page
			JSONObject page = new JSONObject(resultPages.get(i));
			JSONArray repos = page.getJSONArray("items");
			
			// 9.2 For each project of this result page...
			int k = 0;
			for (int j = 0; j < repos.length(); j++){
				
				if (j > 20 && j < 25){
					continue;
				}
				
				// Get the most important attributes of each project
				String name = repos.getJSONObject(j).getString("name");
				String cloneURL = repos.getJSONObject(j).getString("clone_url");
				String apiURL = repos.getJSONObject(j).getString("url");
				int stars = repos.getJSONObject(j).getInt("stargazers_count");
				
				// Check if it contains a pom.xml file
				Client pomClient = ClientBuilder.newClient();
				
				// Construct the Authorization header (Base64 credentials encoding)
				String encoded = getBasicAuthenticationCode(USERNAME, PASSWORD);
				
				// Set the target URL of the client to which the requests will be submitted
				Response pomResponse = client.target(apiURL + "/contents/pom.xml")
					  .request().header("Authorization", "Basic " + encoded).get();
				
				// Parse the response
				JSONObject pomResponseJSON = new JSONObject(pomResponse.readEntity(String.class));
				
				// Check if it is a Maven Project and clone it locally
				boolean isMaven = false;
				//try {
					// Is it a Maven Project?
//					if(pomResponseJSON.getString("name") != null) {
						
						// If yes, print an relevant message to the screen
						if (printMessages) {
							System.out.println("Cpp");
						}
						
						// Increment the counters
						k++;
						index++;
						
						// Download the project
						GitCloner.setRemotePath(cloneURL);
						
						try {
							gitCloner.init(index, gitPath);
						} catch (IOException e1) {
							System.out.println("Init Failed!!!");
						}
						
						try {
							gitCloner.testClone();
						} catch (IOException | GitAPIException e) {
							System.out.println("Clone Failed!!!");
						}
						
						isMaven = true;
//					} else {
//						System.out.println("Not Maven!");
//						isMaven = false;
//					}
//				} catch (JSONException e) {
//					if (printMessages) {
//						System.out.println("Not Maven!");
//					}
//					
//				}
				
				//TODO: Remove these prints
				if (printMessages) {
					System.out.println("********** Name: " + name + " *************");
					System.out.println(" * Clone URL: " + cloneURL);
					System.out.println(" * API URL  : " + apiURL);
					System.out.println(" * Stars    : " + stars);
					System.out.println("*******************************************");
				}

				
				// Add a corresponding entry in the XML file
				Element nameNode = new Element("name");
				nameNode.setText(name);
				
				Element cloneUrlNode = new Element("cloneURL");
				cloneUrlNode.setText(cloneURL);
				
				Element apiUrlNode = new Element("apiURL");
				apiUrlNode.setText(apiURL);
				
				Element starsNode = new Element("stars");
				starsNode.setText(stars + "");
				
				Element isMavenNode = new Element("isMaven");
				if (isMaven) {
					isMavenNode.setText("yes");
				} else {
					isMavenNode.setText("no");
				}
				
				Element proj = new Element("project");
				proj.addContent(nameNode);
				proj.addContent(cloneUrlNode);
				proj.addContent(apiUrlNode);
				proj.addContent(starsNode);
				proj.addContent(isMavenNode);
				
				root.addContent(proj);
				
				// SOS: Stop the process if the desired number of projects has been downloaded
				if (k >= maxProjects) {
					break;
				}
			}
			if (printMessages) {
				System.out.println("Total Number : " + k);
			}

			mavenProjectsPerPage.add(k);
			
			// SOS: Stop the process if the desired number of projects has been downloaded
			if (k >= maxProjects) {
				break;
			}
		}
		
		// Export an XML file containing the information of the downloaded software projects
		XmlLogger.exportXML(root, XmlLogger.PATH);
		
		//TODO: Remove these prints
		if (printMessages) {
			for (int i = 0; i < mavenProjectsPerPage.size(); i++) {
				System.out.println("Page " + i + ": " + mavenProjectsPerPage.get(i));
			}
		}

	}

	/**
	 * This method is responsible for constructing the Authorization Header required for 
	 * submitting a GitHub request using Base64 encoding.
	 * 
	 * @param username: The username of the GitHub user
	 * @param password: The password of the GitHub user
	 */
	private static String getBasicAuthenticationCode(String username, String password) {
		
		// 1. Combine the username and password
		String combined = username + ":" + password;
		
		// 2. Encode the combined token
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		
		// 3. Return the encoded token
		return new String(encodedBytes);
	}
	
	/**
	 * This method is responsible for retrieving all the result pages (i.e. JSON files) of
	 * a given query. 
	 */
	private void retrieveJsonPages(int totalPages) throws InterruptedException {
		
		// 0. Create a JAX-RS Client for submitting requests
		Client client = ClientBuilder.newClient();
		
		// 1. For each results page do...
		for (int i = 1; i < totalPages; i++) {
			
			//TODO: Remove this print
			if (printMessages) {
				System.out.println(i);
			}
			
			// 1.1 Construct the query
			String nextPageQuery = "https://api.github.com/search/repositories?q=Java+language:Java&sort=stars&order=desc&per_page=100" + "&page=" + (i+1);
		
			// 1.2 Submit the request to the GitHub search API
			Response response = client.target(nextPageQuery)
					  .request().get();
			
			// 1.3 Add the retrieved JSON to the corresponding list
			resultPages.add(response.readEntity(String.class));
			
			// 1.4 Sleep for a short period to avoid being blocked by the GitHub Server
			TimeUnit.SECONDS.sleep(10);
		}
		
	}

	/**
	 * This method is responsible for determining the total number of result pages that can
	 * be retrieved by a given query.
	 */
	private static int retrieveTotalPagesNumber(Response response) {
		
		// 1. Get the "Link" header and split it into three strings
		String[] splittedText = response.getHeaderString("Link").split("&page=");
		
		//TODO: Remove these prints
		if (printMessages) {
			System.out.println(splittedText[0]);
			System.out.println(splittedText[1]);
			System.out.println(splittedText[2]);
		}

		// 2. Get the first integer of the third string
	    Pattern pattern = Pattern.compile("^([0-9]+)");
	    Matcher matcher = pattern.matcher(splittedText[2]);
	   
	    // 3. Find the first integer value 
	    if (matcher.find()) {
	    	 if (printMessages) {
	    		 System.out.println(matcher.group(0));
	    	 }
	    } 
	   
	    // 3. Return the retrieved value (i.e. total number of result pages)
		return Integer.parseInt(matcher.group(0));
	}
	
	/**
	 * This main method is used only for testing the present class. 
	 */
	public static void main(String[] args) throws InterruptedException, IOException, GitAPIException {
		
		// 1. Construct the downloader
		GithubDownloader gitDownloader = new GithubDownloader();
		gitDownloader.setQuery(QUERY);
		gitDownloader.setMaxProjects(10);
		
		// 2. Download the projects
		gitDownloader.downloadBenchmarkRepo(GitCloner.BASE_DIR+"/downloaded");
		
		System.out.println("DONE!");
	}


}
